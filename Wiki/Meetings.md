## History

| Meeting | Date | Time Start | Time End | TA Present | Attendance |
|:-:| -------- | :---: | :------: | :-: | :-: |
| 1 | 02/01/22 | 18:30 | 19:00 | :heavy_check_mark:  | All members present |
| 2 | 02/07/22 | 20:20 | 20:40 |   | All members present |
| 3 | 02/08/22 | 18:30 | 18:53 | :heavy_check_mark:  | All members present |
| 4 | 02/08/22 | 18:53 | 20:45 |   | All members present |
| 5 | 02/11/22 | 17:00 | 18:10 |   | All members present |
| 6 | 02/12/22 | 19:30 | 20:40 |   | All members present |
| 7 | 02/15/22 | 18:53 | 19:09 | :heavy_check_mark: | Josh absent |
| 8 | 02/17/22 | 18:00 | 18:47 |  | Connor absent |
| 9 | 03/01/22 | 18:30 | 21:30 | :heavy_check_mark: | Hua absent (just joined) |
| 10 | 03/07/22 | 20:15 | 20:55 |  | Connor absent |
| 11 | 03/08/22 | 18:00 | 20:00 | :heavy_check_mark: | All members present |
| 12 | 03/15/22 | 16:30 | 20:00 | :heavy_check_mark: | All members present |
| 13 | 03/22/22 | 16:30 | 20:00 | :heavy_check_mark: | All members present |
| 14 | 03/29/22 | 17:45 | 18:30 | :heavy_check_mark: | Josh absent |

-----


## Meeting Minutes

### Meeting 14:
  - Most database queries complete, QR code deletion, image upload and download, and integration of social queries remaining
  - TA feedback:
    - Complete integration and practice project demonstration
    - During the demo, will be looking for a fluid demo with features shown methodically and user stories covered in a sequential order
    - For the Q & A portion, expect to be asked for rational for design choices or implementation strategy

### Meeting 13:
  - Working on database integration, QR comments, integrating the sidebar, and finalizing the social tab
  - TA feedback:
    - Project demonstration: plan for around seven minutes with Q & A
    - Have a full integration by next week to allow for bug-resolving and time to practice the project demonstration

### Meeting 12:
  - Work session and plan next sprint's work items
  - Demo of Project Part 3
    - TA feedback: need to finish the database quickly, as integration will be time-consuming
    - Current bugs: rotating device reloads the screen, may not prompt to obtain location permissions in certain instances

### Meeting 11:
  - Work session
  - Merged all of our branches to get everyone up to date
  - Performed manual intent testing to make sure app works properly
  - Planned next steps for which features we wanted to work on

### Meeting 10:
  - Quick Catch up meeting
  - Introductions for Hua
    - Roundtable introduction (not the roundtable below)
    - Maybe a tour of the git
    - A tour of our tools and resources
      - Lucidchart
      - Figma
      - Firebase?
  - Roundtable
    - Josh
      - Status
        - Have the map fragment working and showing user location
        - Know the gist of how pins work, can implement when we have a qrlist
      - Issues
        - need something else to work on or the qrlist to be ready for me to continue the map
    - Yunhe
      - Status
        - nearly done qr generator
        - next will finish the qrcode login
      - Issues
        - looking good
    - Bhavya
      - Status
        - implemented the qrcode scanning
        - can take photos of the code location
      - Issues
        - looking good
      - Other
        - done his task, we'll assign a new one tomorrow
    - Mehul
      - Status
        - has the QRCode class pretty much done
        - started making a customlist for qrcodes
        - has a layout for the adapter
      - Issues
        - looking good, it was tricky to get the fragment to work at first but it's resolved now
    - Shalomi
      - Status
        - working on getting the user profile fragment
        - working on making user class
        - getting login with uuid setup
      - Issues
        - we will have to use the username for the unique id/database key
          - then have an entry in the database with each device id for that account

### Meeting 9:
  - Mostly a working session
    - Made UML Diagram
  - Meeting with TA
    - We are a little behind but not crazy, we just took a break over reading week.
  - Assigned/Selected tasks for everyone to work on
    - On backlog

### Meeting 8:
  - Planning of sprints 1-4
    - Sprint 1 (Fed 17 - 23):
      - Make UML from the CRCs we have
      - Plan a meeting when Connor can make it for a group work session
      - Rough plan of Database structure
      - Delegate some classes to get started on app creation
    - Sprint 2 (Feb 23 - March 01):
      - Basic Firestore access (GS 01.01.01)
      - Profile Class (US 04.01.01)
        - get UUID (US 04.02.01)
      - QRCode Class (US 01.01.01, US 08.01.01)
      - Be able to scan QR codes (Portion of US 02.01.01)
        - Also implement QR code list (US 02.03.01, US 01.02.01)
           - Deletion too (US 01.03.01)
    - Sprint 3 (March 01 - 08):
      - Be able to generate QR codes and QR code login (US 03.02.01)
      - adding score, geolocation, and photo of QR code (US 02.01.01)
      - add sorting methods to scanned code list
      - Make the map (Portion of US 06.01.01)
        - not pulling actual codes
        - able to add pins and stuff (dummy ones for now)
        - opens to the player's current location
    - Sprint 4 (March 08 - March 14):
      - implement other player profiles (US 01.07.01)
        - search for other players (US 05.01.01)
        - view that player's codes and stats (US 02.04.01)
      - putting the QR codes on the map (finishing the map) (US 06.01.01)
      - Some kind of deletion of malicious codes and players for the owner (US 09.02.01, US 09.03.01)
        - Sounds like the firestore console will be good enough
      - add in banner for your scores (eg. total scans, total score, best code, worst code) (US 01.05.01)
      - list of nearby QRcodes (US 05.02.01)


### Meeting 7:
  - Questions for TA and feedback:
    - Josh:
      - How should we get a Unique User ID (UUID) and/or do account creation?
        - **Answer:** Should have usernames for accounts. No specific implementation required when obtaining device ID: can simply google how to obtain the UUID. Can assume the SIM cards won't be changed so UUID is acceptable.
      - How do we know who has admin access for bad qrs, deleting users etc?
        - **Answer:** Create admin access account in database.
      - How do you recommend we get the ball rolling now? eg. Do we just dive into creating classes or should we make a UML?
        - **Answer:** Start with rough UML, then classes. Refine UML as project is developed. Begin implementing unit tests right away alongside your code. Checkout using GitHub actions.

  - Updates from TA:
    - Our group is on schedule.
  - Next steps:
    - Begin UML and plan sprints.


### Meeting 6:
  - CRC meeting:
     - Flushed out purposes of objects
     - Started planning for potential ways to manage firestore
     - Added CRC Cards to the Wiki


  - Storyboard Meeting:
     - Confirmed all user stories referenced and ensured no missing interfaces
     - Added arrows referencing how the user manipulates controls

### Meeting 5:
  - Review Storyboarding
    - Feedback
      - Josh
        - Whoopies, we don't need a login screen
        - ask for name or scan instead probably
      - Shalomi
        - need to add in a show profile/edit profile screen
      - Bhavya
        - Could add in a how to use the app screen
        - Need to add in comments to show how requirements are being filled
    - Next steps
      - Finish the above changes
      - Add in flow arrows and comments
		
  - Review CRC Cards
    - Feedback
      - Looks pretty good, needs a bit of a clean up
    - Next steps
      - Tidy up the cards and make language consistent

  - Getting Ready for submission
    - Getting the github wiki setup
       - Mehul
       - Connor
    - How are we gonna do meeting minutes?
       - In the wiki (like this :))
    - Should we do a once over of the backlog?
       - next meeting 
    - What will be done for the halfway
      - all of the S1 and S2

  - Roundtable
    - Bhavya
      - Storyboarding team should meet up this weekend
        - next meeting Saturday at 7:30pm
    - Connor
      - Wants to work on firebase too
    - Josh
      - I wanna do the firebase
      - Try to make the CRC cards look pretty
    - Mehul
      - not much, gonna work on the product backlog and finish up storyboarding
      - wants to be involved in the firebase too 
    - Shalomi
      - wanna go over the description again and double check stuff
      - would be dope if someone made a logo for the app
        - Mehul thinks it would be fun and will do it
    - Yunhe
      - just wanna flush out the storyboarding


  - TA Questions
    - How to implement UUID / Login
    - Where should we get started on the app creation
### Meetings 3 and 4:
  - TA Feedback:
    - Story points ~ number of hours required
    - Risk level: how critical the component is and its level of priority
    - Reminder: embed the storyboard and CRC cards using .svg
  - Completed:
    - Integrated everyone's individual work for team scrum board backlog
    - Brainstormed storyboard layout and major activities
    - Brainstormed major CRC card classes
  - Next Steps:
    - The storyboarding and CRC card teams will meet individually to start work
    - Next Meeting: 02/11/22
### Meeting 2:
  - Completed:
    - Distribute responsibilities for Project Part 2 backlog
      - Player - Mehul
      - Game QR Codes - Bhavya
      - Player QR Codes and Player Profile - Conner
      - Owner and Privacy - Josh
      - Scoring - Yunhe
      - Location and Searching - Shalomi
  - Next Steps:
    - Integrate individual work for the Backlog
    - Next Meeting: 02/08/22
  - TA Questions:
    - Risk metrics? Story point metrics?
    - Is a UML required for Project Part 2?
    - How are meeting minutes documented?
### Meeting 1:
  - TA Feedback:
    - Suggested tools:
      - https://miro.com/
      - https://www.figma.com/
  - Completed:
    - Established contact with TA
    - Project Part 1 and Lab 4 group exercise
  - Next Steps:
    - Prepare Project Part 2 and assign work to individuals
    - Next Meeting: 02/07/22