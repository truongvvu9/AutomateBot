import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;
import java.util.ArrayList;

public class GlobalListener implements NativeKeyListener {
    //fields
    private String action;
    private ArrayList<ArrayList<Integer>> coordinatesList;
    private GlobalMouseListener mouseListener;
    private int rightShiftPressedAmount;
    private boolean pressedRightShift;
    private ArrayList<Integer> scrollAmountList;

    //constructor method
    public GlobalListener(GlobalMouseListener mouseListener) {
        GlobalScreen.addNativeKeyListener(this);
        action = "";
        coordinatesList = new ArrayList<ArrayList<Integer>>();
        this.mouseListener = mouseListener;
        rightShiftPressedAmount = 0;
        pressedRightShift = false;
        scrollAmountList = new ArrayList<Integer>();

    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        String keyPressed = NativeKeyEvent.getKeyText(nativeKeyEvent.getKeyCode());
        if(keyPressed.equals("Print Screen")){
            Point cursorLocation = MouseInfo.getPointerInfo().getLocation();
            int x = (int) cursorLocation.getX();
            int y = (int) cursorLocation.getY();
            ArrayList<Integer> tempList = new ArrayList<Integer>();
            tempList.add(x);
            tempList.add(y);
            coordinatesList.add(tempList);
            System.out.println(coordinatesList.size()); //test code
            System.out.println("Coordinates (" + x + "," + y +")" + " has been captured.");

        }
        if(keyPressed.equals("Unknown keyCode: 0xe36") && rightShiftPressedAmount == 0){ //pressed the left Shift button
            System.out.println("mouse scrolled " + mouseListener.getScrollAmount() + " times");
            rightShiftPressedAmount++;
            pressedRightShift = true;
        }else if(keyPressed.equals("Unknown keyCode: 0xe36") && rightShiftPressedAmount == 1){
            rightShiftPressedAmount = 0;
            this.scrollAmountList.add(mouseListener.getScrollAmount());
            System.out.println(mouseListener.getScrollAmount() + " has been added to the list.");
            mouseListener.setScrollAmount(0);
            System.out.println("Scroll amount has been set to 0.");
        }

    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {

    }
    //getter methods
    public String getAction() {
        return action;
    }

    public GlobalMouseListener getMouseListener() {
        return mouseListener;
    }

    public ArrayList<Integer> getScrollAmountList() {
        return scrollAmountList;
    }

    public int getRightShiftPressedAmount() {
        return rightShiftPressedAmount;
    }

    public boolean isPressedRightShift() {
        return pressedRightShift;
    }

    public ArrayList<ArrayList<Integer>> getCoordinatesList() {
        return coordinatesList;
    }

    //setter methods
    public void setAction(String action) {
        this.action = action;
    }

    public void setPressedRightShift(boolean pressedRightShift) {
        this.pressedRightShift = pressedRightShift;
    }
}
