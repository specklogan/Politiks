package org.gooseapple.politiks.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class Constants {
    public static String UUID = "UUID";
    public static String AccountID = "AccountID";
    public static String Name = "Name";
    public static String Leader = "Leader";
    public static String Mayor = "Mayor";
    public static String Capitol = "Capitol";
    public static String FirstPosition = "PositionOne";
    public static String SecondPosition = "PositionTwo";
    public static String CustomItem = "CustomItem";
    public static String LandClaimTool = "LandClaimTool";
    public static String Land = "LandID";
    public static String Chunks = "Chunks";
    public static String Type = "Type";

    public static enum DatabaseType {
        MONGO,
        SQLITE //Not supported yet
    }

    public static enum LandType {
        DEFAULT,
        TICKABLE
    }

    public static enum ClaimGeometry {
        RECTANGLE,
        CIRCLE
    }

    public static TextComponent GreenMessage(String text) {
        return Component.text(text).color(TextColor.color(15, 120, 21));
    }

    public static TextComponent ErrorMessage(String text) {
        return Component.text(text).color(TextColor.color(255, 0, 0));
    }

    public static TextComponent WarningMessage(String text) {
        return Component.text(text).color(TextColor.color(255, 130, 17)).decorate(TextDecoration.ITALIC);
    }

    public static TextComponent EventMessage(String text) {
        return Component.text(text).color(TextColor.color(255, 255, 0));
    }

    public static TextComponent SuccessMessage(String text) {
        return Component.text(text).color(TextColor.color(62, 255, 54));
    }

    public static TextComponent DetailMessage(String text) {
        return Component.text(text).color(TextColor.color(84, 200, 255));
    }

    public static TextComponent InfoMessage(String text) {
        return Component.text(text).color(TextColor.color(255, 255, 255));
    }
}
