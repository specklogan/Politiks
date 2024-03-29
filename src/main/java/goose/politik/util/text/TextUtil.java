package goose.politik.util.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;

public class TextUtil {
    public static TextComponent errorMessage(String text) {
        return Component.text(text).color(TextColor.color(255, 0, 0));
    }

    public static TextComponent eventMessage(String text) {
        return Component.text(text).color(TextColor.color(255, 255, 0));
    }

    public static TextComponent successMessage(String text) {
        return Component.text(text).color(TextColor.color(62, 255, 54));
    }

    public static TextComponent detailMessage(String text) {
        return Component.text(text).color(TextColor.color(84, 200, 255));
    }

    public static TextComponent infoMessage(String text) {
        return Component.text(text).color(TextColor.color(255, 255, 255));
    }
}
