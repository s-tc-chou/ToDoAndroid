# Pre-work - To Do List (Android version)

ToDo is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item.

Submitted by: Steve Chou

Time spent: 25 hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can **successfully add and remove items** from the todo list
* [x] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
* [x] User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

* [x] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [ ] Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [x] Add support for completion due dates for todo items (and display within listview item)
* [x] Use a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing items
* []x Add support for selecting the priority of each todo item (and display in listview item)
* [ ] Tweak the style improving the UI / UX, play with colors, images or backgrounds

The following **additional** features are implemented:

* [ ] Improve the sytle of the list to include a checkbox on the front page
* [ ] Add category functionality
* [ ] Add ability to integrate the due date into calendar with calendar API.  

## Video Walkthrough 

Here's a walkthrough of implemented user stories:

http://i.imgur.com/mn9JAaE.gif

GIF created with EZGif and AZ Screen Recorder

## Notes

I had originally intended to include the fragment piece, since that's an important concept to be familiar with, and a lot of
functionalities (priority, dates) would needed to be re-written once the fragment was done, so I put that off until the fragment
was done.  However, I ran into a lot of issues around fragment behaviors (null pointers, background being clickable, view not
expanding correctly and somehow including buttons, layout issues) so I reverted those changes so I can submit the prework on 
time.

I also ran into issues with the SQLite revolving around removing list items and listview positions, and resolved it by 
reordering the list items positions in the DB whenever a delete took place.  

The vast majority of the time was spent on fragments and debugging SQLite.  


## License

    Copyright [2016] [Steve Chou]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
