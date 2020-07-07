package sjsu.android.alarmclockplusplus;

import java.util.Random;

public enum Direction {
    UP, DOWN, LEFT, RIGHT;

    public static Direction getRandomDirection(){
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
}
