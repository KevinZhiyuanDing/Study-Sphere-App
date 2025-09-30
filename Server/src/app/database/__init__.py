import pickle
import hashlib
import os



class StudySphere:
    def __init__(self):
        # User data
        self.user_credentials = {}  # Maps user_id to (salt, hashed_password)
        self.user_study_sessions = {}  # Maps user_id to list of Session objects

        # Session data
        self.session_requests = {}  # Maps Session objects to list of user_ids requesting to join
        self.accepted_requests = {}  # Maps Session objects to list of user_ids who joined successfully

    # Hash password with salt
    @staticmethod
    def hash_password(password, salt=None):
        if salt is None:
            salt = os.urandom(16)
        # Hash password with SHA-256
        hashed_password = hashlib.pbkdf2_hmac(
            "sha256",
            password.encode('utf-8'), #converts password to bytes
            salt,
            100000)
        return salt, hashed_password

    # Verify password
    def verify_password(self, user_id, password):
        if user_id not in self.user_credentials:
            return False
        salt, hashed_password = self.user_credentials[user_id]
        _, new_hashed_password = self.hash_password(password, salt)
        return hashed_password == new_hashed_password

    # Save data to a pickle file
    def save_data(self, filename="study_sphere_data.pkl"):
        with open(filename, "wb") as file:
            pickle.dump({
                "user_credentials": self.user_credentials,
                "user_study_sessions": self.user_study_sessions,
                "session_requests": self.session_requests,
                "accepted_requests": self.accepted_requests,
            }, file)
        return f"Data saved to {filename}."

    # Load data from a pickle file
    def load_data(self, filename="study_sphere_data.pkl"):
        try:
            with open(filename, "rb") as file:
                data = pickle.load(file)
                self.user_credentials = data.get("user_credentials", {})
                self.user_study_sessions = data.get("user_study_sessions", {})
                self.session_requests = data.get("session_requests", {})
                self.accepted_requests = data.get("accepted_requests", {})
            return f"Data loaded from {filename}."
        except FileNotFoundError:
            return f"No data file found at {filename}. Starting with empty data."

    # Register a new user
    def add_user(self, user_id, password):
        if user_id in self.user_credentials:
            return False

        salt, hash_password = self.hash_password(password)
        self.user_credentials[user_id] = (salt, hash_password)
        self.user_study_sessions[user_id] = []  # Initialize an empty list for sessions
        return True

    # Host a new study session
    def host_study_session(self, user_id, session):
        if user_id not in self.user_credentials:
            return f"Host {user_id} is not registered"

        # Generate a unique session ID
        # Add session to the host's list of sessions
        self.user_study_sessions[user_id].append(session)

        # Initialize join and accepted request lists
        self.session_requests[session] = []
        self.accepted_requests[session] = []

        return f"Study session successfully created by user {user_id}"

    # User requests to join a session
    def request_to_join(self, session, user_id):
        if session not in self.session_requests:
            return f"Session does not exist"
        if user_id not in self.user_credentials:
            return f"User {user_id} is not registered"

        self.session_requests[session].append(user_id)
        return f"User {user_id} successfully requested to join session"

    # Host accepts a join request
    def accept_request(self, session, user_id):
        if session not in self.session_requests:
            return f"Session does not exist"
        if user_id not in self.session_requests[session]:
            return f"User {user_id} has not requested to join session"

        self.accepted_requests[session].append(user_id)
        return f"User {user_id} successfully joined session"

    # View all sessions hosted by a user
    def view_user_sessions(self, user_id):
        if user_id not in self.user_study_sessions:
            return f"User {user_id} does not exist"
        return self.user_study_sessions[user_id]

    # View details of a specific session
    def view_session(self, session):
        if session not in self.session_requests:
            return f"Session does not exist"
        return session




# Create a global instance of StudySphere
database = StudySphere()
