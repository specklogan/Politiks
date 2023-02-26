package goose.politik.events;

import goose.politik.Politik;
import goose.politik.util.government.PolitikPlayer;
import goose.politik.util.landUtil.Land;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import java.util.Objects;

public class InteractEvent {
    public static void playerInteract(PlayerInteractEvent event) {
        PolitikPlayer player = PolitikPlayer.getPolitikPlayer(event.getPlayer());
        ItemStack handItem = player.getInventory().getItemInMainHand();

        //only run for one hand
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }

        if (handItem.getType().equals(Material.STICK)) {
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (Objects.equals(handItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Politik.getInstance(), "name"), PersistentDataType.STRING), "landClaimTool")) {
                    String posOneStr = handItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Politik.getInstance(), "posOne"), PersistentDataType.STRING);

                    Block clickedBlock = event.getClickedBlock();
                    if (clickedBlock == null) {
                        return;
                    }
                    Location blockLocation = clickedBlock.getLocation();

                    String x = String.valueOf(blockLocation.getBlockX());
                    String z = String.valueOf(blockLocation.getBlockZ());
                    if (posOneStr == null || posOneStr.equals("")) {
                        //hasn't used it before

                        String locationStr = x + "," + z;
                        ItemMeta temp = handItem.getItemMeta();
                        temp.getPersistentDataContainer().set(new NamespacedKey(Politik.getInstance(), "posOne"), PersistentDataType.STRING,locationStr);
                        handItem.setItemMeta(temp);

                    } else {
                        //has used before, set second value
                        ItemMeta temp = handItem.getItemMeta();
                        String locationStr = x + "," + z;
                        Land land = new Land(handItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Politik.getInstance(), "posOne"), PersistentDataType.STRING), locationStr, player);
                        temp.getPersistentDataContainer().set(new NamespacedKey(Politik.getInstance(), "posOne"), PersistentDataType.STRING,"");
                        handItem.setItemMeta(temp);
                    }
                }
            }
        }
    }
}
