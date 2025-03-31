package org.gooseapple.politiks.event;

import org.gooseapple.politiks.event.implementation.ClaimToolListener;
import org.gooseapple.politiks.event.implementation.LandListener;
import org.gooseapple.politiks.event.implementation.PlayerListener;
import org.gooseapple.politiks.event.implementation.ServerListener;

public class EventManager {

    private static PlayerListener playerListener;
    private static ServerListener serverListener;
    private static ClaimToolListener claimToolListener;
    private static LandListener landListener;

    public static void registerListeners() {
        playerListener = new PlayerListener();
        serverListener = new ServerListener();
        claimToolListener = new ClaimToolListener();
        landListener = new LandListener();
    }

    public static void handleShutdown() {
        serverListener.HandleShutdown();
    }
}
