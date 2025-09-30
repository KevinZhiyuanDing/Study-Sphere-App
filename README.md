# Team Aragorn - UBC Study Sphere
## Overview
* App
  * package: cpen.app
    * desktop user interface
  * package: cpen.network
    * server communication code
* Server
  * package: cpen.database
    * storage of user data
  * package: cpen.network
    * client communication code
  * package: cpen.ui
    * starting point of the server code
  * package: cpen.webscrape
    * code for pulling room availability off of website
* DesignSolutionScreenshots
  * draft of screenshots
* Stubs
  * initial code plan (deprecated)
* .md files
  * initial planning
* Notes
  * All code is in App and Server
  * App is an intellij project
    * Starts in cpen.app
  * Server is a pycharm project
    * Starts in cpen.ui, structure is to run the server starting from this package, all other packages are helpers
## Problem statement
UBC students struggle to stay informed about study room locations, availability because of the scattered postings across various platforms. Students often have to check multiple sources, 
such as websites and help desks to find a suitable space. This can make it difficult to plan study sessions effectively, especially during peak times when rooms are in high demand. Additionally, 
it can be hard to find study buddies. Currently, there is no platform that allows students to connect to study courses together. Many students may be hesitant to reach out to peers and 
may not know their availability. 


