package org.gooseapple.politiks.core.nation;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.gooseapple.politiks.core.player.PolitikPlayer;
import org.gooseapple.politiks.core.town.Town;

import java.util.ArrayList;

public class Nation {
    private TextComponent enterMessage = Component.text("");
    private String nationName;
    private PolitikPlayer leader;
    private final ArrayList<PolitikPlayer> playerList = new ArrayList<>();

    //Political stuff
    private Town capitol;

    public void setEnterMessage(TextComponent enterMessage) {
        this.enterMessage = enterMessage;
    }

    public String getNationName() {
        return this.nationName;
    }

    public ArrayList<PolitikPlayer> getPlayerList() {
        return playerList;
    }

    public void addPlayer(PolitikPlayer player) {
        this.playerList.add(player);
    }

    public void removePlayer(PolitikPlayer player) {
        this.playerList.remove(player);
    }

    public boolean containsPlayer(PolitikPlayer player) {
        return this.playerList.contains(player);
    }

    public TextComponent getEnterMessage() {
        return this.enterMessage;
    }

    public PolitikPlayer getLeader() {
        return this.leader;
    }

    public void setCapitol(Town capitol) {
        this.capitol = capitol;
    }

    public Town getCapitol() {
        return this.capitol;
    }
}
