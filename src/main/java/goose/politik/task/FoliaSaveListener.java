package goose.politik.task;

import goose.politik.Politik;

public class FoliaSaveListener implements Runnable{
    private static FoliaSaveListener instance = new FoliaSaveListener();
    public static FoliaSaveListener getInstance() {
        return instance;
    }
    @Override
    public void run() {
        Politik.getInstance().saveServer();
    }
}
