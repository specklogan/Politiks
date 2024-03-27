package goose.politik.events;

import goose.politik.Politik;
import goose.politik.util.government.PolitikPlayer;
import goose.politik.util.landUtil.Land;
import goose.politik.util.landUtil.LandUtil;
import org.bukkit.Chunk;
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
import java.util.logging.Level;

public class InteractEvent {

    public static void playerInteract(PlayerInteractEvent event) {
        PolitikPlayer player = PolitikPlayer.getPolitikPlayer(event.getPlayer());
        ItemStack handItem = player.getInventory().getItemInMainHand();

        Block interactedBlock =  event.getClickedBlock();
            if (interactedBlock == null || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            return;
        }
        Material interactedType = interactedBlock.getType();

        if (interactedType.toString().contains("BUCKET") && !(interactedType == Material.BUCKET || interactedType == Material.MILK_BUCKET)) {
            Land land = LandUtil.blockInLand(interactedBlock);
            if (land != null) {
                if (!player.getPlayer().isOp()) {
                    if (land.getPlayerOwner() != player) {
                        //player trying to place bucket
                        player.message(Politik.errorMessage("You can't interact here"));
                        event.setCancelled(true);
                    }
                }
            }
        }

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (handItem.getType().toString().contains("EGG")) {
                if (player.getPlayer().isOp()) {
                    return;
                }
            }
        }

        //check if it is farmland
        if ((interactedType == Material.FARMLAND || interactedType == Material.WHEAT) && event.getAction() == Action.PHYSICAL) {
            event.setCancelled(true);
        }

        //prevent player from using doors, crafting tables, or chests
        if (interactedType.toString().contains("DOOR") || interactedType == Material.CHEST || interactedType == Material.BARREL || interactedType == Material.FURNACE || interactedType == Material.BLAST_FURNACE || interactedType == Material.SMOKER || interactedType.toString().contains("SHULKER_BOX") || interactedType.toString().contains("BUTTON") || interactedType == Material.LEVER) {
            //check if any of the special blocks are valid to be used
            Land land = LandUtil.blockInLand(interactedBlock);
            if (land != null) {
                if (!player.getPlayer().isOp()) {
                    if (land.getPlayerOwner() != player) {
                        player.message(Politik.errorMessage("You can't interact here"));
                        event.setCancelled(true);
                    }
                }
            }
        }


    }
}
