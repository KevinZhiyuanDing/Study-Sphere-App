import asyncio
from playwright.async_api import async_playwright, TimeoutError as PlaywrightTimeoutError
from datetime import datetime, timedelta
import re
import csv
import threading
import copy
import os.path

class Webscrape:
    """
    A class for asynchronously scraping the UBC booking page and extracting room information.

    This class provides methods for getting the url after filtering for criteria, getting the
    room information from the url after applying filters, getting all the rooms that fit
    the user inputted timeframe.

    Attributes:
        start_time (string): The start time of the timeframe
        end_time (string): The end time of the timeframe
        start_date (string): The start date of the timeframe
        end_date (string): The end date of the timeframe

    Representation Invariant:
        start_time, end_time are always of the format 'HH:MM'
        start_date, end_date are always of the format 'YYYY-MM-DD'
        start_time < end_time
        start_date <= end_date
        file_name is either 'rooms1.csv' or 'rooms2.csv'


    Methods:
        continuous_scrape: Sequentially initiates webscrape operations to keep data updated

        wait_for_file: Checks if a file is being read, and will block until the file is no longer being accessed

        process_request: Calls get_all_rooms_within_timeframe by passing in user inputted
                            values as arguments

        get_rooms_info: Gets the room information for a given result url page

        get_url_from_filter: Gets the post-search url after applying the filter

        get_all_rooms: Gets all the room data from all five study spaces corresponding to the
                        start time, end time, date passed in

        get_all_rooms_within_timeframe: Scrapes all the room data for all five study spaces corresponding
                                        to the time frame provided and stores it in a file specified by file_name

        save_to_csv: Saves room data to csv file

        readDataFromCSV: Gets the rooms that fit the search criteria from the file that is not currently being written to
                            by the webscrape operation.

        stop_scraping: Stops the webscraping operation for continuous_scraping and ends the loop

        check_complete: Checks if a webscrape operation has successfully completed
    """

    MAX_CONCURRENT_TASKS = 5
    sem = asyncio.Semaphore(MAX_CONCURRENT_TASKS)

    def __init__(self, start_time, end_time, start_date, end_date):
        """
        Initializes the Webscrape object with the timeframe and file name.

        :param start_time: the start time of the timeframe
        :param end_time: the end time of the timeframe
        :param start_date: the start date of the timeframe
        :param end_date: the end date of the timeframe

        :raises ValueError: If the time or date is in an invalid format, or if
                            start time is later than or equal to end time, start date is later than
                            or equal to end date
        """

        time_pattern = r'^[0-2][0-9]:[0-5][0-9]$'
        date_pattern = r'^\d\d\d\d-\d\d-\d\d$'
        if not re.match(time_pattern,start_time) or not re.match(time_pattern,end_time):
            raise ValueError("Start time or end time is in an invalid format. Expected 'HH:MM'.")
        elif not re.match(date_pattern,start_date) or not re.match(date_pattern,end_date):
            raise ValueError("Start date or end date is in an invalid format. Expected 'YYYY-MM-DD'.")
        elif start_time >= end_time:
            raise ValueError("Start time is later than end time")
        elif start_date > end_date:
            raise ValueError("Start date is later than end date")

        self.__start_time = start_time
        self.__end_time = end_time
        self.__start_date = start_date
        self.__end_date = end_date
        self.__file_name = 'rooms1.csv'
        self.__stop_event = threading.Event()
        self.__finished = False
        self.__file_read_occurring = {'rooms1.csv': False,
                                      'rooms2.csv': False}

    async def continuous_scrape(self, loop):
        """
        Continuously loops and executes webscrape operations sequentially (only starts
        a new webscrape operation once the previous one is finished)

        :return: None
        """
        while not self.__stop_event.is_set():
            self.__finished = False

            await self.__wait_for_file(self.__file_name)

            await self.process_request()

            self.__file_name = 'rooms1.csv' if self.__file_name == 'rooms2.csv' else 'rooms2.csv'

            await asyncio.sleep(5)

        self.__finished = True
        loop.stop()

    async def __wait_for_file(self, file_name):
        """
        Blocks the next webscrape from happening until the file that is intended to be written to is no longer
        being accessed

        :return: None
        """
        while self.__file_read_occurring[file_name]:
            await asyncio.sleep(1)



    async def process_request(self):
        """
        Processes the request for getting all rooms within a timeframe

        :return: None
        """

        await self.__get_all_rooms_within_timeframe()


    async def __get_rooms_info(self, browser, result_url, start_time, end_time, date):
        """
        Gets the room information for all available rooms at the post-search result url

        :param browser: A browser instance
        :param result_url: The post-search result url where available rooms are displayed
        :param start_time: The  time of when the room becomes available
        :param end_time: The time of when the room becomes unavailable
        :param date: The date the room is available on
        :return: A list of dictionaries containing room information (identifiers; e.g., room number)
        """

        if self.__stop_event.is_set():
            return []

        if not result_url:
            return []

        page = await browser.new_page()

        try:
            await page.goto(result_url, timeout=15000)
        except PlaywrightTimeoutError:
            await page.close()
            return []

        await page.wait_for_selector('#s-lc-eq-search-results')

        no_result = await page.query_selector('#s-lc-eq-search-results > p')
        if no_result:
            return []

        # Getting all the rooms that fit criteria
        rooms = await page.query_selector_all('#s-lc-eq-search-results .media.s-lc-booking-suggestion')

        available_room_list = []

        # Gets the individual room information
        for room in rooms:
            room_item = {
                'url': '',
                'group': '',
                'room_num': '',
                'date': date,
                'start_time': start_time,
                'end_time': end_time
            }

            room_booking_url = await room.query_selector("h3 a")
            if room_booking_url:
                relative_url = await room_booking_url.get_attribute('href')
                if relative_url:
                    room_item['url'] = 'https://libcal.library.ubc.ca' + relative_url
                room_item['room_num'] = await room_booking_url.inner_text()

            booking_group = await room.query_selector('.s-lc-booking-group')
            if booking_group:
                room_item['group'] = await booking_group.inner_text()

            available_room_list.append(room_item)

        await page.close()

        return available_room_list


    async def __get_url_from_filter(self, browser, pre_search_url, start_time, end_time, date):
        """
        Gets the search result page url from filter criteria.

        :param browser: A browser instance
        :param pre_search_url: The url before any filter is applied and any room is displayed
        :param start_time: Start time to filter for, must be in 24-hour format ('HH:MM')
        :param end_time:  End time to filter for, must be in 24-hour format ('HH:MM')
        :param date:  Date to filter for, must be in YYYY-MM-DD format
        :return: The url after filter criteria are applied
        """

        if self.__stop_event.is_set():
            return ''

        page = await browser.new_page()

        try:
            await page.goto(pre_search_url, timeout=15000)
        except PlaywrightTimeoutError:
            await page.close()
            return ''

        await page.wait_for_selector('#s-lc-date')

        date_selector = '#s-lc-date'
        await page.fill(date_selector, date)

        start_id = '#s-lc-time-start'
        end_id = '#s-lc-time-end'
        await page.fill(start_id, start_time)
        await page.fill(end_id, end_time)

        button_path = '#s-lc-go'
        await page.click(button_path)

        # Waits until the new page is loaded to get the new url
        try:
            await page.wait_for_selector('#s-lc-eq-search-results', timeout=10000)
            search_result_url = page.url
        except PlaywrightTimeoutError:
            search_result_url = ''

        await page.close()

        return search_result_url


    async def __get_all_rooms(self, browser, start_time, end_time, date, pre_search_url_list):
        """
        Gets all the available rooms (with their identifying information) from all the study spaces
        between a start time, end time, and date.

        :param browser: A browser instance
        :param start_time: The start time of when the rooms are available, must be earlier than end time with
                            a gap of 1 hour between them
        :param end_time: The end time of when the rooms are not available, must be later than start time with
                            a gap of 1 hour between them
        :param date: The date of when the rooms are available
        :param pre_search_url_list: A list of all the study space search urls
        :return: A list of all the rooms, with identifying information, that fit the criteria
        """

        tasks = []

        # koerner end time must be earlier than 13:00
        # woodward end time must be earlier than 13:00
        # research commons end time must be earlier than 15:00
        for url in pre_search_url_list:
            if self.__stop_event.is_set():
                break

            end_time_input = end_time
            if 'koerner' in url and end_time > '14:00':
                end_time_input = '14:00'
            elif 'woodward' in url and end_time > '13:00':
                end_time_input = '13:00'
            elif 'research' in url and end_time > '15:00':
                end_time_input = '15:00'

            if start_time >= end_time_input:
                continue

            async with Webscrape.sem:
                post_search_url = await self.__get_url_from_filter(browser, url, start_time, end_time_input, date)
                task = asyncio.create_task(self.__get_rooms_info(browser, post_search_url, start_time, end_time_input, date))
                tasks.append(task)

        if self.__stop_event.is_set():
            for task in tasks:
                if not task.done():
                    task.cancel()
            return []

        rooms_list = await asyncio.gather(*tasks)

        rooms_list = [room for rooms_diff_space in rooms_list for room in rooms_diff_space]

        return rooms_list


    async def __get_all_rooms_within_timeframe(self):
        """
        Gets all the available rooms from all the study spaces within the timeframe specified by user

        :param user_start_time: The user specified start time, must be less than end time
        :param user_end_time: The user specified end time, must be greater than start time
        :param user_start_date: The user specified start date, must be less than end date
        :param user_end_date: The user specified end date, must be greater than start date
        :param file_name: The file name to save the room data results to
        :return: An empty list, used for debugging only
        """

        async with async_playwright() as p:
            browser = await p.chromium.launch()

            pre_search_url_list = ('https://libcal.library.ubc.ca/r/search/ikbstudy#s-lc-public-pt',
                                   'https://libcal.library.ubc.ca/r/search/maa#s-lc-public-pt',
                                   'https://libcal.library.ubc.ca/r/search/koerner_library#s-lc-public-pt',
                                   'https://libcal.library.ubc.ca/r/search/woodward_library#s-lc-public-pt',
                                   'https://libcal.library.ubc.ca/r/search/research_commons#s-lc-public-pt'
                                   )
            maximum_tasks = 5

            current_date = self.__start_date
            tasks = []
            all_rooms = []
            Webscrape.__save_to_csv([],True, self.__file_name)


            # Loops through hours in increments of 1 hour, then loops through days in timeframe
            while current_date <= self.__end_date:
                if self.__stop_event.is_set():
                    break

                current_start_time = self.__start_time

                while current_start_time < self.__end_time:
                    if self.__stop_event.is_set():
                        break

                    time_object = datetime.strptime(current_start_time, '%H:%M')
                    incremented_time_object = time_object + timedelta(hours=1)

                    current_end_time = incremented_time_object.strftime('%H:%M')

                    async with Webscrape.sem:
                        task = asyncio.create_task(self.__get_all_rooms(browser, current_start_time, current_end_time, current_date, pre_search_url_list))
                        tasks.append(task)

                    # Stores data into .csv in batches to prevent memory issues
                    if len(tasks) >= maximum_tasks:
                        if self.__stop_event.is_set():
                            for individualTask in tasks:
                                if not individualTask.done():
                                    individualTask.cancel()

                        all_rooms = await asyncio.gather(*tasks)
                        all_rooms = [room for rooms_diff_space in all_rooms for room in rooms_diff_space]
                        Webscrape.__save_to_csv(all_rooms, False, self.__file_name)
                        all_rooms = []
                        tasks = []

                    current_start_time = incremented_time_object.strftime('%H:%M')

                date_object = datetime.strptime(current_date, '%Y-%m-%d')
                incremented_date_object = date_object + timedelta(days=1)

                current_date = incremented_date_object.strftime('%Y-%m-%d')

            if not self.__stop_event.is_set():
                if tasks:
                    all_rooms = await asyncio.gather(*tasks)
                    all_rooms = [room for rooms_diff_space in all_rooms for room in rooms_diff_space]
                    Webscrape.__save_to_csv(all_rooms, False, self.__file_name)
                    all_rooms = []

            await browser.close()

            return all_rooms # DEBUG USE ONLY


    @staticmethod
    def __save_to_csv(data, first_time,filename='all_rooms.csv'):
        """
        Saves data to a .csv file

        :param data: The data to save
        :param first_time: Boolean for if it is the first time the method is called
        :param filename: The file name to save as
        :return: Nothing
        """

        if first_time:
            mode = 'w'
        else:
            mode = 'a'
        with open(filename, mode=mode, newline='') as data_file:
            writer = csv.writer(data_file)
            if data_file.tell() == 0:
                writer.writerow(['url','group','room_num','date','start_time','end_time'])
            for room in data:
                writer.writerow([room.get('url'),room.get('group'),room.get('room_num'),room.get('date'),room.get('start_time'),room.get('end_time')])


    def readDataFromCSV(self, building, start_date_obj, end_date_obj):
        """
        When building is empty string, then it will just filter for the time

        :param building: Building name. Must be one of IKB, MAA, Koerner, Woodward, Research
        :param start_date_obj: Datetime object that represents the start time
        :param end_date_obj: Datetime object that represents the end time
        :return: List of available rooms that fit the criteria, with a maximum of 100 returned rooms
        """

        rooms_list = []
        MAX_RETURNED_ROOMS = 100

        filename = 'rooms1.csv' if self.__file_name == 'rooms2.csv' else 'rooms2.csv'

        if not os.path.exists(filename):
            return []

        self.__file_read_occurring[filename] = True

        with open(filename, mode='r') as file:
            reader = csv.DictReader(file)

            for row in reader:
                if len(rooms_list) >= MAX_RETURNED_ROOMS:
                    break

                if not building:
                    room_date = datetime.strptime(row['date'], '%Y-%m-%d').date()
                    room_start = datetime.strptime(row['start_time'], '%H:%M').time()
                    room_end = datetime.strptime(row['end_time'], '%H:%M').time()

                    if start_date_obj.date() <= room_date <= end_date_obj.date():
                        if start_date_obj.time() <= room_start <= end_date_obj.time() and start_date_obj.time() <= room_end <= end_date_obj.time():
                            rooms_list.append(row)
                elif building.lower() in row['group'].lower():
                    room_date = datetime.strptime(row['date'], '%Y-%m-%d').date()
                    room_start = datetime.strptime(row['start_time'], '%H:%M').time()
                    room_end = datetime.strptime(row['end_time'], '%H:%M').time()

                    if start_date_obj.date() <= room_date <= end_date_obj.date():
                        if start_date_obj.time() <= room_start <= end_date_obj.time() and start_date_obj.time() <= room_end <= end_date_obj.time():
                            rooms_list.append(row)

        self.__file_read_occurring[filename] = False

        return copy.deepcopy(rooms_list)


    def stop_scraping(self):
        """
        Sets the flag to stop scraping

        :return: None
        """
        self.__stop_event.set()

    def check_complete(self):
        """
        Checks whether a webscrape operation has terminated

        :return: True if finished, else false
        """
        return self.__finished