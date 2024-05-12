package goose.politik.util.player;

import com.nametagedit.plugin.NametagEdit;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;

public class PlayerDisplay {
    public static void SetPlayerPrefix(PolitikPlayer player, String prefix) {
        NametagEdit.getApi().setPrefix(player.getPlayer(), prefix);
    }

    public static void SetPlayerPrefix(Player player, String prefix) {
        NametagEdit.getApi().setPrefix(player, prefix);

    }
}
