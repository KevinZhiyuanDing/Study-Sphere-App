import json
from datetime import datetime
from enum import Enum
from ..datatype import datatype


class RequestType(Enum):
    CREATE_SESSION = "CREATE_SESSION"
    GET_SESSIONS = "GET_SESSIONS"
    GET_ROOMS = "GET_ROOMS"
    ACCEPT_REQUEST = "ACCEPT_REQUEST"
    LOGIN = "USER_LOGIN"
    CREATE_USER = "CREATE_USER"
    JOIN_SESSION = "JOIN_SESSION"


def getRequestType(message: str):
    data = json.loads(message)
    requestType = data['requestType']
    if not requestType:
        raise Exception("Invalid request received from client")
    if requestType == RequestType.GET_ROOMS.value:
        return RequestType.GET_ROOMS
    elif requestType == RequestType.CREATE_SESSION.value:
        return RequestType.CREATE_SESSION
    elif requestType == RequestType.GET_SESSIONS.value:
        return RequestType.GET_SESSIONS
    elif requestType == RequestType.ACCEPT_REQUEST.value:
        return RequestType.ACCEPT_REQUEST
    elif requestType == RequestType.LOGIN.value:
        return RequestType.LOGIN
    elif requestType == RequestType.CREATE_USER.value:
        return RequestType.CREATE_USER
    elif requestType == RequestType.JOIN_SESSION.value:
        return RequestType.JOIN_SESSION
    else:
        raise Exception("Invalid request received from client")


class AcceptSessionRequest:
    def __init__(self, message: str):
        data = json.loads(message)
        self.host = datatype.User(data['hostUser'], data['hostPass'])
        self.requester = datatype.User(data['requestUser'], data['requestPass'])

        start_date = datetime.strptime(data['startDate'], "%Y-%m-%d%H:%M")
        end_date = datetime.strptime(data['endDate'], "%Y-%m-%d%H:%M")

        course_obj = datatype.Course(data['courseSubject'], data['courseCode'], data['courseYear'])
        room_obj = datatype.Room(data['building'], data['roomNum'], start_date, end_date)

        self.session = datatype.Session(self.host, course_obj, room_obj, start_date, end_date, data['sessionDesc'])
        self.acceptRequest = data['accept']

        
class GetRoomsRequest:
    def __init__(self, message: str):
        data = json.loads(message)

        self.building = data['building']

        start_date = data['startDate']
        end_date = data['endDate']

        self.startDate = datetime.strptime(start_date, "%Y-%m-%d%H:%M")
        self.endDate = datetime.strptime(end_date, "%Y-%m-%d%H:%M")


class GetSessionsRequest:
    def __init__(self, message: str):
        data = json.loads(message)

        self.host = datatype.User(data['hostUser'], "")

        self.course = data['course']

        start_date = data['startDate']
        end_date = data['endDate']

        if start_date != "":
            self.startDate = datetime.strptime(start_date, "%Y-%m-%d%H:%M")
        else:
            self.startDate = None
        if end_date != "":
            self.endDate = datetime.strptime(end_date, "%Y-%m-%d%H:%M")
        else:
            self.endDate = None

        self.building = data['building']


class CreateSessionRequest:
    def __init__(self, message: str):
        data = json.loads(message)

        self.host = datatype.User(data['hostUser'], "")
        course = datatype.Course(data['courseSubject'], data['courseCode'], data['courseYear'])

        start_date = datetime.strptime(data['startDate'], "%Y-%m-%d%H:%M")
        end_date = datetime.strptime(data['endDate'], "%Y-%m-%d%H:%M")
        room = datatype.Room(data['building'], data['roomNum'], start_date, end_date)

        self.session = datatype.Session(self.host, course, room, start_date, end_date, data['sessionDesc'])


class LoginRequest:
    def __init__(self, message: str):
        data = json.loads(message)
        self.userID = data['username']
        self.password = data['password']


class CreateNewUserRequest:
    def __init__(self, message: str):
        data = json.loads(message)
        self.userID = data['username']
        self.password = data['password']

class JoinSessionRequest:
    def __init__(self, message: str):
        data = json.loads(message)

        self.requester = datatype.User(data['requestUser'], data['requestPass'])

        host = datatype.User(data['hostUser'], data['hostPass'])
        course = datatype.Course(data['courseSubject'], data['courseCode'], data['courseYear'])

        start_date = datetime.strptime(data['startDate'], "%Y-%m-%d%H:%M")
        end_date = datetime.strptime(data['endDate'], "%Y-%m-%d%H:%M")
        room = datatype.Room(data['building'], data['roomNum'], start_date, end_date)

        self.session = datatype.Session(host, course, room, start_date, end_date, data['sessionDesc'])
