package org.gooseapple.politiks.core.land;

import org.gooseapple.politiks.core.land.geometry.IClaim;
import org.gooseapple.politiks.core.player.PolitikPlayer;
import org.gooseapple.politiks.core.town.Town;
import org.gooseapple.politiks.util.Constants;

import java.util.UUID;

public interface ILand {
    public UUID GetID();
    public Constants.LandType GetLandType();
    public void SetLandType(Constants.LandType type);
    public IClaim GetClaim();
    public void SetClaim(IClaim claim);
    public void SetOwner(PolitikPlayer player);
    public void SetTownOwner(Town town);
}
