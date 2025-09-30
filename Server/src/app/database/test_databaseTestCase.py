#automated test cases for database
import unittest
from app.database import StudySphere

class TestStudySphere(unittest.TestCase):
    def setUp(self):
        # Create a fresh instance of StudySphere for testing
        self.database = StudySphere()

    def test_add_user(self):
        result = self.database.add_user("user1", "securepassword")
        self.assertEqual(result, True)
        self.assertIn("user1", self.database.user_credentials)

    def test_duplicate_user(self):
        self.database.add_user("user1", "securepassword")
        result = self.database.add_user("user1", "securepassword")
        self.assertEqual(result, False)

    def test_verify_password(self):
        self.database.add_user("user1", "securepassword")
        self.assertTrue(self.database.verify_password("user1", "securepassword"))
        self.assertFalse(self.database.verify_password("user1", "wrongpassword"))
        self.assertFalse(self.database.verify_password("nonexistent", "password"))

    def test_host_study_session(self):
        self.database.add_user("user1", "securepassword")
        session = "Math101Session"
        result = self.database.host_study_session("user1", session)
        self.assertEqual(result, "Study session successfully created by user user1")
        self.assertIn(session, self.database.user_study_sessions["user1"])
        self.assertIn(session, self.database.session_requests)
        self.assertIn(session, self.database.accepted_requests)

    def test_request_to_join(self):
        self.database.add_user("user1", "securepassword")
        self.database.add_user("user2", "securepassword")
        session = "Math101Session"
        self.database.host_study_session("user1", session)

        result = self.database.request_to_join(session, "user2")
        self.assertEqual(result, "User user2 successfully requested to join session")
        self.assertIn("user2", self.database.session_requests[session])

    def test_accept_request(self):
        self.database.add_user("user1", "securepassword")
        self.database.add_user("user2", "securepassword")
        session = "Math101Session"
        self.database.host_study_session("user1", session)
        self.database.request_to_join(session, "user2")

        result = self.database.accept_request(session, "user2")
        self.assertEqual(result, "User user2 successfully joined session")
        self.assertIn("user2", self.database.accepted_requests[session])

    def test_save_and_load_data(self):
        self.database.add_user("user1", "securepassword")
        self.database.save_data("test_data.pkl")

        # Load into a new instance
        new_database = StudySphere()
        new_database.load_data("test_data.pkl")
        self.assertIn("user1", new_database.user_credentials)

