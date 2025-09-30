from flask import Flask, request, jsonify
import sqlite3
import hashlib
import os

app = Flask(__name__)
DATABASE = os.path.join(os.path.dirname(__file__), '../../study_sphere.db')

def get_db():
    conn = sqlite3.connect(DATABASE)
    conn.row_factory = sqlite3.Row
    return conn

# User registration
@app.route('/register', methods=['POST'])
def register():
    data = request.json
    username = data.get('username')
    password = data.get('password')
    if not username or not password:
        return jsonify({'error': 'Missing username or password'}), 400
    salt = os.urandom(16)
    hashed = hashlib.pbkdf2_hmac('sha256', password.encode('utf-8'), salt, 100000)
    conn = get_db()
    try:
        conn.execute('INSERT INTO users (username, salt, password) VALUES (?, ?, ?)', (username, salt, hashed))
        conn.commit()
    except sqlite3.IntegrityError:
        return jsonify({'error': 'Username already exists'}), 409
    return jsonify({'success': True})

# User login
@app.route('/login', methods=['POST'])
def login():
    data = request.json
    username = data.get('username')
    password = data.get('password')
    conn = get_db()
    user = conn.execute('SELECT * FROM users WHERE username = ?', (username,)).fetchone()
    if not user:
        return jsonify({'valid': False}), 401
    salt = user['salt']
    hashed = hashlib.pbkdf2_hmac('sha256', password.encode('utf-8'), salt, 100000)
    if hashed == user['password']:
        return jsonify({'valid': True, 'username': username})
    else:
        return jsonify({'valid': False}), 401

@app.route('/sessions', methods=['POST'])
def post_session():
    data = request.json
    host = data.get('host')
    course = data.get('course')
    room = data.get('room')
    start_time = data.get('start_time')
    end_time = data.get('end_time')
    description = data.get('description')
    session_type = data.get('type', 'public')
    host_email = data.get('host_email')
    conn = get_db()
    conn.execute('INSERT INTO sessions (host, course, room, start_time, end_time, description, type, host_email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)',
                 (host, course, room, start_time, end_time, description, session_type, host_email))
    conn.commit()
    return jsonify({'success': True})

@app.route('/sessions/<int:session_id>/request', methods=['POST'])
def request_join(session_id):
    data = request.json
    requester = data.get('requester')
    description = data.get('description', '')
    name = data.get('name', '')
    email = data.get('email', '')
    year_level = data.get('year_level', '')
    conn = get_db()
    conn.execute('INSERT INTO requests (session_id, requester, description, name, email, year_level) VALUES (?, ?, ?, ?, ?, ?)',
                 (session_id, requester, description, name, email, year_level))
    conn.commit()
    return jsonify({'success': True})

@app.route('/sessions/<int:session_id>/accept', methods=['POST'])
def accept_request(session_id):
    data = request.json
    requester = data.get('requester')
    conn = get_db()
    conn.execute('UPDATE requests SET accepted = 1 WHERE session_id = ? AND requester = ?', (session_id, requester))
    conn.commit()
    return jsonify({'success': True})
# User profile management
@app.route('/users/<username>/profile', methods=['GET', 'POST'])
def user_profile(username):
    conn = get_db()
    if request.method == 'GET':
        user = conn.execute('SELECT * FROM users WHERE username = ?', (username,)).fetchone()
        if not user:
            return jsonify({'error': 'User not found'}), 404
        return jsonify({'username': user['username'], 'email': user.get('email', '')})
    else:
        data = request.json
        email = data.get('email')
        conn.execute('UPDATE users SET email = ? WHERE username = ?', (email, username))
        conn.commit()
        return jsonify({'success': True})

# Get sessions for a user
@app.route('/users/<username>/sessions', methods=['GET'])
def get_user_sessions(username):
    conn = get_db()
    sessions = conn.execute('SELECT * FROM sessions WHERE host = ?', (username,)).fetchall()
    return jsonify([dict(row) for row in sessions])

# Get join requests for a session
@app.route('/sessions/<int:session_id>/requests', methods=['GET'])
def get_session_requests(session_id):
    conn = get_db()
    requests = conn.execute('SELECT * FROM requests WHERE session_id = ?', (session_id,)).fetchall()
    return jsonify([dict(row) for row in requests])

if __name__ == '__main__':
    app.run(debug=True)
