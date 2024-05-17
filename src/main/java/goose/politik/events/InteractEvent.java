package goose.politik.events;

import goose.politik.permissions.PermissionHandler;
import goose.politik.player.PolitikPlayer;
import goose.politik.util.landUtil.Land;
import goose.politik.util.landUtil.LandUtil;
import goose.politik.util.text.TextUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InteractEvent {

    public static void playerInteract(PlayerInteractEvent event) {
        PolitikPlayer player = PolitikPlayer.getPolitikPlayer(event.getPlayer());
        ItemStack handItem = player.getInventory().getItemInMainHand();
        Block interactedBlock =  event.getClickedBlock();
        if (interactedBlock == null || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            return;
        }
        Land land = LandUtil.blockInLand(interactedBlock);
        Material interactedType = interactedBlock.getType();

        if (PermissionHandler.PlayerCanInteract(player, land)) {
            return;
        }

        if (interactedType.toString().contains("BUCKET") && !(interactedType == Material.BUCKET || interactedType == Material.MILK_BUCKET)) {
            event.setCancelled(true);
        }

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (handItem.getType().toString().contains("EGG")) {
                event.setCancelled(true);
            }
        }

        //check if it is farmland
        if ((interactedType == Material.FARMLAND || interactedType == Material.WHEAT) && event.getAction() == Action.PHYSICAL) {
            event.setCancelled(true);
        }

        //prevent player from using doors, crafting tables, chests, etc
        if (interactedType.toString().contains("DOOR") || interactedType == Material.CHEST || interactedType == Material.BARREL || interactedType == Material.FURNACE || interactedType == Material.BLAST_FURNACE || interactedType == Material.SMOKER || interactedType.toString().contains("SHULKER_BOX") || interactedType.toString().contains("BUTTON") || interactedType == Material.LEVER) {
            event.setCancelled(true);
        }

        if (event.isCancelled()) {
            player.message(TextUtil.errorMessage("You can't interact here!"));
        }
    }
}
