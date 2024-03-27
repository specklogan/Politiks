package goose.politik.events.LandEvents;

import goose.politik.Politik;
import goose.politik.util.government.PolitikPlayer;
import goose.politik.util.landUtil.Land;
import goose.politik.util.landUtil.LandUtil;
import goose.politik.util.menu.LandMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;
import java.util.logging.Level;

public class LandToolInteractEvent implements Listener {

    @EventHandler
    public void onClaimToolUse(PlayerInteractEvent event) {
        PolitikPlayer player = PolitikPlayer.getPolitikPlayer(event.getPlayer());
        ItemStack handItem = player.getInventory().getItemInMainHand();
        Block clickedBlock = event.getClickedBlock();
        if (handItem.getType().equals(Material.STICK)) {
            //only run for one hand
            if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
                return;
            }

            //if they right click a block they are trying to claim it, if they
            //hold shift right then they are gonna open up a menu
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

                //check if shift key was held
                if (player.getPlayer().isSneaking() && clickedBlock != null) {
                    Land land = LandUtil.blockInLand(clickedBlock);
                    if (land != null) {
                        if (player.getPlayer().isOp() || land.getPlayerOwner() == player) {
                            //shift key was held down open up the landMenuGui
                            LandMenu menu = new LandMenu(player);
                            Politik.getInstance().getServer().getPluginManager().registerEvents(menu, Politik.getInstance());
                            menu.openInventory(player.getPlayer());
                            menu.setLand(land);
                            return;
                        }
                    }
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

                    } else {
                        //has used before, set second value
                        ItemMeta temp = handItem.getItemMeta();
                        String locationStr = x + "," + y + "," + z;
                        new Land(handItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Politik.getInstance(), "posOne"), PersistentDataType.STRING), locationStr, player, chunk);
                        temp.getPersistentDataContainer().set(new NamespacedKey(Politik.getInstance(), "posOne"), PersistentDataType.STRING,"");
                        handItem.setItemMeta(temp);
                    }
                }
            } else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                Block playerLoc = player.getPlayer().getLocation().getBlock();
                Land land = LandUtil.blockInLand(playerLoc);
                if (land != null) {
                    player.message(Politik.detailMessage("Nation: " + land.getPlayerOwner().getNation().getNationName()).append(Component.text(" Town: " + land.getTownOwner().getTownName()).color(TextColor.color(49, 125, 53)).append(Component.text(" Land: " + land.getPlayerOwner().getDisplayName()).color(TextColor.color(36, 8, 94)))));
                    //player.message(Politik.detailMessage("Land Claim: owned by " + land.getPlayerOwner().getDisplayName() + " : " + land.getUUID() + " area of: " + land.getArea() + " in the town of: " + land.getTownOwner().getTownName() + " in the nation: " + land.getNationOwner().getNationName() + ", it is type: " + land.getType()));
                } else {
                    player.message(Politik.errorMessage("You aren't in any land claims"));
                }
                event.setCancelled(true);
            }
        }
    }
}
