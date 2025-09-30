import datetime

from ..datatype import datatype


class Course:
    def __init__(self, subject, courseCode, yearLevel):
        self.subject = subject  # str
        self.courseCode = courseCode  # str
        self.yearLevel = yearLevel  # int


class Session:
    def __init__(self, host, course, room, startTime, endTime, description):
        self.host = host  # User
        self.course = course  # Course
        self.room = room  # Room
        self.startTime = startTime  # datetime.datetime
        self.endTime = endTime  # datetime.datetime
        self.description = description  # str
        self.requesters = []  # list of Users that request to join the study session


class User:
    def __init__(self, username, password):
        self.username = username  # str
        self.password = password  # str
        self.userPendingSessions = []  # pending studySessions that user attempted to create
        self.userCreatedSessions = []  # studySessions created by the user
        self.userJoinedSessions = []  # studySessions that the user is in


class Room:
    def __init__(self, building, roomNumber, startTime, endTime):
        self.building = building  # str
        self.room_number = roomNumber  # str
        self.start_time = startTime  # datetime.datetime
        self.end_time = endTime  # datetime.datetime

