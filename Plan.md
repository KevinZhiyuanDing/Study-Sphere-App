# Team Aragorn - UBC Study Sphere
## Plan

### Work Coordination

The PM will coordinate the work through the discord group chat because the PM is responsible for managing the project and is presumed to be most adept at coordinating group work.

Project management will be based on mutual accountability and helping each other with managing our tasks. We think this is the best format to maintain group cohesion and friendship.

We will have biweekly meetings via discord group chat. We will use https://www.when2meet.com/ to plan according to everyone’s agenda. Most of us share similar timetables, which makes this easier.

### Communication Tools
Discord will be the primary form of communication for this project. 
It is chosen because all members are active on Discord and would be more likely to see group notifications. Additionally, Discord supports features such as file sharing up to 8Mb, screen sharing, and pin notifications. These features make communication and referencing past documentation easier.

The alternative we decided not to use is Instagram. While all members also use Instagram, it contains features such as reels and stories which may be distracting. In addition, .java files and other common file types cannot be shared on Instagram

### Timelines

Nov.1  – Starting date

Nov.4  – Clarify project assignments

Nov.11 – Finish most backend features

Nov.18 - Finish most frontend features. 

Nov.25 - Complete beta release and perform rigourous bug testing

Nov.29 - Submit project beta release

Dec.6  - Evaulate major and minor functionality of other team's projects. Start triage and repair bugs

Dec.9  -  Complete repairs and push patches. Complete personal reflections.

### Requirement verification

1. Date selector functionality:

    a. We should have unit-tests that tests if clicking the calendar popup opens the calendar interface.

    b. There should be visual tests that the calendar displays the correct number of days for all months, including edge cases such as the month of April in a leap year.
    
    c. Common dates should either be unit-tested or manually tested to see if they can be selected.

    d. Unit tests on operations on the dropdown menu, such as clicking a time slot, should be included.
    
2. Room Availability search

    a. Tests on operations of the "Find Empty Study Rooms" state, such as clicking the "Find Empty Study Rooms" button should be verified.

    b. Perform visual testing that the load indicator is present

    c. Perform manual testing of trying to book an empty study room

    d. Manual test that room availability search returns appropriate information. Automatic unit-testing is difficult because the room availability search is non-predictable.

3. Room Booking Redirect

    a. Add test cases for "Book now" button function to make sure it directs user to study space booker webpage.

    b. Add test case to check is url returned by web scraper module is correct


4.  Study Session Invitation Posting

    a. Unit tests on basic operations such as adding invitation posting information in the input fields should be performed.

    b. Manual testing of submitting "Post Invitations" should be done.

5. Study Session Filtering

    a. Unit tests on basic operations should be performed.

    b. Manual testing of the course filters should be done.

    Manual testing of the filter by study session functionaly should be done.

6. Joining Public Study Sessions

    a. Visual inspection of the "Join Session Button" behaviour should be checked, e.g does the button turn color when hovered.

    b. Manually check if email is sent when "Confirm" is pressed.

7. Requesting to Join Private Sessions

    a. Visual inspection of the "Request to Join" behaviour should be checked, e.g does the button turn color when hovered.

    b. Check text fields for "Confirm" button, as well as input fields for user info.

8. Session Host Accepting Join Request

    a. Test cases for both accepting and declining joing request.

    b. Manual testing of if email is send your when request is specified.

9. User Profile mangement:

    a. Test cases for changing profile fields. Must verify if input information is correct. Ex. email must include '@' and '.com'.

    b. Test case that "Save Changes" updates user info in the backend

10. Notification System

    a. Manual checking if email is send to user for sepcific user actions (e.g. Request to join study session).

11. Web Scraping Implementation.

    a. Test cases that all required info about room availability is checked.

    b. Test cases that rooms shown satisfy the filters that the user applied.

12. Data Update Frequency

    a. Manually test if web scraping operation works wen user triggers the search.

    b. Manually check if web scraping is optimized to only scrape room id first.

General verification justification:
Many front end features require manual testing and visual inspection to verify functional correctness. Backend features can often use automatic test cases and they should be frequently check to ensure implementing one featre does not break another feature in another part of the code.

Since the room availability changes in real time, we cannot easily construct automatic test cases to verify if functionality related to room availability works correctly or not.
