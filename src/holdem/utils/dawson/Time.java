package holdem.utils.dawson;

public class Time {

    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}
