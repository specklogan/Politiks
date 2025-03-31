package org.gooseapple.politiks.event.implementation;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.gooseapple.politiks.Politiks;
import org.gooseapple.politiks.config.ConfigManager;
import org.gooseapple.politiks.core.land.ILand;
import org.gooseapple.politiks.core.land.geometry.ClaimRectangle;
import org.gooseapple.politiks.core.land.types.Land;
import org.gooseapple.politiks.core.player.PolitikPlayer;
import org.gooseapple.politiks.database.DatabaseManager;
import org.gooseapple.politiks.database.ILandTable;
import org.gooseapple.politiks.database.IPlayerTable;
import org.gooseapple.politiks.ui.LandUI;
import org.gooseapple.politiks.util.Constants;
import org.gooseapple.politiks.util.LocationUtil;

import java.math.BigDecimal;

public class ClaimToolListener implements Listener {
    private IPlayerTable players;
    private ILandTable lands;
    public ClaimToolListener() {
        players = DatabaseManager.getDatabase().getPlayerTable();
        lands = DatabaseManager.getDatabase().getLandTable();
        Politiks.getInstance().getServer().getPluginManager().registerEvents(this, Politiks.getInstance());
    }

    private boolean WasClaimToolUsed(ItemStack item) {
        boolean customItem = item.getPersistentDataContainer().has(new NamespacedKey(Politiks.getInstance(), Constants.CustomItem), PersistentDataType.BOOLEAN);
        if (!customItem) {
            return false;
        }

        String name = item.getPersistentDataContainer().get(new NamespacedKey(Politiks.getInstance(), Constants.Name), PersistentDataType.STRING);
        return Constants.LandClaimTool.equalsIgnoreCase(name);
    }

    private void HandleLeftClick(PlayerInteractEvent event, PolitikPlayer player) {
        event.setCancelled(true);
        if (event.getClickedBlock() == null) {
            return;
        }
        ILand land = lands.GetLandFromLocation(event.getClickedBlock().getLocation());
        player.message(Constants.DetailMessage("You are currently in a claim owned by " + land.GetPlayerOwner().getDisplayName()));
    }

    private void OpenLandGUI(PolitikPlayer p) {
        LandUI ui = new LandUI();
        ui.CreateGUI();
        ui.ShowGUI(p);
    }

    private void playSound(PolitikPlayer player) {
        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 1);
    }

    private void HandleCreation(String firstLocation, String secondLocation, PolitikPlayer player) {
        Location firstLoc = LocationUtil.Deserialize(firstLocation);
        Location secondLoc = LocationUtil.Deserialize(secondLocation);

        ClaimRectangle claim = new ClaimRectangle(firstLoc, secondLoc);

        player.message(Constants.DetailMessage(claim.GetContainingChunks().toString()));

        //Check player balance
        BigDecimal cost = ConfigManager.getCostPerArea().multiply(new BigDecimal(claim.GetArea()));

        if (!player.GetAccount().canWithdraw(cost)) {
            player.message(Constants.ErrorMessage("You lack funds to purchase this claim, you only have " + player.GetAccount().getBalanceFormatted() + " but require $" + cost.toString()));
            return;
        }

        //Check that the claim size meets the minimum block requirements set by the config
        if (claim.GetArea() >= ConfigManager.getMinLandSize() && claim.GetArea() <= ConfigManager.getMaxLandSize()) {
            Land land = new Land(Constants.LandType.DEFAULT, claim);
            land.SetOwner(player);
            land.SetTownOwner(player.getTown());
            //Check if the land exists in another claim
            boolean overlaps = lands.IsLandOverlapping(land);

            if (overlaps) {
                player.message(Constants.ErrorMessage("Your claim is overlapping an existing claim!"));
                return;
            }

            lands.InsertNewLand(land);
            player.message(Constants.SuccessMessage("Land claim successful, " + player.getTown().getTownName() + " grows stronger."));


        } else {
            player.message(Constants.ErrorMessage("The claim is too big/small, the minimum size is " + ConfigManager.getMinLandSize() + " blocks, and the maximum size is " + ConfigManager.getMaxLandSize() + " blocks"));
        }
    }

    @EventHandler
    public void InteractEvent(PlayerInteractEvent event) {
        PolitikPlayer player = players.GetPlayer(event.getPlayer());
        ItemStack handItem = player.getInventory().getItemInMainHand();
        Block clickedBlock = event.getClickedBlock();

        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }

        if (!WasClaimToolUsed(handItem)) {
            return;
        }

        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            HandleLeftClick(event, player);
            return;
        }

        if (player.getPlayer().isSneaking() && clickedBlock != null) {
            OpenLandGUI(player);
            return;
        }

        if (clickedBlock == null) {
            return;
        }
        String firstPositionString = handItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Politiks.getInstance(), Constants.FirstPosition), PersistentDataType.STRING);

        Location blockLocation = clickedBlock.getLocation();
        String serializedLocation = LocationUtil.Serialize(blockLocation);

        if (firstPositionString == null || firstPositionString.isEmpty()) { //First position hasn't been set yet
            ItemMeta temp = handItem.getItemMeta();
            temp.getPersistentDataContainer().set(new NamespacedKey(Politiks.getInstance(), Constants.FirstPosition), PersistentDataType.STRING, serializedLocation);
            handItem.setItemMeta(temp);
            playSound(player);
        } else {
            playSound(player);
            //has used before, set second value
            ItemMeta temp = handItem.getItemMeta();
            String firstLocation = temp.getPersistentDataContainer().get(new NamespacedKey(Politiks.getInstance(), Constants.FirstPosition), PersistentDataType.STRING);

            HandleCreation(firstLocation, serializedLocation, player);

            //Clear the old data
            temp.getPersistentDataContainer().set(new NamespacedKey(Politiks.getInstance(), Constants.FirstPosition), PersistentDataType.STRING,"");
            handItem.setItemMeta(temp);
        }

    }
}
