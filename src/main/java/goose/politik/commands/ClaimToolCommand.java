package goose.politik.commands;

import goose.politik.Politik;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ClaimToolCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Politik.errorMessage("Server cannot get the claim chunk stick."));
            return true;
        }

        //give the player a stick tool
        //basic for now
        Player player = (Player) sender;
        ItemStack stickItem = new ItemStack(Material.STICK);
        ItemMeta stickMeta = stickItem.getItemMeta();
        stickMeta.getPersistentDataContainer().set(new NamespacedKey(Politik.getInstance(), "posOne"), PersistentDataType.STRING,"");
        stickMeta.getPersistentDataContainer().set(new NamespacedKey(Politik.getInstance(), "name"), PersistentDataType.STRING,"landClaimTool");
        stickMeta.setUnbreakable(true);

        //set stick text
        TextComponent component = Component.text("Land Claim Tool").decoration(TextDecoration.ITALIC, false).color(TextColor.color(255, 255, 0));
        stickMeta.displayName(component);
        stickItem.setItemMeta(stickMeta);
        player.getInventory().addItem(stickItem);

        return true;
    }
}
