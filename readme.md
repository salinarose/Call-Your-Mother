
# Call Your Mother! 
A timezone management app for keeping in touch with friends and family around the world.

## Description  
Call Your Mother is a desktop app that allows you to easily keep in touch with friends and family with chaotic schedules across different time zones. Automatically calculate the windows of time you have in common with up to four other people.

Features include:
- Save contacts and input their time zone and availability
- Automatically calculates shared time windows based around your own schedule
- Easily schedule a meetup across time zones with up to three other people
- Save contacts, scheduled calls, and schedule for future convenience

## Why  
Many people, including myself, have friends and family members scattered across different time zones. Living so far apart and unable to see so many of my loved ones in person, I rely heavily on text and social media to stay in touch. However, nothing quite beats talking face-to-face over video chat. Unfortunately, this can be difficult to prioritize when busy schedules and vastly different time zones get in the way. Additionally, looking up and converting between time zones every time you want to call your grandmother who lives on the other side of the globe gets tedious very quickly.

Rather than looking up or converting time zones in your head and trying to keep track of everyone's regular schedule, this program allows you to save a contact, their time zone, and their regular availability just once. It will then compute the times in which you are mutually compatible. Even better, you can plan a call with up to three other people - all from different time zones and with different schedules - and it will find the times that work for everyone involved. Data is saved locally for future convenience and privacy.


## Getting Started  
Need to work on this

## Usage  
Languages and technologies used include Java, JSON, JUnit, Maven, JavaFx, and SceneBuilder. Built in Eclipse with Java 17.  

User settings, contacts data, and schedule calls are all saved locally as settings.JSON, contacts.JSON, and calls.txt, respectively. They may or may not exist the first time the program is run. If the user settings file does not exist or is deleted, the program will use the device’s system clock to determine the default location and time, but the user can easily change this under the settings tab.  

On the home screen, a list of currently saved contacts is displayed. On the right, the current time, location, offset, and currently scheduled calls are shown. From here the user can add or edit contacts or plan a new call.  

On the “planning a call” page, the user may select 1 - 3 contacts in the list and calculate the mutual availability shared between all of them. The availability for each is stored by day and time as either available (true) or not available (false), relative to their own selected time zone. To calculate the mutual availability, the program iterates through the user’s schedule and finds their available times. It converts the user’s and each contact’s zone into an offset value and calculates the difference. This difference is applied to each contact and the mutual availability is stored in a map and displayed in a grid to the user.  

Currently, non-hour offsets are truncated down to the hour. For example, an offset of +02:30 is truncated to +02. I hope to fix this in a future update!  

To avoid loss of data, the user is prompted to save changes when exiting the program.  

### Planned future features:
- Account for 30 minute offsets and DST
- Light and dark modes
- Profile pictures for each contact
- Prettier UI overall
- more!

## Contributing
Any and all contributions are welcome! If you'd like to get involved in the project, here are some ways you can help:  

1. Find and report bugs: if you encounter any bugs or come across something that doesn't seem to be working properly, please let me know. You can open an issue describing the problem and how it occurred.
2. Suggestions: if you have any ideas for new features or have a better idea for implementing an existing one, I'd love to hear about it as well. You can open an issue describing your idea.
3. Contribute code: if you're a developer yourself and you want to contribute, follow the instructions below.
4. Documentation: feel free to add more documentation where you see fit!

### Instructions
1. Fork this repository and clone it
2. Add your contribution
3. Commit and push changes
4. Wait for pull request to be merged

Thank you!

---

For questions, feel free to message me or email me at salinasharudin@gmail.com



