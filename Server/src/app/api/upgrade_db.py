import sqlite3
import os

db_path = os.path.join(os.path.dirname(__file__), '../../study_sphere.db')
conn = sqlite3.connect(db_path)
c = conn.cursor()

# Add new columns for MVP features
try:
    c.execute('ALTER TABLE sessions ADD COLUMN type TEXT DEFAULT "public"')
except sqlite3.OperationalError:
    pass
try:
    c.execute('ALTER TABLE sessions ADD COLUMN host_email TEXT')
except sqlite3.OperationalError:
    pass
try:
    c.execute('ALTER TABLE users ADD COLUMN email TEXT')
except sqlite3.OperationalError:
    pass
try:
    c.execute('ALTER TABLE requests ADD COLUMN description TEXT')
except sqlite3.OperationalError:
    pass
try:
    c.execute('ALTER TABLE requests ADD COLUMN name TEXT')
except sqlite3.OperationalError:
    pass
try:
    c.execute('ALTER TABLE requests ADD COLUMN email TEXT')
except sqlite3.OperationalError:
    pass
try:
    c.execute('ALTER TABLE requests ADD COLUMN year_level TEXT')
except sqlite3.OperationalError:
    pass

conn.commit()
conn.close()
