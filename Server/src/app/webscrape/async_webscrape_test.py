import unittest
import asyncio
from async_webscrape import *
from playwright.async_api import async_playwright
import threading
import datetime


class MyTestCase(unittest.IsolatedAsyncioTestCase):
    @unittest.skip
    async def test_get_rooms_info(self):
        url_example = 'https://libcal.library.ubc.ca/r/search/ikbstudy?m=t&gid=0&capacity=3&date=2024-11-23&date-end=2024-11-18&start=11%3A00&end=12%3A00'

        async with async_playwright() as p:
            browser = await p.chromium.launch()

            rooms = await get_rooms_info(browser, url_example)

            for room in rooms:
                print("Url: " + room.get('url'))
                print("Group: " + room.get('group'))
                print("Room: " + room.get('room_num'))
                print("Date: " + room.get('date'))
                print("Start time: " + room.get('start_time'))
                print("End time: " + room.get('end_time'))
                print('')

            await browser.close()

    @unittest.skip
    async def test_get_rooms_info_no_matches(self):
        url_example = 'https://libcal.library.ubc.ca/r/search/ikbstudy?m=t&gid=0&capacity=3&date=2024-11-24&date-end=2024-11-19&start=17%3A00&end=20%3A00'

        async with async_playwright() as p:
            browser = await p.chromium.launch()

            rooms = await get_rooms_info(browser, url_example)

            for room in rooms:
                print("Url: " + room.get('url'))
                print("Group: " + room.get('group'))
                print("Room: " + room.get('room_num'))
                print("Date: " + room.get('date'))
                print("Start time: " + room.get('start_time'))
                print("End time: " + room.get('end_time'))
                print('')

            await browser.close()

    @unittest.skip
    async def test_get_url_from_filter(self):
        test_url = 'https://libcal.library.ubc.ca/r/search/ikbstudy#s-lc-public-pt'
        test_date = '2024-11-23'
        test_start = '14:00'
        test_end = '16:00'

        returned_url = await get_url_from_filter(test_url, test_start, test_end, test_date)

        print(returned_url)

    @unittest.skip
    async def test_get_all_rooms(self):
        test_start = '14:00'
        test_end = '16:00'
        test_date = '2024-11-23'
        rooms_list_output = await get_all_rooms(test_start, test_end, test_date)

        for room in rooms_list_output:
            print(room.get('url'))
            print(room.get('group'))
            print(room.get('endtime'))
            print('')

    @unittest.skip
    async def test_get_all_rooms_within_timeframe(self):
        test_start = '08:00'
        test_end = '16:00'
        test_start_date = '2024-11-23'
        test_end_date = '2024-11-28'

        all_rooms_output = await get_all_rooms_within_timeframe(test_start, test_end, test_start_date, test_end_date)

        print(len(all_rooms_output))

    @unittest.skip
    async def test_encapsulation(self):
        test_start = '14:00'
        test_end = '16:00'
        test_start_date = '2024-11-28'
        test_end_date = '2024-11-28'

        webscrape_obj = Webscrape(test_start, test_end, test_start_date, test_end_date, "rooms.csv")

        await webscrape_obj.process_request()

    @unittest.skip
    async def test_stop(self):
        test_start = '14:00'
        test_end = '16:00'
        test_start_date = '2024-11-28'
        test_end_date = '2024-12-03'

        webscrape_obj = Webscrape(test_start, test_end, test_start_date, test_end_date, "rooms1.csv")

        print("Object created")

        scraping_task = asyncio.create_task(webscrape_obj.process_request())

        await asyncio.sleep(10)

        webscrape_obj.stop_scraping()
        print("Scraping stopped")

        print(webscrape_obj.check_complete())
        await scraping_task
        print(webscrape_obj.check_complete())
        self.assertTrue(scraping_task.done(), "Scraping task didn't finish")


    async def test_continuous_scrape(self):
        test_start = '14:00'
        test_end = '16:00'
        test_start_date = '2024-11-28'
        test_end_date = '2024-11-30'

        start_date_obj = datetime.datetime(2024, 11, 28, hour=14)
        end_date_obj = datetime.datetime(2024, 11, 30, hour=16)

        webscrape_obj = Webscrape(test_start, test_end, test_start_date, test_end_date)

        scraping_task = asyncio.create_task(webscrape_obj.continuous_scrape())

        await asyncio.sleep(60)
        print("Read initiated")

        rooms_list = webscrape_obj.readDataFromCSV("",start_date_obj,end_date_obj)

        webscrape_obj.stop_scraping()
        print("Stop scraping initiated")


        await scraping_task
        print("Scraping fully stopped")

        print(len(rooms_list))


if __name__ == '__main__':
    unittest.main()
