![alt text](https://user-images.githubusercontent.com/38051253/49102243-fd0ca480-f280-11e8-94d5-073255f18584.jpg)

# Pro Log
In short, Pro Log is a simple android app that was created for managing the user workouts, keeping the workouts data such as the date, which exercises were done and more. <b>This application is for practice only,</b> which explain why I didn't invest my time and energy to design it.

# Motivation
So, it is my first project on Android AND GitHub. Everyone has to start from somewhere.
You can't improve your code without dirt your hand a little.

# ScreenShots
![screenshot_2018-11-28-15-45-19](https://user-images.githubusercontent.com/38051253/49156388-f421de80-f325-11e8-9630-f92bb77c461f.png) ![screenshot_2018-11-28-15-44-51](https://user-images.githubusercontent.com/38051253/49156337-c63c9a00-f325-11e8-90c2-a4c139995867.png) ![screenshot_2018-11-28-15-44-44](https://user-images.githubusercontent.com/38051253/49156336-c63c9a00-f325-11e8-9a15-7b239cdd1055.png) 

# How to use?
In this specific explanation we will use Android Studio to compile and run the app.</br>
1.Download the project from GitHub.</br>
2.Open Android Studio and select the open tab from the File menu.</br>
3.Search for the location where the project was saved in, then double select it.</br>
4.Run the app (look for the green arrow icon or just press shift+F10 on windows).</br>
5.Select the device that you would like to use the app from.</br>
The project will be compiled on your device and then you ready to go.

# Libraries Used
1. <i>wdullaer datetimepicker</i>: has a dynamic date time picker, used to pick a date for a workout.
2. <i>gson</i>: used to serialize the sub-class of the workout (i.e exercises and worklines) so it would fit the database.
3. <i>room</i>: google's support library for sql, used to store the workouts.
and so on.

# Components Used
I chose some specific components to challange myself (and also because I had no choice :D ):
1. Expanded ListView and ListView: to show the data
2. Dialog Fragments: to add some UI
3. Shared References: To keep the settings and the default exercises list in a file
4. For database: ViewModel,RoomDataBase, Repository and Dao.
and so on.

# Contribute
Code review would be nice.

# About me
Ofek, a 2nd year student of computer science.</br> Currently- taking place in [Android Academy - TLV](https://www.facebook.com/groups/android.academy.ils/) Fundamentals course.

# License
Copyright 2018 Ofek Pintok (c)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
