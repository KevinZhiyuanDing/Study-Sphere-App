# Team Aragorn - UBC Study Sphere
## Problem: 
UBC students struggle finding information about available study room locations because of the restrictive filtering system in place currently on the booking site. 
Additionally, it can be hard to find study buddies who want to study with you. The current UBC website for booking study rooms only lets you search one space at a time.
Additionally, there are numerous study spaces scattered across campus that are not documented on the main website.  

## Solution:
To address these issues, we aim to develop UBC Study Sphere, a study room locator and reservation app that allows students to book study rooms across the various scattered 
study spaces on campus. Study Sphere’s study room booking system will be able to access various relevant websites, such as the UBC library website to book library study 
spaces, or UBC learning spaces to access study spaces in the MacLeod building. Additionally, Study Sphere allows students to post invitations for other students to join them.
Study Sphere would allow students to seek other study buddies on the app to study for specific subjects. Students would also be able to label the room as a silent quiet space or
a collaborative space when posting invitations. 

Our first screen will consist of two date selectors and two buttons. The two date selectors will have similar functionality to date selectors on plane ticket purchasing websites.
Clicking on the first date selector will pop up a calendar for you to select the desired start date to look for a study room. Clicking on a specific date will allow you to pick which
hour of the day is desired. The next date selector will be for the end time. Click on the day and hour to end the search for study rooms. After selecting the start time and end time,
the user can click two buttons. One button is labeled “Find Empty Study Rooms”. The website will search and present all empty study rooms within the given time frame. The second button
is labeled “Join Existing Study Rooms”. The website will search for open study sessions from other students within the given time frame.
![The main page](DesignSolutionScreenshots/startendCalendar.PNG)

If you click the “Find Empty Study Rooms” button, a chronologically sorted list of available empty rooms will be shown. Each row will show the time available and location of the study
room with a button right next to it that will redirect the user to UBC’s study space booking website for the specified room and time slot. Additionally, there will be a prompt that allows
the user to host a study session (public or private).
![Find empty study rooms](DesignSolutionScreenshots/findempty.png)

After clicking "Book Now!", the users are redirected to UBC's corresponding study space booking webpage for that room and time slot. The user will have to log in with their cwl to
be able to actually book the room.
![Redirected to this webpage](DesignSolutionScreenshots/redirectToBooking.PNG)

If you click the “Join Existing Study Rooms” button, a list of study sessions will be shown. The default order is by descending course number, but the user can choose to filter through courses or year.
Open (public) study sessions will have a button that directly lets you join the room, while invite (private) study sessions will have a button that sends a request to the person who booked the study room
and that person can choose to accept your request to join or deny it. 
![Join existing study rooms](DesignSolutionScreenshots/joinexisting.png)
