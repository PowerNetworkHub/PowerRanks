package nl.svenar.powerranks.bukkit.data;

import java.util.ArrayList;

public class TablistAnimation {

    private int delay = 0;
    private int tick = 0;
    private int index = 0;
    private ArrayList<String> frames = new ArrayList<String>();

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setFrames(ArrayList<String> frames) {
        this.frames = frames;
    }

    public void update() {
        if (tick >= delay) {
            tick = 0;
            index++;
            if (index >= frames.size()) {
                index = 0;
            }
        }
        tick++;
    }

    public String getCurrentFrame() {
        return frames.get(index);
    }
    
}
