import asyncio
import threading
import tkinter

from .database import database
from .network import network, response

from datetime import datetime, timedelta
from .webscrape.async_webscrape import Webscrape


# adds a request to join a session
def joinSession(req):
    user_id = req.requester.username
    session = req.session
    if session not in database.session_requests:
        return response.JoinSessionResponse(False)
    if user_id not in database.user_credentials:
        return response.JoinSessionResponse(False)
    database.request_to_join(session, user_id)
    return response.JoinSessionResponse(True)


def createUser(req):
    user_id = req.userID
    password = req.password
    success = database.add_user(user_id, password)
    return response.CreateNewUserResponse(success)


# call all hosted sessions that fit the filter
def getSession(req):
    matching_sessions = []
    user_id = req.host.username
    building = req.building
    start_date = req.startDate
    end_date = req.endDate
    if req.course != "":
        subject = req.course.subject
    else:
        subject = ""

    if user_id != "":
        # return all sessions created by host
        matching_sessions = database.view_user_sessions(user_id)
        return response.GetSessionsResponse(matching_sessions)
    else:
        matching_sessions = database.session_requests.keys()

    if building != "":
        matching_sessions = [session for session in matching_sessions if session.room.building == building]
    if subject != "":
        matching_sessions = [session for session in matching_sessions if session.course.subject == subject]
    if start_date is not None:
        matching_sessions = [session for session in matching_sessions if session.startTime >= start_date]
    if end_date is not None:
        matching_sessions = [session for session in matching_sessions if session.endTime <= end_date]
    return response.GetSessionsResponse(matching_sessions)

def createSession(req):
    # does not need to respond, simply create session and add it to database when all conditions are met
    user_id = req.host.username
    study_session = req.session

    if user_id not in database.user_study_sessions:
        pass
    database.host_study_session(user_id, study_session)
    pass


# accept join request
def acceptSession(req):
    user_id = req.requester.username
    session = req.session
    acceptRequest = req.acceptRequest
    if session not in database.session_requests:
        return response.AcceptSessionResponse(False)
    if user_id not in database.user_credentials:
        return response.AcceptSessionResponse(False)

    if acceptRequest:
        database.accept_requests(session, user_id)
        return response.AcceptSessionResponse(True)
    else:
        database.user_credentials[session].remove(user_id)
        return response.AcceptSessionResponse(False)


# login request to verify user credentials
def loginRequest(req):
    user_id = req.userID
    password = req.password

    if user_id not in database.user_credentials:
        return response.LoginResponse(False, user_id)

    return response.LoginResponse(database.verify_password(user_id, password), user_id)


def saveServerData():
    database.save_data()


def loadServerData():
    database.load_data()


class MainWindow:
    def __init__(self):
        self.datetime = datetime

        self.connection = None
        self.loop = asyncio.new_event_loop()
        self.serverThread = None
        self.stopEvent = threading.Event()

        self.webscrape_obj = None

        self.window = tkinter.Tk()
        self.window.title("Server")
        self.window.protocol("WM_DELETE_WINDOW", self.onClosing)

        outputFrame = tkinter.Frame(self.window)
        outputFrame.grid(row=1, column=0)

        serverOutputLabel = tkinter.Label(outputFrame, text="Server information:", anchor="w", justify="left")
        serverOutputLabel.grid(row=0, column=0, sticky="w", padx=10)
        self.serverOutput = tkinter.Text(outputFrame, height=20, width=75)
        self.serverOutput.configure(font=("Consolas", 10), state=tkinter.DISABLED)
        self.serverOutput.grid(row=1, column=0, padx=10, pady=(2, 10))

        clientOutputLabel = tkinter.Label(outputFrame, text="Client connections:", anchor="w", justify="left")
        clientOutputLabel.grid(row=0, column=1, sticky="w", padx=10)
        self.clientOutput = tkinter.Text(outputFrame, height=20, width=75)
        self.clientOutput.configure(font=("Consolas", 10), state=tkinter.DISABLED)
        self.clientOutput.grid(row=1, column=1, padx=10, pady=(2, 10))

        clientOutputLabel = tkinter.Label(outputFrame, text="Errors:", anchor="w", justify="left")
        clientOutputLabel.grid(row=0, column=2, sticky="w", padx=10)
        self.errorOutput = tkinter.Text(outputFrame, height=20, width=75)
        self.errorOutput.configure(font=("Consolas", 10), state=tkinter.DISABLED)
        self.errorOutput.grid(row=1, column=2, padx=10, pady=(2, 10))

        buttonFrame = tkinter.Frame(self.window)
        buttonFrame.grid(row=0, column=0, pady=10)

        self.startServerBtn = tkinter.Button(buttonFrame, height=1, width=15, command=self.startServer)
        self.startServerBtn.configure(text="Start Server")
        self.startServerBtn.grid(row=0, column=0, padx=10, pady=(10, 5))

        self.endServerBtn = tkinter.Button(buttonFrame, height=1, width=15, command=self.endServer)
        self.endServerBtn.configure(text="End Server", state=tkinter.DISABLED)
        self.endServerBtn.grid(row=1, column=0, padx=10, pady=(5, 10))

        # start data scraping
        self.startScrape()

        # load server data
        loadServerData()

        self.window.mainloop()

    def getDt(self):
        return self.datetime.now().strftime("%d/%m/%Y %H:%M:%S")

    def activateLoop(self):
        asyncio.set_event_loop(self.loop)
        self.loop.run_until_complete(self.connection.connect(self.stopEvent))

    def startServer(self):
        if not self.serverThread:
            self.connection = network.Network(self.getRoom, createSession, getSession, acceptSession, loginRequest, createUser, joinSession,
                                              self.addServerOutput, self.addClientOutput, self.addErrorOutput)
            self.stopEvent.clear()
            self.serverThread = threading.Thread(target=self.activateLoop)
            self.serverThread.start()
            self.startServerBtn.configure(state=tkinter.DISABLED)
            self.endServerBtn.configure(state=tkinter.NORMAL)

    def endServer(self):
        if self.serverThread:
            self.stopEvent.set()
            while self.loop.is_running():
                print("STUCK123")
            self.serverThread.join()
            self.serverThread = None
            self.addServerOutput("Server closed")
            self.startServerBtn.configure(state=tkinter.NORMAL)
            self.endServerBtn.configure(state=tkinter.DISABLED)

    def addServerOutput(self, output: str):
        self.serverOutput.configure(state=tkinter.NORMAL)
        self.serverOutput.insert(tkinter.END, f"{self.getDt()}: {output}\n")
        self.serverOutput.configure(state=tkinter.DISABLED)

    def addClientOutput(self, output: str):
        self.clientOutput.configure(state=tkinter.NORMAL)
        self.clientOutput.insert(tkinter.END, f"{self.getDt()}: {output}\n")
        self.clientOutput.configure(state=tkinter.DISABLED)

    def addErrorOutput(self, output: str):
        self.errorOutput.configure(state=tkinter.NORMAL)
        self.errorOutput.insert(tkinter.END, f"{self.getDt()}: {output}\n")
        self.errorOutput.configure(state=tkinter.DISABLED)

    def onClosing(self):
        self.endServer()
        saveServerData()
        self.window.destroy()

    def getRoom(self, req):
        emptyRoomsList = self.webscrape_obj.readDataFromCSV(req.building, req.startDate, req.endDate)
        res = response.GetRoomsResponse(emptyRoomsList)
        return res

    def startScrape(self):
        current_datetime = datetime.today()
        end_datetime = current_datetime + timedelta(days=7)

        current_date_formatted = current_datetime.strftime('%Y-%m-%d')
        end_date_formatted = end_datetime.strftime('%Y-%m-%d')

        self.webscrape_obj = Webscrape('08:00', '20:00', current_date_formatted, end_date_formatted)

        loop_thread = threading.Thread(target=self.run_scrape_loop)
        loop_thread.start()

    # continuous_scrape will break out of forever loop when endScrape is called
    def run_scrape_loop(self):
        loop = asyncio.new_event_loop()
        loop.create_task(self.webscrape_obj.continuous_scrape(loop))
        loop.run_forever()

    def endScrape(self):
        if self.webscrape_obj:
            self.webscrape_obj.stop_scraping()

    def checkScrapeStatus(self):
        if self.webscrape_obj:
            return self.webscrape_obj.check_complete()
