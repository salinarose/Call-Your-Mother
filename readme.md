
# Time Zone Manager Project
For keeping in touch with friends and family around the world.

Created by Salina Sharudin  
salinasharudin@gmail.com

## Using the program: 
This program aims to help its users stay in contact with their loved ones around the globe. The user can add people to their contacts list and save their time zone and regular availability, as well as their own schedule. From this, the program allows the user to select one to three contacts and finds the times in which everyone is mutually available. If the user chooses to do so, they can save these times and create a reminder for regular calls at this time. 

### Why I created the Time Zone Manager:
Many people, including myself, have friends and family members scattered across different time zones. Living so far apart and unable to see so many of my loved ones in person, I rely heavily on text and social media to stay in touch. However, nothing quite beats talking face-to-face over video chat. Unfortunately, this can be difficult to prioritize when busy schedules and vastly different time zones get in the way. Additionally, looking up and converting between time zones every time you want to call your grandmother who lives on the other side of globe gets tedious very quickly.  

I created this program to help solve these problems. Rather than looking up or converting time zones in your head and trying to keep track of everyone's regular schedule, this program allows you to save a contact, their time zone, and their regular availability just once. It will then compute the times in which you are mutually compatible. Even better, you can plan a call with up to three other people - all from different time zones and with different schedules - and it will find times that work for everyone involved. 

I also created this program because I personally wanted to see what I could build myself completely from scratch. This is the largest complete personal project I have created and the first complete personal project I have uploaded to GitHub.

### What I learned while working on this project:
I learned quite a lot while building this program! For this project, I attempted to use the MVC design pattern for the first time. Though I am sure much about the structure of my program could be improved, I greatly enjoyed learning how this pattern works. This is one of the largest programs I have made solely by myself to date, and using the model greatly helped me break it down into manageable parts. By clearly defining my goals, what the minimum viable product would look like, and implementing features piece-by-piece, I was able to systematically make progress on the project even when using technologies that were completely new to me. For example, for this project I learned how to read and write JSON objects to a file. I used this to save both the contacts data and the user's settings data. Additionally, I gained valuable practice using Git and GitHub, JavaFX, SceneBuilder, and JUnit. Finally, I learned how to implement a singleton class, which I used for the user's settings class, as well as Java's many DateTime/ZonedDateTime/LocalDateTime classes.

### The tech used to build the Time Zone Manager:
Java, JSON, JUnit, Git/GitHub, Maven, Eclipse, JavaFx, SceneBuilder
Used Trello and other tools for project/time management

### What went well on this project: 
Most things in the project went well, which I attribute largely to extensive planning before I started implementing any code at all. Having gathered knowledge and experience working on other projects previous to this one, I tried my best to plan in detail as much as possible before I began writing any classes, keeping in mind that I had planned to use a couple technologies that were new to me. I planned time to set aside to learn them and kept the program flexible enough to account for unplanned changes, since I wasn't sure if how I imagined they would work out as planned. For example, JSON took a little bit of time and tinkering to work out, but ended up working beautifully in the final product. The diagrams and other schemas helped me set achievable goals, minimized backtracking, and helped me stay motivated. SceneBuilder helped me make each scene's view easily and efficiently. Using GitHub was not only helpful for version control, but also allowed me to more easily log progress and changes over time. I found JUnit very pleasant and convenient to use as well.

### Challenging aspects and what I would do differently:
Though the project progressed smoothly for the most part, it wasn't without its obstacles. For one, while implementing the offset conversion part, I learned that not all offsets are set on the hour! Some are actually on the 15/30 minute mark (in relation to UTC+0/Z). Unfortunately, I have not yet been able to implement a solution that reflects this - all offsets are truncated to the hour only for simplicity. Additionally, it does not yet account for daylight savings time.

I also think that the app would be well suited to work over the internet. Entering a schedule for each contact feels tedious and a little invasive. A better solution may be one in which each user sets their own time zone and schedule and then chooses to share this profile with others in their contacts list. In this case, I would have stored the data in a database instead of local JSON files. 

This was my first time attempting to implement a Model-View-Controller design pattern. I am certain I have made many mistakes in the design of my classes and that they could be vastly improved. As I gain more experience and feedback on my work, I hope to revisit this program and make improvements in this area. 

Lastly, there are many more features I would like to implement in the future. In addition to accounting for the rest of the offsets and daylight savings time mentioned, I would like to implement light/dark modes, and profile pictures for each contact, among other things.
