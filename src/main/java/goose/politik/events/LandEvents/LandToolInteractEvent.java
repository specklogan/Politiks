package goose.politik.events.LandEvents;

import goose.politik.Politik;
import goose.politik.player.PolitikPlayer;
import goose.politik.util.landUtil.Land;
import goose.politik.util.landUtil.LandUtil;
import goose.politik.menu.LandMenu;
import goose.politik.util.text.TextUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
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

import java.util.Objects;

public class LandToolInteractEvent implements Listener {

    private void playSound(PolitikPlayer player) {
        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 1);
    }

    private void openLandMenu(PlayerInteractEvent event, PolitikPlayer player) {
        Land land = LandUtil.blockInLand(Objects.requireNonNull(event.getClickedBlock()));
        if (land != null) {
            if (player.getPlayer().isOp() || land.getPlayerOwner() == player) {
                //shift key was held down open up the landMenuGui
                LandMenu menu = new LandMenu(player);
                Politik.getInstance().getServer().getPluginManager().registerEvents(menu, Politik.getInstance());
                menu.openInventory(player.getPlayer());
                menu.setLand(land);
            }
        }
    }

    private void displayLeftClickStats(PlayerInteractEvent event, PolitikPlayer player) {
        Block playerLoc = player.getPlayer().getLocation().getBlock();
        Land land = LandUtil.blockInLand(playerLoc);
        if (land != null) {
            player.message(TextUtil.detailMessage("Nation: " + land.getPlayerOwner().getNation().getNationName()).append(Component.text(" Town: " + land.getTownOwner().getTownName()).color(TextColor.color(49, 125, 53)).append(Component.text(" Land: " + land.getPlayerOwner().getDisplayName()).color(TextColor.color(36, 8, 94)))));
            //player.message(Politik.detailMessage("Land Claim: owned by " + land.getPlayerOwner().getDisplayName() + " : " + land.getUUID() + " area of: " + land.getArea() + " in the town of: " + land.getTownOwner().getTownName() + " in the nation: " + land.getNationOwner().getNationName() + ", it is type: " + land.getType()));
        } else {
            player.message(TextUtil.errorMessage("You aren't in any land claims"));
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onClaimToolUse(PlayerInteractEvent event) {
        PolitikPlayer player = PolitikPlayer.getPolitikPlayer(event.getPlayer());
        ItemStack handItem = player.getInventory().getItemInMainHand();

        Block clickedBlock = event.getClickedBlock();

        if (!handItem.getType().equals(Material.STICK)) {
            return;
        }

        //only run for one hand
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }

        String itemType = handItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Politik.getInstance(), "name"), PersistentDataType.STRING);
        if (itemType != "serverClaimTool" && itemType != "landClaimTool") {
            return;
        }

        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            displayLeftClickStats(event, player);
            return;
        }

        if (player.getPlayer().isSneaking() && clickedBlock != null) {
            openLandMenu(event, player);
            return;
        }

        if (Objects.equals(handItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Politik.getInstance(), "name"), PersistentDataType.STRING), "landClaimTool")) {
            String posOneStr = handItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Politik.getInstance(), "posOne"), PersistentDataType.STRING);

            if (clickedBlock == null) {
                return;
            }
            Location blockLocation = clickedBlock.getLocation();
            String x = String.valueOf(blockLocation.getBlockX());
            String y = String.valueOf(blockLocation.getBlockY());
            String z = String.valueOf(blockLocation.getBlockZ());
            Chunk chunk = clickedBlock.getChunk();

            if (posOneStr == null || posOneStr.equals("")) {
                //hasn't used it before

                String locationStr = x + "," + y + "," + z;
                ItemMeta temp = handItem.getItemMeta();
                temp.getPersistentDataContainer().set(new NamespacedKey(Politik.getInstance(), "posOne"), PersistentDataType.STRING,locationStr);
                handItem.setItemMeta(temp);
                playSound(player);

            } else {
                playSound(player);
                //has used before, set second value
                ItemMeta temp = handItem.getItemMeta();
                String locationStr = x + "," + y + "," + z;
                new Land(handItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Politik.getInstance(), "posOne"), PersistentDataType.STRING), locationStr, player, chunk);
                temp.getPersistentDataContainer().set(new NamespacedKey(Politik.getInstance(), "posOne"), PersistentDataType.STRING,"");
                handItem.setItemMeta(temp);
            }
        }
    }
}
