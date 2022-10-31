package helpers;

import io.qameta.allure.Step;

public class Methods {

    @Step("Attempt multiple times until success")
    public static void waitForSuccess(Runnable run, int steps, int pause) {
        for(int i = 0; i < steps; i++) {
            try {
                execStep(i, run);
                break;
            } catch (Exception | AssertionError ae) {
                try {
                    Thread.sleep(pause);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    @Step("Step {i}")
    private static void execStep(int i, Runnable runnable) {
        runnable.run();
    }

}
