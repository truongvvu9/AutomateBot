import org.jnativehook.GlobalScreen;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.jnativehook.mouse.NativeMouseWheelEvent;
import org.jnativehook.mouse.NativeMouseWheelListener;

import java.util.ArrayList;

public class GlobalMouseListener implements NativeMouseWheelListener {
    //fields
    private int scrollAmount;
    //constructor method
    public GlobalMouseListener() {
        GlobalScreen.addNativeMouseWheelListener(this);
        scrollAmount = 0;
    }

    @Override
    public void nativeMouseWheelMoved(NativeMouseWheelEvent nativeMouseWheelEvent) {
        this.scrollAmount += nativeMouseWheelEvent.getWheelRotation();
    }

    //getter methods
    public int getScrollAmount() {
        return scrollAmount;
    }
    //setter methods

    public void setScrollAmount(int scrollAmount) {
        this.scrollAmount = scrollAmount;
    }
}
