import json
from enum import Enum
from ..datatype import datatype


class ResponseType(Enum):
    ACCEPT_SESSION = "ACCEPT_SESSION"
    GET_SESSIONS = "GET_SESSIONS"
    GET_ROOMS = "GET_ROOMS"
    LOGIN = "LOGIN"
    CREATE_USER = "CREATE_USER"
    JOIN_SESSION = "JOIN_SESSION"


class AcceptSessionResponse:
    def __init__(self, sessionAccepted: bool):
        self.responseType = ResponseType.ACCEPT_SESSION
        self.sessionAccepted = sessionAccepted
        
    def string(self):
        response = {
            'accepted': self.sessionAccepted
        }
        return json.dumps(response)


class GetRoomsResponse:
    def __init__(self, emptyRooms: list):
        self.responseType = ResponseType.GET_ROOMS
        self.emptyRooms = emptyRooms # List of dictionaries

    def string(self):
        return json.dumps(self.emptyRooms)


class GetSessionsResponse:
    def __init__(self, sessionList: list):
        self.responseType = ResponseType.GET_SESSIONS
        self.sessionList = sessionList

    def string(self):
        response = []
        for session in self.sessionList:
            start_time = session.startTime.strftime("%Y-%m-%d%H:%M")
            end_time = session.endTime.strftime("%Y-%m-%d%H:%M")
            session_dict = {
                'hostUser': session.host.username,
                'hostPass': session.host.password,
                'courseSubject': session.course.subject,
                'courseCode': session.course.courseCode,
                'courseYear': session.course.yearLevel,
                'building': session.room.building,
                'roomNum': session.room.room_number,
                'startTime': start_time,
                'endTime': end_time,
                'desc': session.description
            }
            response.append(session_dict)

        return json.dumps(response)


class LoginResponse:
    def __init__(self, validUser: bool, user: datatype.User):
        self.responseType = ResponseType.LOGIN
        self.validUser = validUser
        self.user = user

    def string(self):
        response = {
            'valid': self.validUser,
            'userUsername': self.user
        }
        return json.dumps(response)


class CreateNewUserResponse:
    def __init__(self, acceptedCreation: bool):
        self.responseType = ResponseType.CREATE_USER
        self.accepted = acceptedCreation

    def string(self):
        response = {
            'accepted': self.accepted
        }
        return json.dumps(response)


class JoinSessionResponse:
    def __init(self, processed: bool):
        self.responseType = ResponseType.JOIN_SESSION
        self.processed = processed

    def string(self):
        response = {
            'processed': self.processed
        }
        return json.dumps(response)