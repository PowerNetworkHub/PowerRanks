package nl.svenar.powerranks.test.util;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class TestDebugger {

    private static boolean DEBUG = true;
    
    public static boolean DOPERFORMANCETEST = false;

    private static Instant startTime;

    private static String lastClassName = "";

    public static void log(Object clazz, String message) {
        if (!DEBUG)
            return;

        if (Objects.isNull(startTime) || !clazz.getClass().getSimpleName().equals(lastClassName)) {
            lastClassName = clazz.getClass().getSimpleName();
            startTime = Instant.now();
        }

        System.out.println("[" + String.format("%04d", Duration.between(startTime, Instant.now()).toMillis()) + "ms] ["
                + clazz.getClass().getSimpleName() + "] " + message);
    }
}