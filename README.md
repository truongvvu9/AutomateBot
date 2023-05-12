# AutomateBot
A console application written in Java that allows you to create a bot that can automate any task.
<br>
This application allows you to create a bot that can automate any task that you do on the computer. 
<br>
<br>
In order for this application to work, you must add the JNativeHook third party library as a dependency. The JAR file can be found in the JNativeHook folder on this repository. This library allows this application to listen for global keyboard events and global mouse events. To add the JNativeHook .JAR file as a dependency, if you are using IntelliJ Idea Community Edition, click on File > Project Structure > Modules > Module Source > + > JARS or Directories > then navigate to the folder that contains the JNativeHook .JAR file.

<br>
Main menu:
<br>
1.Create an action. (This option allows you to create an action that the bot can automate)
<br>
2.Load an action. (This option allows you to load an action that you saved as a .txt file)
<br>
3.Run an action. (This option allows you to get the bot to automate a task that you just created)
<br>
4.Save an action. (This option allows you to save an action to a .txt file so you can load it in the future)
<br>
5.Exit. (Exit out of the application)

<br>
<br>

Create an action:
<br>
1.Move mouse (Prompts you to move the cursor to a location on the screen and press the Print Screen button on the keyboard to capture the XY coordinates for that cursor location. Press x and then enter to save the coordinates as a string instruction and tells the bot to move the cursor to those XY coordinates.
<br>
2.Left click (Saves a string instruction that tells the bot to click the left click button of the mouse)
<br>
3.Right click (Saves a string instruction that tells the bot to click the right click button of the mouse)
<br>
4.Type something (Saves a string instruction that tells the bot to type something that you specified)
<br>
5.Pause (Saves a string instruction that tells the bot to pause for a specified number of seconds. Make sure you pause between tasks because the bot will perform the task really fast. For example, if you tell the bot to move cursor to Google Chrome icon, then tell it to left click the mouse button twice, after it left clicks twice you should tell it to pause for 4 seconds to allow your computer some time to open up Google Chrome before moving on to another task.)
<br>
6.Scroll mouse wheel (Saves a string instruction that tells the bot to screel the mouse wheel either up or down. When you choose this option, you have to either scroll up or down, you cannot scroll both up and down. This is is because scrolling up gives a negative value and scrolling down gives a positive value. This option will prompt you to scroll either up or down and then you have to press the right Shift button to record the number of scrolls that you just performed and then you have to press the right Shift button again to save the number of scrolls and then clear it. Then press x and then enter to save it as a string instruction for the bot to perform. To keep it simple, whenever it prompts you to scroll up or down, perform the scroll then press the right Shift button twice and then press x and hit enter.)
<br>
7.Save action list (Saves all the string instructions and exits)
<br>
8. Exit (Exits out of this menu and goes back to the main menu and it saves the string instructions. Same function as opton #7)

<br>
Load an action:
<br>
This option allows you to load a text file that contains the list of string instructions which tells the bot what to do. You type the name of the text file without the .txt extension. (There is a minor bug here. I forgot to perform some validation to check if the file exists or not. Will be fixing this soon.)
<br>
Run an action:
<br>
This option allows you to get the bot to automate the task that you just created or loaded. This option has two sub-options: 1. Run action by number of times 2. Run action indefinitely. The first option allows you to input how many times you want the bot to perform this task. The second option will make the bot perform the task indefinitely, until you manually close the application.
<br>
<br>
Save an action:
<br>
This option allows you to save the task which contains the string instructions that you just created to a text file so that you can load it in the future.



  
