package goose.politik.util.menu;

import goose.politik.Politik;
import goose.politik.util.player.PolitikPlayer;
import goose.politik.util.landUtil.Land;
import goose.politik.util.landUtil.LandConverter;
import goose.politik.util.landUtil.lands.Farm;
import goose.politik.util.text.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;
import java.util.logging.Level;

public class LandMenu implements Listener {
    private Inventory inventory;
    private Land land;

    public Land getLand() {
        return land;
    }

    public void setLand(Land land) {
        this.land = land;
    }

    private int layer = 0;

    public LandMenu(PolitikPlayer player) {
        this.inventory = Bukkit.createInventory(player.getPlayer(), 27, TextUtil.detailMessage("Land Menu"));
        addItems();
    }

    public void openInventory(HumanEntity player) {
        player.openInventory(this.inventory);

    }

    public void addItems() {
        this.inventory.clear();
        if (this.layer == 0) {
            //clear any items in this inventory if there are any
            //populate the inventory with custom items that will have their own effect
            ItemStack closeButton = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            ItemMeta closeMeta = closeButton.getItemMeta();
            closeMeta.displayName(TextUtil.errorMessage("Close Menu"));
            closeButton.setItemMeta(closeMeta);

            ItemStack blankButton = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta blankMeta = blankButton.getItemMeta();
            blankMeta.displayName(TextUtil.infoMessage(""));
            blankButton.setItemMeta(blankMeta);

            ItemStack productionType = new ItemStack(Material.CARTOGRAPHY_TABLE);
            ItemMeta productionMeta = productionType.getItemMeta();
            productionMeta.displayName(TextUtil.detailMessage("Land Production Type"));
            productionType.setItemMeta(productionMeta);

            this.inventory.setItem(26,closeButton);
            this.inventory.setItem(25,blankButton);
            this.inventory.setItem(24,blankButton);
            this.inventory.setItem(22,blankButton);
            this.inventory.setItem(20,blankButton);
            this.inventory.setItem(19,blankButton);
            this.inventory.setItem(18,blankButton);
            this.inventory.setItem(0,productionType);
        } else if (this.layer == 1) {
            ItemStack farm = new ItemStack(Material.DIAMOND_HOE);
            ItemMeta farmMeta = farm.getItemMeta();
            farmMeta.displayName(TextUtil.detailMessage("Convert To Farm"));
            farm.setItemMeta(farmMeta);
            this.inventory.setItem(0,farm);
        }
    }

    @EventHandler
    public void onGuiClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == this.inventory || event.isShiftClick()) {
            //prevent players from clicking
            if (event.getCurrentItem() != null) {
                ItemMeta itemMeta = event.getCurrentItem().getItemMeta();
                if (Objects.equals(itemMeta.displayName(), TextUtil.errorMessage("Close Menu"))) {
                    closeGui();
                } else if (Objects.equals(itemMeta.displayName(), TextUtil.detailMessage("Land Production Type"))) {
                    return; //will have to do later
                    //this.layer++;
                    //addItems();
                } else if (Objects.equals(itemMeta.displayName(), TextUtil.detailMessage("Convert To Farm"))) {
                    //call the function which converts it
                    closeGui();
                    PolitikPlayer player = PolitikPlayer.getPolitikPlayer((Player) Objects.requireNonNull(this.inventory.getHolder()));
                    Farm farm = new Farm();
                    farm = (Farm) LandConverter.convertLand(this.land, farm);
                    Politik.logger.log(Level.INFO, "FARM: " + farm);
                    player.message(TextUtil.detailMessage("Successfully converted to Farm"));
                }
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onGuiDrag(InventoryDragEvent event) {
        if (event.getInventory() == this.inventory) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onGuiClose(InventoryCloseEvent event) {
        closeGui();
    }

    public void closeGui() {
        HandlerList.unregisterAll(this);
        this.inventory.close();
    }
}
