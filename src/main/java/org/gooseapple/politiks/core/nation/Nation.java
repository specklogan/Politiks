package org.gooseapple.politiks.core.nation;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.gooseapple.politiks.core.player.PolitikPlayer;
import org.gooseapple.politiks.core.town.Town;
import org.gooseapple.politiks.database.DatabaseManager;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class Nation {
    private TextComponent enterMessage = Component.text("");
    private String nationName;
    private PolitikPlayer leader;
    private UUID id;
    public Nation(UUID id) {
        this.id = id;
    }

    //Political stuff
    private Town capitol;

    public void setEnterMessage(TextComponent enterMessage) {
        this.enterMessage = enterMessage;
    }

    public String getNationName() {
        return this.nationName;
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
    public CopyOnWriteArrayList<PolitikPlayer> getPlayers() {
        return DatabaseManager.getDatabase().getNationTable().GetNationPlayers(this);
    }

    public UUID getId() {
        return this.id;
    }

    public void setLeader(PolitikPlayer p) {
        this.leader = p;
    }
}
