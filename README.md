hf-iotd
=======

HeadFirst-NasaDailyImage

This is an excercise of the Head First android programming book.

It is an rss feed reader that reads daily feed from the nasa website and presents it on a screen.
It includes a title, data, description and an image.


IDE (eclipse based android studio)
----
I have created this with the android studio. 
I have not put any effort into excluding certain IDE specific files etc... from version control.
If you want to run this application i advise you to download android development tools (adt).
his contains an folder eclipse, which is ... wait for it ... a customized eclipse version to create android applications.
If you use that one you should be able to open this project.
Enjoi!


Corrections
------------
- The xml of the rss feed is correctly parsed here 
(the parser from the sample in the book doesn't work, probably because the rss feed structure has changed...)
- Used asynctask to retrieve the feed. In newer android sdk versions, you cannot do work threads on the GUI anymore.
It was allready bad practice but now it is no longer allowed by the compiler.


Some extensions
----------------
- Not just the info of today is retrieved, info of multiple days is retrieved and then the date/Day is put in a spinner
(or dropdownlist/combobox if you like) and the user can select the info for a particular day.


