package org.gooseapple.politiks.item;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.gooseapple.politiks.Politiks;
import org.gooseapple.politiks.util.Constants;

public class ClaimTool {
    public static ItemStack GetItem() {
        ItemStack stickItem = new ItemStack(Material.STICK);
        ItemMeta stickMeta = stickItem.getItemMeta();

        stickMeta.getPersistentDataContainer().set(new NamespacedKey(Politiks.getInstance(), Constants.FirstPosition), PersistentDataType.STRING,"");
        stickMeta.getPersistentDataContainer().set(new NamespacedKey(Politiks.getInstance(), Constants.Name), PersistentDataType.STRING,Constants.LandClaimTool);
        stickMeta.getPersistentDataContainer().set(new NamespacedKey(Politiks.getInstance(), Constants.CustomItem), PersistentDataType.BOOLEAN, Boolean.TRUE);
        stickMeta.setMaxStackSize(1);
        stickMeta.addEnchant(Enchantment.CHANNELING, 1, true);
        stickMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        TextComponent component = Component.text("Land Claim Tool").decoration(TextDecoration.ITALIC, false).color(TextColor.color(255, 255, 0));
        stickMeta.displayName(component);
        stickItem.setItemMeta(stickMeta);
        return stickItem;
    }
}
