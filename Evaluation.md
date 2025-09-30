## Issues we discovered
1. **Third Party Site suggested is flagged as a phishing site**
The website to upload images for creating posts is flagged as a phishing website.  
*tags: minor, enhancement*

1. **Redirects bring user to entire collection page**
Clicking on a specific building redirects users to a list of all available buildings. This also occurs for clicking on a specific post in the shop, on the home page. Doing so will redirect to a list of all the items that are posted instead of bringing the user directly to the item that was clicked.  
*tags: minor, bug*

1. **Search feature is missing completely**
The search feature does not work, rendering the search for recent postings unusable.  
*tags: major, bug*


1. **Repeatedly clicking submit creates more than one post**
Repeatedly clicking the submit button for “create post” creates more than one post.  
*tags: major, bug*

1. **Non-digit characters are allowed in price field**
It is possible to enter the letter "e" into the price field.  
*tags: minor, enhancement*

1. **Server does not save data**
The server is not protected against crashes, it does not store any data when it force quits or crashes.   
*tags: major, bug*

1. **Restrictions in price can be bypassed**
It is still possible to submit a post with a price of 3 decimal places, even though it is specifically stated that the text field requires 2 decimal places.  
*tags: minor, bug*

1. **Only text is clickable**
Even though the entire box containing the post gets filled with another colour when a mouse is hovered over it, only the text is clickable.  
*tags: minor, enhancement*

## Overall process
The documentation was descriptive enough that it was relatively easy to begin using the app. We utilized a series of methods to test the app systematically. Notably, we employed functional testing and usability testing.

We tried to understand the required functionality for their application by reading their documentation and their project planning documents. For example, the application required that “Users must be able to post items with details like price, description, and image.” By testing for this functionality, we were unable to make a post for the shop and housing categories because the image field did not accept any image urls from the third party website that was linked. The website that was linked also triggered Google Chrome’s automatic phishing detection alert.

We tested for common user inputs as well as uncommon edge cases. Common user inputs seem to be working as intended. Better edge case handling logic could be used. Edge cases included entering very large integers, and frequent button presses. For example, when entering a very large value for the price, the site does not recognize that the price field is filled and will not let the user proceed and create the post. Registering multiple button presses on the “create a post” page would result in more than one post being registered in the announcements section. We can not comment on the search functionality because it is not complete. As such, we were unable to test if posts were successfully received and created by the server.

We also tested based on Usability Testing. We put ourselves in the shoes of a potential commercial user who is using this application. We evaluated the general UI appeal and convenience of use. We think the UI design overall is appropriate and straightforward for a potential user. However, the issues with some lack of functionality means that we could not make a holistic and complete assessment of the user experience as the developers intended it to be.

We have included all bugs found in the “Issues We Discovered” section, labeling the more serious issues with the “major” tag. Some issues are purely enhancement-related, which means that the app will still work as intended if left unfixed. However, fixing these issues can make the app become more user-friendly. The most detrimental bug is that the server is not protected against crashes: all user-created post/announcement data was lost when the server was force quitted.
