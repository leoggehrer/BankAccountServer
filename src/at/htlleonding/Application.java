package at.htlleonding;

public class Application {
    private static Application instance;
    private boolean isRunning = false;
    private String name = "Bank-Account-Server";

    private Application() {

    }
    public static Application getInsatnce() {
        if (instance == null) {
            instance = new Application();
        }
        return instance;
    }

    public static boolean isActive() {
        return instance.isRunning();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
