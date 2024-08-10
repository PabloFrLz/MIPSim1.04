package MIPS;
import java.util.ArrayList;
import java.util.List;

public class Clock {
    private List<ClockListener> listeners = new ArrayList<>();

    public void addListener(ClockListener listener) {
        listeners.add(listener);
    }

    public void clock() {
        for (ClockListener listener : listeners) {
            listener.clock();
        }
    }
}
