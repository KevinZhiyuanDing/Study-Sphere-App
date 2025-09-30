import unittest
import json
from datetime import datetime
from app.network.request import (
    RequestType,
    getRequestType,
    AcceptSessionRequest,
    GetRoomsRequest,
    GetSessionsRequest,
    CreateSessionRequest,
    LoginRequest,
    CreateNewUserRequest,
    JoinSessionRequest,
)
from app.datatype.datatype import User, Course, Room, Session


class TestRequest(unittest.TestCase):
    def test_get_request_type(self):
        message = json.dumps({"requestType": "CREATE_SESSION"})
        request_type = getRequestType(message)
        self.assertEqual(request_type, RequestType.CREATE_SESSION)

        message = json.dumps({"requestType": "GET_ROOMS"})
        request_type = getRequestType(message)
        self.assertEqual(request_type, RequestType.GET_ROOMS)

        with self.assertRaises(Exception):
            getRequestType(json.dumps({"requestType": "INVALID_TYPE"}))

    def test_login_request(self):
        message = json.dumps({
            "username": "test_user",
            "password": "securepassword"
        })
        request = LoginRequest(message)

        self.assertEqual(request.userID, "test_user")
        self.assertEqual(request.password, "securepassword")

    def test_create_new_user_request(self):
        message = json.dumps({
            "username": "new_user",
            "password": "newpassword"
        })
        request = CreateNewUserRequest(message)

        self.assertEqual(request.userID, "new_user")
        self.assertEqual(request.password, "newpassword")

    def test_get_sessions_request(self):
        message = json.dumps({
            "hostUser": "host_user",
            "course": "",
            "building": "Engineering",
            "startDate": "",
            "endDate": ""
        })
        request = GetSessionsRequest(message)

        self.assertEqual(request.host.username, "host_user")
        self.assertEqual(request.course, "")
        self.assertEqual(request.building, "Engineering")
        self.assertIsNone(request.startDate)
        self.assertIsNone(request.endDate)

    def test_accept_session_request(self):
        message = json.dumps({
            "hostUser": "host_user",
            "hostPass": "host_password",
            "requestUser": "request_user",
            "requestPass": "request_password",
            "startDate": "2024-01-0110:00",
            "endDate": "2024-01-0112:00",
            "courseSubject": "Math",
            "courseCode": "MATH101",
            "courseYear": 1,
            "building": "Engineering",
            "roomNum": "101",
            "sessionDesc": "Study session description",
            "accept": True
        })
        request = AcceptSessionRequest(message)

        self.assertEqual(request.host.username, "host_user")
        self.assertEqual(request.requester.username, "request_user")
        self.assertEqual(request.session.course.subject, "Math")
        self.assertTrue(request.acceptRequest)
    def test_get_rooms_request(self):
        message = json.dumps({
            "building": "Engineering",
            "startDate": "2024-01-0110:00",
            "endDate": "2024-01-0112:00"
        })
        request = GetRoomsRequest(message)

        self.assertEqual(request.building, "Engineering")
        self.assertEqual(request.startDate, datetime.strptime("2024-01-0110:00", "%Y-%m-%d%H:%M"))
        self.assertEqual(request.endDate, datetime.strptime("2024-01-0112:00", "%Y-%m-%d%H:%M"))

    def test_create_session_request(self):
        message = json.dumps({
            "hostUser": "host_user",
            "courseSubject": "Math",
            "courseCode": "MATH101",
            "courseYear": 1,
            "building": "Engineering",
            "roomNum": "101",
            "startDate": "2024-01-0110:00",
            "endDate": "2024-01-0112:00",
            "sessionDesc": "Study session description"
        })
        request = CreateSessionRequest(message)

        self.assertEqual(request.host.username, "host_user")
        self.assertEqual(request.session.course.subject, "Math")
        self.assertEqual(request.session.course.courseCode, "MATH101")
        self.assertEqual(request.session.course.yearLevel, 1)
        self.assertEqual(request.session.room.building, "Engineering")
        self.assertEqual(request.session.room.room_number, "101")
        self.assertEqual(request.session.room.start_time, datetime.strptime("2024-01-0110:00", "%Y-%m-%d%H:%M"))
        self.assertEqual(request.session.room.end_time, datetime.strptime("2024-01-0112:00", "%Y-%m-%d%H:%M"))
        self.assertEqual(request.session.description, "Study session description")

    def test_join_session_request(self):
        message = json.dumps({
            "requestUser": "request_user",
            "requestPass": "request_password",
            "hostUser": "host_user",
            "hostPass": "host_password",
            "courseSubject": "Math",
            "courseCode": "MATH101",
            "courseYear": 1,
            "building": "Engineering",
            "roomNum": "101",
            "startDate": "2024-01-0110:00",
            "endDate": "2024-01-0112:00",
            "sessionDesc": "Study session description"
        })
        request = JoinSessionRequest(message)

        self.assertEqual(request.requester.username, "request_user")
        self.assertEqual(request.requester.password, "request_password")
        self.assertEqual(request.session.host.username, "host_user")
        self.assertEqual(request.session.host.password, "host_password")
        self.assertEqual(request.session.course.subject, "Math")
        self.assertEqual(request.session.course.courseCode, "MATH101")
        self.assertEqual(request.session.course.yearLevel, 1)
        self.assertEqual(request.session.room.building, "Engineering")
        self.assertEqual(request.session.room.room_number, "101")
        self.assertEqual(request.session.room.start_time, datetime.strptime("2024-01-0110:00", "%Y-%m-%d%H:%M"))
        self.assertEqual(request.session.room.end_time, datetime.strptime("2024-01-0112:00", "%Y-%m-%d%H:%M"))
        self.assertEqual(request.session.description, "Study session description")
