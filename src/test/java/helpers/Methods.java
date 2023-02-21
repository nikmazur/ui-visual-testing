package helpers;

import java.util.concurrent.locks.LockSupport;

public class Methods {

    public static void waitForSuccess(Runnable run, int steps, int pause) {
        boolean success = false;
        for(int i = 0; i < steps - 1; i++) {
            try {
                run.run();
                success = true;
                break;
            } catch (Exception | AssertionError ae) {
                LockSupport.parkNanos(pause);
            }
        }
        if(!success) run.run();
    }

}
