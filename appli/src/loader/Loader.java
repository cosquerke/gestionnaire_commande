package loader;

public class Loader {
    private static volatile Loader instance;

    public static Loader getInstance() {
        Loader loader = instance;
        if (null != loader) {
            return loader;
        }
        synchronized (Loader.class) {
            if (instance == null) {
                instance = new Loader();
            }
            return instance;
        }
    }
}
