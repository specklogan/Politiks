package org.gooseapple.politiks.ui;

import org.bukkit.Material;
import org.gooseapple.politiks.core.player.PolitikPlayer;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

public class LandUI implements IUserInterface{
    private Gui UI;
    private Window window;
    @Override
    public void CreateGUI() {
        UI = Gui.normal() // Creates the GuiBuilder for a normal GUI
                .setStructure(
                        "# # # # # # # # #",
                        "# . . . . . . . #",
                        "# . . . . . . . #",
                        "# # # # # # # # #")
                .addIngredient('#', new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)))
                .build();
    }

    @Override
    public void ShowGUI(PolitikPlayer player) {
        window = Window.single()
                .setViewer(player.getPlayer())
                .setTitle("Land Management")
                .setGui(UI)
                .build();

        window.open();
    }
}
