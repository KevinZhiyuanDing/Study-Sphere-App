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

