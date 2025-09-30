import unittest
import datetime
from app.datatype.datatype import Course, Session, User, Room


class TestDataTypes(unittest.TestCase):
    def test_course_initialization(self):
        course = Course(subject="Math", courseCode="MATH101", yearLevel=1)
        self.assertEqual(course.subject, "Math")
        self.assertEqual(course.courseCode, "MATH101")
        self.assertEqual(course.yearLevel, 1)

    def test_user_initialization(self):
        user = User(username="john_doe", password="securepassword")
        self.assertEqual(user.username, "john_doe")
        self.assertEqual(user.password, "securepassword")
        self.assertEqual(user.userPendingSessions, [])
        self.assertEqual(user.userCreatedSessions, [])
        self.assertEqual(user.userJoinedSessions, [])

    def test_room_initialization(self):
        start_time = datetime.datetime(2024, 1, 1, 10, 0)
        end_time = datetime.datetime(2024, 1, 1, 12, 0)
        room = Room(building="Engineering", roomNumber="101", startTime=start_time, endTime=end_time)
        self.assertEqual(room.building, "Engineering")
        self.assertEqual(room.room_number, "101")
        self.assertEqual(room.start_time, start_time)
        self.assertEqual(room.end_time, end_time)

    def test_session_initialization(self):
        host = User(username="jane_doe", password="securepassword")
        course = Course(subject="CS", courseCode="CS101", yearLevel=2)
        start_time = datetime.datetime(2024, 1, 1, 10, 0)
        end_time = datetime.datetime(2024, 1, 1, 12, 0)
        room = Room(building="Library", roomNumber="202", startTime=start_time, endTime=end_time)
        session = Session(host=host, course=course, room=room, startTime=start_time, endTime=end_time,
                          description="Study session")

        self.assertEqual(session.host, host)
        self.assertEqual(session.course, course)
        self.assertEqual(session.room, room)
        self.assertEqual(session.startTime, start_time)
        self.assertEqual(session.endTime, end_time)
        self.assertEqual(session.description, "Study session")
        self.assertEqual(session.requesters, [])

    def test_session_requesters(self):
        host = User(username="jane_doe", password="securepassword")
        requester = User(username="john_doe", password="securepassword")
        course = Course(subject="CS", courseCode="CS101", yearLevel=2)
        start_time = datetime.datetime(2024, 1, 1, 10, 0)
        end_time = datetime.datetime(2024, 1, 1, 12, 0)
        room = Room(building="Library", roomNumber="202", startTime=start_time, endTime=end_time)
        session = Session(host=host, course=course, room=room, startTime=start_time, endTime=end_time,
                          description="Study session")

        session.requesters.append(requester)
        self.assertIn(requester, session.requesters)




