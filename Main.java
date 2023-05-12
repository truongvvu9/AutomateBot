import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main{
    public static void main(String[] args) throws NativeHookException, InterruptedException, AWTException {
        Logger log = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        log.setLevel(Level.OFF);
        GlobalScreen.registerNativeHook();
        GlobalMouseListener mouseListener = new GlobalMouseListener();
        GlobalListener listener = new GlobalListener(mouseListener);
        menu(listener);
        GlobalScreen.unregisterNativeHook();
        GlobalScreen.removeNativeKeyListener(listener);





    }

    private static void menu(GlobalListener listener) throws InterruptedException, AWTException {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> actionList = null;
        boolean exit = false;
        while(true){
            System.out.println("1.Create an action");
            System.out.println("2.Load an action");
            System.out.println("3.Run an action");
            System.out.println("4.Save an action");
            System.out.println("5.Exit");
            String choice = scanner.nextLine();
            switch(choice){
                case "1":
                    actionList = createAction(listener);
                    break;
                case "2":
                    actionList = loadAction();
                    break;
                case "3":
                    if(actionList != null & actionList.size() > 0){
                        promptRunAction(actionList);
                    }else{
                        System.out.println("You did not create any actions.");
                    }
                    break;
                case "4":
                    if(actionList != null && actionList.size() > 0){
                        saveAction(actionList);
                    }else{
                        System.out.println("You did not create any actions.");
                    }
                    break;
                case "5":
                    exit = true;
                    break;
                    default:
                    System.out.println("That is not a valid choice. Please try again.");
            }
            if(exit){
                break;
            }

        }

    }
    private static ArrayList<String> createAction(GlobalListener listener){
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> actionList = new ArrayList<String>();
        boolean exit = false;
        while(true){
            System.out.println("1.Move mouse");
            System.out.println("2.Left click");
            System.out.println("3.Right click");
            System.out.println("4.Type something");
            System.out.println("5.Pause");
            System.out.println("6.Scroll mouse wheel");
            System.out.println("7.Save action list");
            System.out.println("8.Exit");
            switch(scanner.nextLine()){
                case "1":
                    actionList.add(moveMouse(listener));
                    break;
                case "2":
                    actionList.add(leftClick());
                    break;
                case "3":
                    actionList.add(rightClick());
                    break;
                case "4":
                    actionList.add(typeSomething());
                    break;
                case "5":
                    actionList.add(pause());
                    break;
                case "6":
                    actionList.add(scrollWheel(listener));
                    break;

                case "7":
                case "8":
                    exit = true;
                    break;

                    default:
                    System.out.println("That is not a valid choice. Please try again.");
            }
            if(exit){
                return actionList;
            }
        }

    }
    private static void promptRunAction(ArrayList<String> actionList) throws InterruptedException, AWTException {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        while(true){
            System.out.println("Choose one:");
            System.out.println("1.Run action by number of times");
            System.out.println("2.Run action indefinitely (until you close the application");
            System.out.println("3.Exit");
            switch(scanner.nextLine()){
                case "1":
                    runActionByNumberOfTimes(getNumberOfTimesToRunAction(),actionList);
                    break;
                case "2":
                    runActionIndefinitely(actionList);
                    break;
                case "3":
                    exit = true;
                    break;
                    default:
                    System.out.println("Invalid choice. Please try again.");
            }
            if(exit){
                break;
            }
        }
    }
    private static void runActionByNumberOfTimes(int numberOfTimes, ArrayList<String> actionList) throws InterruptedException, AWTException {
        for(int i=0; i<numberOfTimes; i++){
            runAction(actionList);
        }

    }
    private static void runActionIndefinitely(ArrayList<String> actionList) throws InterruptedException, AWTException {
        while(true){
            runAction(actionList);
        }
    }
    private static int getNumberOfTimesToRunAction(){
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("How many times do you want to run the action?");
            boolean hasInt = scanner.hasNextInt();
            if(hasInt){
                return scanner.nextInt();
            }else{
                System.out.println("You did not enter a valid number.");
            }
        }
    }
    private static void runAction(ArrayList<String> actionList) throws AWTException, InterruptedException {
        pause(promptPauseBeforeAction());
        Robot robot = new Robot();
        for(int i=0; i<actionList.size(); i++){
            switch(actionList.get(i).charAt(0)){
                case 'm': //move mouse
                    String coordinatesString = actionList.get(i).substring(10);
                    String[] coordinatesStrArray = coordinatesString.split(",");
                    robot.mouseMove(convertStringToInteger(coordinatesStrArray[0]),convertStringToInteger(coordinatesStrArray[1]));
                    break;
                case 'l': //left click
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    break;
                case 'r': //right click
                    robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                    break;
                case 't': //type something
                    typeText(actionList.get(i));
                    break;
                case 'p': //pause
                    pause(convertStringToInteger(actionList.get(i).substring(6)));
                    break;
                case 's': //scroll mouse wheel
                    robot.mouseWheel(convertStringToInteger(actionList.get(i).substring(7)));
                    break;
            }

        }
    }
    private static int convertStringToInteger(String digits){
        return Integer.parseInt(digits);
    }
    private static void typeText(String text) throws AWTException {
        for(int i=5; i<text.length(); i++){
            buttonPresser(text.charAt(i));
        }

    }
    private static void pause(long seconds) throws InterruptedException {
        for(int i=0; i<seconds; i++){
            System.out.println(i);
            Thread.sleep(1000);
        }
    }
    private static void buttonPresser(char key) throws AWTException {
        Robot roboTyper = new Robot();
        switch(key){
            case 'A':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_A);
                roboTyper.keyRelease(KeyEvent.VK_A);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case 'a':
                roboTyper.keyPress(KeyEvent.VK_A);
                roboTyper.keyRelease(KeyEvent.VK_A);
                break;
            case 'B':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_B);
                roboTyper.keyRelease(KeyEvent.VK_B);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case 'b':
                roboTyper.keyPress(KeyEvent.VK_B);
                roboTyper.keyRelease(KeyEvent.VK_B);
                break;
            case 'C':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_C);
                roboTyper.keyRelease(KeyEvent.VK_C);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case 'c':
                roboTyper.keyPress(KeyEvent.VK_C);
                roboTyper.keyRelease(KeyEvent.VK_C);
                break;
            case 'D':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_D);
                roboTyper.keyRelease(KeyEvent.VK_D);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case 'd':
                roboTyper.keyPress(KeyEvent.VK_D);
                roboTyper.keyRelease(KeyEvent.VK_D);
                break;
            case 'E':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_E);
                roboTyper.keyRelease(KeyEvent.VK_E);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case 'e':
                roboTyper.keyPress(KeyEvent.VK_E);
                roboTyper.keyRelease(KeyEvent.VK_E);
                break;
            case 'F':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_F);
                roboTyper.keyRelease(KeyEvent.VK_F);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case 'f':
                roboTyper.keyPress(KeyEvent.VK_F);
                roboTyper.keyRelease(KeyEvent.VK_F);
                break;
            case 'G':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_G);
                roboTyper.keyRelease(KeyEvent.VK_G);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case 'g':
                roboTyper.keyPress(KeyEvent.VK_G);
                roboTyper.keyRelease(KeyEvent.VK_G);
                break;
            case 'H':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_H);
                roboTyper.keyRelease(KeyEvent.VK_H);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case 'h':
                roboTyper.keyPress(KeyEvent.VK_H);
                roboTyper.keyRelease(KeyEvent.VK_H);
                break;
            case 'I':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_I);
                roboTyper.keyRelease(KeyEvent.VK_I);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case 'i':
                roboTyper.keyPress(KeyEvent.VK_I);
                roboTyper.keyRelease(KeyEvent.VK_I);
                break;
            case 'J':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_J);
                roboTyper.keyRelease(KeyEvent.VK_J);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case 'j':
                roboTyper.keyPress(KeyEvent.VK_J);
                roboTyper.keyRelease(KeyEvent.VK_J);
                break;
            case 'K':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_K);
                roboTyper.keyRelease(KeyEvent.VK_K);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case 'k':
                roboTyper.keyPress(KeyEvent.VK_K);
                roboTyper.keyRelease(KeyEvent.VK_K);
                break;
            case 'L':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_L);
                roboTyper.keyRelease(KeyEvent.VK_L);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case 'l':
                roboTyper.keyPress(KeyEvent.VK_L);
                roboTyper.keyRelease(KeyEvent.VK_L);
                break;
            case 'M':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_M);
                roboTyper.keyRelease(KeyEvent.VK_M);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case 'm':
                roboTyper.keyPress(KeyEvent.VK_M);
                roboTyper.keyRelease(KeyEvent.VK_M);
                break;
            case 'N':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_N);
                roboTyper.keyRelease(KeyEvent.VK_N);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case 'n':
                roboTyper.keyPress(KeyEvent.VK_N);
                roboTyper.keyRelease(KeyEvent.VK_N);
                break;
            case 'O':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_O);
                roboTyper.keyRelease(KeyEvent.VK_O);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case 'o':
                roboTyper.keyPress(KeyEvent.VK_O);
                roboTyper.keyRelease(KeyEvent.VK_O);
                break;
            case 'P':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_P);
                roboTyper.keyRelease(KeyEvent.VK_P);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case 'p':
                roboTyper.keyPress(KeyEvent.VK_P);
                roboTyper.keyRelease(KeyEvent.VK_P);
                break;
            case 'Q':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_Q);
                roboTyper.keyRelease(KeyEvent.VK_Q);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case 'q':
                roboTyper.keyPress(KeyEvent.VK_Q);
                roboTyper.keyRelease(KeyEvent.VK_Q);
                break;
            case 'R':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_R);
                roboTyper.keyRelease(KeyEvent.VK_R);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case 'r':
                roboTyper.keyPress(KeyEvent.VK_R);
                roboTyper.keyRelease(KeyEvent.VK_R);
                break;
            case 'S':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_S);
                roboTyper.keyRelease(KeyEvent.VK_S);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case 's':
                roboTyper.keyPress(KeyEvent.VK_S);
                roboTyper.keyRelease(KeyEvent.VK_S);
                break;
            case 'T':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_T);
                roboTyper.keyRelease(KeyEvent.VK_T);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case 't':
                roboTyper.keyPress(KeyEvent.VK_T);
                roboTyper.keyRelease(KeyEvent.VK_T);
                break;
            case 'U':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_U);
                roboTyper.keyRelease(KeyEvent.VK_U);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case 'u':
                roboTyper.keyPress(KeyEvent.VK_U);
                roboTyper.keyRelease(KeyEvent.VK_U);
                break;
            case 'V':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_V);
                roboTyper.keyRelease(KeyEvent.VK_V);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case 'v':
                roboTyper.keyPress(KeyEvent.VK_V);
                roboTyper.keyRelease(KeyEvent.VK_V);
                break;
            case 'W':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_W);
                roboTyper.keyRelease(KeyEvent.VK_W);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case 'w':
                roboTyper.keyPress(KeyEvent.VK_W);
                roboTyper.keyRelease(KeyEvent.VK_W);
                break;
            case 'X':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_X);
                roboTyper.keyRelease(KeyEvent.VK_X);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case 'x':
                roboTyper.keyPress(KeyEvent.VK_X);
                roboTyper.keyRelease(KeyEvent.VK_X);
                break;
            case 'Y':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_Y);
                roboTyper.keyRelease(KeyEvent.VK_Y);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case 'y':
                roboTyper.keyPress(KeyEvent.VK_Y);
                roboTyper.keyRelease(KeyEvent.VK_Y);
                break;
            case 'Z':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_Z);
                roboTyper.keyRelease(KeyEvent.VK_Z);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case 'z':
                roboTyper.keyPress(KeyEvent.VK_Z);
                roboTyper.keyRelease(KeyEvent.VK_Z);
                break;
            case '0':
                roboTyper.keyPress(KeyEvent.VK_0);
                roboTyper.keyRelease((KeyEvent.VK_0));
                break;
            case '1':
                roboTyper.keyPress(KeyEvent.VK_1);
                roboTyper.keyRelease((KeyEvent.VK_1));
                break;
            case '2':
                roboTyper.keyPress(KeyEvent.VK_2);
                roboTyper.keyRelease((KeyEvent.VK_2));
                break;
            case '3':
                roboTyper.keyPress(KeyEvent.VK_3);
                roboTyper.keyRelease((KeyEvent.VK_3));
                break;
            case '4':
                roboTyper.keyPress(KeyEvent.VK_4);
                roboTyper.keyRelease((KeyEvent.VK_4));
                break;
            case '5':
                roboTyper.keyPress(KeyEvent.VK_5);
                roboTyper.keyRelease((KeyEvent.VK_5));
                break;
            case '6':
                roboTyper.keyPress(KeyEvent.VK_6);
                roboTyper.keyRelease((KeyEvent.VK_6));
                break;
            case '7':
                roboTyper.keyPress(KeyEvent.VK_7);
                roboTyper.keyRelease((KeyEvent.VK_7));
                break;
            case '8':
                roboTyper.keyPress(KeyEvent.VK_8);
                roboTyper.keyRelease((KeyEvent.VK_8));
                break;
            case '9':
                roboTyper.keyPress(KeyEvent.VK_9);
                roboTyper.keyRelease((KeyEvent.VK_9));
                break;

            case '`':
                roboTyper.keyPress(KeyEvent.VK_BACK_QUOTE);
                roboTyper.keyRelease(KeyEvent.VK_BACK_QUOTE);
                break;

            case '~':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_BACK_QUOTE);
                roboTyper.keyRelease(KeyEvent.VK_BACK_QUOTE);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;

            case '!':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_1);
                roboTyper.keyRelease(KeyEvent.VK_1);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case '@':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_2);
                roboTyper.keyRelease(KeyEvent.VK_2);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case '#':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_3);
                roboTyper.keyRelease(KeyEvent.VK_3);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case '$':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_4);
                roboTyper.keyRelease(KeyEvent.VK_4);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case '%':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_5);
                roboTyper.keyRelease(KeyEvent.VK_5);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case '^':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_6);
                roboTyper.keyRelease(KeyEvent.VK_6);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case '&':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_7);
                roboTyper.keyRelease(KeyEvent.VK_7);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case '*':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_8);
                roboTyper.keyRelease(KeyEvent.VK_8);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case '(':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_9);
                roboTyper.keyRelease(KeyEvent.VK_9);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case ')':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_0);
                roboTyper.keyRelease(KeyEvent.VK_0);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case '-':
                roboTyper.keyPress(KeyEvent.VK_MINUS);
                roboTyper.keyRelease(KeyEvent.VK_MINUS);
                break;
            case '_':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_MINUS);
                roboTyper.keyRelease(KeyEvent.VK_MINUS);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case '=':
                roboTyper.keyPress(KeyEvent.VK_EQUALS);
                roboTyper.keyRelease(KeyEvent.VK_EQUALS);
                break;
            case '+':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_EQUALS);
                roboTyper.keyRelease(KeyEvent.VK_EQUALS);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case '[':
                roboTyper.keyPress(KeyEvent.VK_OPEN_BRACKET);
                roboTyper.keyRelease(KeyEvent.VK_OPEN_BRACKET);
                break;
            case ']':
                roboTyper.keyPress(KeyEvent.VK_CLOSE_BRACKET);
                roboTyper.keyRelease(KeyEvent.VK_CLOSE_BRACKET);
                break;
            case '{':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_OPEN_BRACKET);
                roboTyper.keyRelease(KeyEvent.VK_OPEN_BRACKET);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case '}':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_CLOSE_BRACKET);
                roboTyper.keyRelease(KeyEvent.VK_CLOSE_BRACKET);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case '|':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_BACK_SLASH);
                roboTyper.keyRelease(KeyEvent.VK_BACK_SLASH);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case ';':
                roboTyper.keyPress(KeyEvent.VK_SEMICOLON);
                roboTyper.keyRelease(KeyEvent.VK_SEMICOLON);
                break;
            case ':':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_SEMICOLON);
                roboTyper.keyRelease(KeyEvent.VK_SEMICOLON);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case '\'':
                roboTyper.keyPress(KeyEvent.VK_QUOTE);
                roboTyper.keyRelease(KeyEvent.VK_QUOTE);
                break;
            case '"':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_QUOTE);
                roboTyper.keyRelease(KeyEvent.VK_QUOTE);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case ',':
                roboTyper.keyPress(KeyEvent.VK_COMMA);
                roboTyper.keyRelease(KeyEvent.VK_COMMA);
                break;
            case '<':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_COMMA);
                roboTyper.keyRelease(KeyEvent.VK_COMMA);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case '.':
                roboTyper.keyPress(KeyEvent.VK_PERIOD);
                roboTyper.keyRelease(KeyEvent.VK_PERIOD);
                break;
            case '>':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_PERIOD);
                roboTyper.keyRelease(KeyEvent.VK_PERIOD);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case '/':
                roboTyper.keyPress(KeyEvent.VK_SLASH);
                roboTyper.keyRelease(KeyEvent.VK_SLASH);
                break;
            case '?':
                roboTyper.keyPress(KeyEvent.VK_SHIFT);
                roboTyper.keyPress(KeyEvent.VK_SLASH);
                roboTyper.keyRelease(KeyEvent.VK_SLASH);
                roboTyper.keyRelease(KeyEvent.VK_SHIFT);
                break;
            case ' ':
                roboTyper.keyPress(KeyEvent.VK_SPACE);
                roboTyper.keyRelease(KeyEvent.VK_SPACE);
                break;
        }

    }
    private static int convertCharacterToInteger(char digit){
        return Integer.parseInt(String.valueOf(digit));
    }
    private static ArrayList<String> loadAction(){
        ArrayList<String> actionList = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        String fileName = "";
        while(true){
            System.out.println("Enter the name of the text file that you want to load (include the .txt extension):");
            fileName = scanner.nextLine();
            if(fileName.substring(fileName.length()-3).equals("txt")){
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
                    while(bufferedReader.ready()){
                        String action = bufferedReader.readLine();
                        System.out.println("Added " + action);
                        actionList.add(action);
                    }
                    return actionList;
                } catch (FileNotFoundException e) {
                    System.out.println("There was a problem locating this file.");
                } catch (IOException e) {
                    System.out.println("There as a problem reading this file.");
                }


            }else{
                System.out.println("The file name you entered does not contain the .txt extension. Please make sure to include it and try again.");
            }
        }
    }
    private static void saveAction(ArrayList<String> actionList){
        Scanner scanner = new Scanner(System.in);
        String fileName = "";
        while(true){
            System.out.println("What do you want to name the text file?");
            fileName = scanner.nextLine();
            fileName += ".txt";
            File file = new File(fileName);
            if(file.exists()){
                while(true){
                    System.out.println("This file already exists. Do you want to overwrite it? (y/n):");
                    String choice = scanner.nextLine();
                    if(choice.equals("y")){
                        try {
                            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));
                            for(int i=0; i<actionList.size(); i++){
                                bufferedWriter.write(actionList.get(i));
                                System.out.println("wrote " + actionList.get(i));
                                bufferedWriter.newLine();
                            }
                            System.out.println(fileName + " has been created.");
                            bufferedWriter.close();
                            break;
                        } catch (IOException e) {
                            System.out.println("There was a problem writing to this file.");
                        }

                    }else if(choice.equals("n")){
                        break;
                    }else{
                        System.out.println("Invalid choice. Try again.");
                    }
                }

            }else{
                try {
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));
                    for(int i=0; i<actionList.size(); i++){
                        bufferedWriter.write(actionList.get(i));
                        System.out.println("wrote " + actionList.get(i));
                        bufferedWriter.newLine();
                    }
                    System.out.println(fileName + " has been created.");
                    bufferedWriter.close();
                    break;
                } catch (IOException e) {
                    System.out.println("There was a problem writing to this file.");
                }
            }

        }
    }
    private static String moveMouse(GlobalListener listener){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Move the cursor to any location on the screen and press the Prt Sc key on your keyboard to capture the X Y coordinates.");
        while(true){
            System.out.println("After capturing the X Y coordinates, press x and then enter to add it.");
            if(scanner.nextLine().equals("x")){
                String result = "movemouse " + listener.getCoordinatesList().get(listener.getCoordinatesList().size()-1).get(0) + "," + listener.getCoordinatesList().get(listener.getCoordinatesList().size()-1).get(1);
                System.out.println("Added '" + result + "' action");
                return result;
            }
        }

    }
    private static String scrollWheel(GlobalListener listener){
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("Scroll the mouse wheel either up or down. Do not scroll both up and down. After scrolling, press the right Shift button to record the scroll amount and then press the right Shift button again to save the scroll amount and to clear it. Then press x and then enter to exit.");
            if(scanner.nextLine().equals("x")){
                if(listener.getRightShiftPressedAmount() == 0 && listener.isPressedRightShift()){
                    listener.setPressedRightShift(false);
                    return "scroll " + listener.getScrollAmountList().get(listener.getScrollAmountList().size()-1);

                }else{
                    System.out.println("You either did not pressed the right Shift button once to record the scroll amount or you did not press the right Shift button twice to save the scroll amount and clear it.");
                }
            }else{
                System.out.println("Invalid choice. Press x and then enter to exit.");
            }

        }
    }
    private static String leftClick(){
        System.out.println("Added 'leftclick' action");
        return "leftclick";
    }
    private static String rightClick(){
        System.out.println("Added 'rightclick' action");
        return "rightclick";
    }
    private static String typeSomething(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter what you want the bot to type:");
        String result = "type " + scanner.nextLine();
        System.out.println("Added '" + result + "' action");
        return result;
    }
    private static String pause(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("How long do you want to pause? (seconds):");
        String result = "pause " + scanner.nextLine();
        System.out.println("Added '" + result + "' action");
        return result;

    }
    private static long promptPauseBeforeAction(){
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("How long do you want to pause before running the action? (seconds): ");
            boolean hasLong = scanner.hasNextLong();
            if(hasLong){
                return scanner.nextLong();
            }else{
                System.out.println("You did not enter a number. Please try again.");
            }
        }


    }
}
