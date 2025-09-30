# **Triage Document for STUDYSPHERE**

## **Summary of Issues**

### **Issues Reported by Team: \[Mithrellas-Planify\]**

---

### **(High Priority) Search "Any Location" Causes Long Wait Time**

* **Problem**: The "Any Location" selector currently has no logic to resolve the request, resulting in long wait times.  
* **Decision**: Accepted.  
* **Rationale**: This issue stems from logic initially implemented for a blank location field to filter for "any location." Fixing this will improve response times and user experience.  
* **Potential Fix**: Either remove the "Any Location" selection and clarify in instructions that leaving the location field blank achieves the same result, or migrate the logic from the blank location field to the "Any Location" selector.  

---

### **(High Priority) End Time Can Be Before Start Time with No Error Displayed**

* **Problem**: Start and end times are not validated, allowing invalid inputs that may cause server bugs during room filtering.  
* **Decision**: Accepted.  
* **Rationale**: This issue is critical as it affects server functionality. A fix would ensure users input valid start and end times, improving reliability.  
* **Potential Fix**: Add frontend validation to ensure that the start time is strictly before the end time (e.g., 08:00 is before 10:00, but not vice versa).  

---

### **(High Priority) Completing the Functionality of Hosting and Joining Study Sessions**

* **Problem**: The functionalities for hosting and joining study sessions are incomplete, preventing users from fully utilizing these features.  
* **Decision**: Accepted.  
* **Rationale**: These are core features of the platform, and their absence significantly impacts usability and user satisfaction.  
* **Potential Fix**: Implement the backend and frontend logic necessary to complete the functionality, including session creation, user assignment, and proper session states.  

---

### **(Medium Priority) Inputting Time in Creating Study Session**

* **Problem**: There is a redundant input time on the "creating study session" dashboard, allowing users to set custom start and end times that may conflict with booked study room times.  
* **Decision**: Accepted.  
* **Rationale**: This is a user flow issue that complicates session creation. Fixing this ensures consistency and reduces errors.  
* **Potential Fix**: Remove the inputting time option from the "creating study session" dashboard. Automatically populate session start and end times based on the selected study room's availability.  

---

### **(Medium Priority) Clicking "Create Study Session" with Filter Parameters Leads to Errors**

* **Problem**: Attempting to create a session directly without selecting a study room causes server errors.  
* **Decision**: Accepted.  
* **Rationale**: This issue disrupts user flow and may discourage use of the platform. A robust fix will improve reliability and prevent errors.  
* **Potential Fix**: Restrict the "Create Study Session" button to only appear after a user has selected a study room.  

---

### **(Medium Priority) Graceful Shutdown of Sessions When Server Closes**

* **Problem**: Currently, tasks are terminated without graceful shutdown when the server is closed, leading to potential data loss.  
* **Decision**: Accepted.  
* **Rationale**: This is a backend issue that impacts reliability and data integrity during server operations.  
* **Potential Fix**: Implement a mechanism to gracefully terminate active sessions, such as adding shutdown hooks or saving session states before termination.  

---

### **(Medium Priority) Error When Clicking "Find Study Session" with Empty Filter Parameters**

* **Problem**: Attempting to find a study session without setting any filter parameters causes server retrieval errors.  
* **Decision**: Accepted.  
* **Rationale**: Filtering functionality is central to the platform, and this bug disrupts usability.  
* **Potential Fix**: Add frontend validation to ensure users set at least one filter parameter before submitting the form.  

---

### **(Low Priority) Time Increment \- 30 Minutes vs. 1 Hour**

* **Problem**: The app currently uses 1-hour increments for filtering, which differs from the original requirement of 30-minute increments.  
* **Decision**: Accepted.  
* **Rationale**: While the 1-hour increment is simpler and matches most use cases, clarifying or reverting to 30-minute increments aligns with initial requirements.  
* **Potential Fix**: Update the time selectors to allow 30-minute increments, or revise the documentation to reflect the current 1-hour increment logic. Furthermore, fix the time increment and allow users to type valid hours in the field.  

---

## **Prioritized List of Issues**

### **High Priority:**

1. Search "Any Location" Causes Long Wait Time  
2. End Time Can Be Before Start Time with No Error Displayed  
3. Completing the Functionality of Hosting and Joining Study Sessions

### **Medium Priority:**

1. Inputting Time in Creating Study Session  
2. Clicking "Create Study Session" with Filter Parameters Leads to Errors  
3. Graceful Shutdown of Sessions When Server Closes  
4. Error When Clicking "Find Study Session" with Empty Filter Parameters

### **Low Priority:**

1. Time Increment \- 30 Minutes vs. 1 Hour  

---

## **General Feedback to Team:**

* The focus on completing critical features like hosting and joining study sessions is well-aligned with user needs.  
* Refining edge cases, such as empty filters and redundant inputs, will improve the platform's overall reliability and user experience.  
* Addressing high-priority issues promptly will significantly enhance the perceived quality and usability of the application.


