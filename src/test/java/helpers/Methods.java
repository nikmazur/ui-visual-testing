package helpers;

public class Methods {

    public static void waitForSuccess(Runnable run, int steps, int pause) {
        boolean success = false;
        for(int i = 0; i < steps - 1; i++) {
            try {
                run.run();
                success = true;
                break;
            } catch (Exception | AssertionError ae) {
                try {
                    Thread.sleep(pause);
                } catch (InterruptedException ignored) {}
            }
        }
        if(!success) run.run();
    }

}
