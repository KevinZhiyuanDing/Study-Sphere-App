# Our Architecture Design:
## Defining Components:
1. Models
    - Study Room Model
        - Responsibilities: Stores data of study room availability, includes information on location, capacity, booking status, capacity, and study sessions. Provides methods to retrieve and filter study rooms based on selected time frames.
        - Communicates with: Study Room Controller, sending room data based on user criteria and sending users directly to UBC’s booking websites.
        - Server-side model
    - User Model
        - Responsibilities: Manages the user’s data, such as hosting public or private sessions. Can track which courses the user is interested in. 
        - Communicates with: User Controller to join or host study sessions
        - Server-side model
    - Booking Model
        - Responsibilities: Stores booking information, such as time, location, room, and host 
        - Communicates with: Booking Controller for creating, modifying, and retrieving booking data. 
        - Server-side model
    - Study Session Model
        - Responsibilities: Stores study session information, such as host, course, public or private, and participant request.
        - Communicates with: Study Session Controller for adding and removing participants.
1. Controllers
    - Study Room Model
        - Responsibilities: Handles logic for getting available study rooms, manages filters, and redirects users to UBC’s study room booking site.
        - Communicates with: Study Room Model
        - Server-side controller
    - User Controller
        - Responsibilities: Keeps track of users posting private or public study sessions.
        - Communicates with: User Model
        - Server-side controller
    - Booking Controller
        - Responsibilities: Manages study room bookings, including keeping track of what rooms are booked, creating public/private study sessions.
        - Communicates with: Booking Model
        - Server-side controller
    - Study Sessions Controller
        - Responsibilities: Manages study session information, allows participants to request joining sessions.
        - Communicates with: Study Session Model
        - Server-side controller
1. Webpage View
    - Homepage View
        - Displays an interactive calendar that allows users to pick the date and time to filter for study rooms.
        - Communicates with study room controller to fetch and display available rooms
        - Client-sided
    - Study Room List View
        - Shows the filtered list of available study rooms with a booking option
        - Communicates with Study Room Controller for room data and Booking Controller for booking actions
        - Client-sided
    - Study Session View
        - Shows a filtered list of study sessions on the user’s preferred courses with a joining option. 
        - Communicates with Study Sessions Controller for session data and Booking Controller for booking actions
        - Client-sided

## Organizing Architecture:
1. Client-Server Architecture:
    - Client: The view component is the frontend component designed to be seen by the client. It provides user interactions to obtain data from the server.
    - Server: The backend contains the model and controller component. It manages all the data for the study rooms, study sessions, redirects users to booking websites, and scraps UBC websites for the data.
1. Data Flow:
    - The client requests specific data from the server. The server processes these requests via the appropriate controllers, modifies the data and returns it to the client. The client’s view is updated based on the data received.
1. APIs:
    - Web scrapers will be used to pull data from UBC’s library website and from other study room booking websites such as SUS. This data will be stored to keep track of which study rooms are available. 
    - Implement filtering and sorting mechanics. 