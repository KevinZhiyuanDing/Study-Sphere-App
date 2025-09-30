# Requirements

1. Date selector functionality
    - Front End:
        - The date selector should be a clickable input field that, when clicked, opens a calendar popup allowing the user to select a date
        - The calendar popup should display all days of the month, with the current date highlighted
        - The user can interact with the calendar by clicking on a date, which then populates the selected date into the input field in ‘MM/DD/YYYY format, then pops up a time selector
        - The time selector should be a dropdown menu that displays time slots in 30-minute increments from “08:00 AM” to “10:00 PM”
1. Room Availability search
    - Front End:
        - The “Find Empty Study Rooms” button should be visually distinct and labeled clearly (filled blue box when hovered over)
        - Upon clicking the “Find Empty Study Rooms” button, triggers a method to initiate the room availability search
        - While the room availability search is running, provide a loading indicator to inform user that the system is processing the request
        - The loaded results must include the room location and availability time
            - Ordered left to right: Time | Location | Room Number | Book Now button
1. Room Booking Redirect
    - Front End:
        - Each available booking room must display a “Book Now!” button adjacent to its listing (on the right of the room details)
        - Clicking the “Book Now!” button should redirect the user to UBC’s study space booker webpage for that specific room and time slot, opening the link in a new tab
    - Back End:
        - The url for the UBC study space booker webpage is an element that is returned by the web scraping module
1. Study Session Invitation Posting
    - Front End:
        - The invitation posting form should contain labeled input fields for title, date, time, and type (public or private), and the users email address (used for sending updates for when others join a public session)
        - The user should be able to submit the invitation form by clicking a “Post Invitation” button , which must be clickable and change colour on hover to indicate interactivity
1. Study Session Filtering
    - Front End:
        - There is a navigation button labeled “Find Active and Upcoming Study Sessions” that redirects the user to a webpage for finding the currently ongoing study sessions and upcoming study sessions
        - The filter options should be presented as checkboxes that users can click to reveal filtering options based on course codes (e.g., PHYS) and academic years (e.g, 3rd year)
        - Upon selecting filter options, clicking a “search” button, the user is provided with a list of study sessions that match the criteria applied by the filter options
1. Joining Public Study Sessions
    - Front End:
        - Each public study session should display a “Join Session” button that is easily identifiable (blue box around the words) and clickable (changes colour on hover to indicate interactivity)
        - Upon clicking “Join Session”, a text box appears, allowing the user to enter a brief description (users can provide their name or other related information) along with a button called “Confirm”
        - Upon clicking “Confirm”, triggers a method to email the host of the study session with all the text provided in the description
    Back End:
        - Sends an email to the address provided by the host of the study session, with all the text entered in the description
1. Requesting to Join Private Sessions
    - Front End:
        - Each private study session should display a “Request to Join” button that is easily identifiable (blue box around the words) and clickable (changes colour on hover to indicate interactivity)
        - Upon clicking “Request To Join”, a few required text fields will appear, in addition to the text box for entering a brief description. There will also be a button called “Confirm"
            - User must provide their first name, an email address, and their year level
        - Upon clicking “Confirm”, triggers a method to email the host with the provided information formatted as follows:
            -  Name of Interested Student: \<Name provided\>
            - Email of Interested Student: \<Email provided\>
            - Year Level of Interested Student: \<Year level provided\>
            - Brief description: \<Optional description provided\>
1. Session Host Accepting Join Requests
    - Front End:
        - After clicking the link sent through email, the host of the study session is given 2 buttons, one for accepting, one for declining the join request
        - Upon clicking “accept” or “decline”, triggers a module to send an email back to the student who requested to join the study room informing about the status of the request -- either “Host has accepted the request” or “Host has declined the request”
    - Back End:
        - The link provided in the email to the host for join request should bring the user to a webpage that has two buttons to either accept or decline the join request
        - When the user clicks “accept” or “decline”, there is a method within the notification module for sending out the email update to the student who requested to join the private session
1. User Profile Management
    - Front End:
        - Users must have a “Profile” button visible in the main navigation bar that, when clicked, opens the user’s profile page
        - Profile fields (name, email) must be editable through input fields that allow text input
        - To edit an existing profile, there is a button that says “Edit” next to the field (on the right of the field), that upon click, will make the text box containing the field editable
        - To confirm an edit, there will be a button that says “Save Changes” in the same place as where the “Edit” button was, that upon click, will confirm the changes made
    - Back End:
        - All changes to the user profile must be saved when the user clicks the “Save Changes” button
1. Notification System
    - Back End:
        - Notifications must be sent to users’ provided email address, triggered by specific user actions (e.g., Request to Join private study session)
1. Web Scraping Implementation
    - Back End:
        - The web scraping module must be implemented to extract specific details: time slots, location, room id, availability, and the url for redirecting to UBC’s booking site for the specific room at the specified time slot
        - The extracted rooms must be based on the specified selectors or identifiers (i.e., the filters that the user has applied in their search query)
1. Data Update Frequency
    - Back End:
        - The website should automatically initiate a web scraping operation every 30 minutes to update room availability data stored
        - Web scraping operation is initiated again, overriding the pre-set frequency when a user triggers the search (clicks the “Find Empty Rooms” button)
            - Time since last updated is set to the current time
        - For efficiency, the web scraping will only scrape Room Id first, then compare with existing data (if the number of Room Id and Room Ids do not match up to the existing data, then perform a “full scrape” -- obtaining all the necessary data)
