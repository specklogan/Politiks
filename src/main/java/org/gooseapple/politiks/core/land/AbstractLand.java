package org.gooseapple.politiks.core.land;

import org.gooseapple.politiks.core.land.geometry.IClaim;
import org.gooseapple.politiks.core.player.PolitikPlayer;
import org.gooseapple.politiks.core.town.Town;
import org.gooseapple.politiks.util.Constants;

import java.util.UUID;

public abstract class AbstractLand implements ILand {
    protected UUID id;
    protected Constants.LandType type;
    protected Town townOwner;
    protected PolitikPlayer owner;
    protected IClaim claim;

    public AbstractLand(Constants.LandType type, IClaim claim) {
        SetLandType(type);
        this.claim = claim;
        this.id = UUID.randomUUID();
    }

    @Override
    public Town GetTownOwner() {
        return this.townOwner;
    }

    @Override
    public PolitikPlayer GetPlayerOwner() {
        return this.owner;
    }

    @Override
    public UUID GetID() {
        return id;
    }

    @Override
    public Constants.LandType GetLandType() {
        return type;
    }

    @Override
    public void SetLandType(Constants.LandType type) {
        this.type = type;
    }

    @Override
    public IClaim GetClaim() {
        return claim;
    }

    @Override
    public void SetClaim(IClaim claim) {
        this.claim = claim;
    }

    @Override
    public void SetOwner(PolitikPlayer player) {
        this.owner = player;
    }

    @Override
    public void SetTownOwner(Town town) {
        this.townOwner = town;
    }
}
