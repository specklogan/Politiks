package org.gooseapple.politiks.event;

import org.gooseapple.politiks.event.implementation.PlayerListener;
import org.gooseapple.politiks.event.implementation.ServerListener;

public class EventManager {

    private static PlayerListener playerListener;
    private static ServerListener serverListener;

    public static void registerListeners() {
        playerListener = new PlayerListener();
        serverListener = new ServerListener();
    }

    public static void handleShutdown() {
        serverListener.HandleShutdown();
    }
}
