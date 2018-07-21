package dk.minenation.minenationlobby.util;

import org.bukkit.ChatColor;

public class MNUtil {

    public static String color(String in) {
        return ChatColor.translateAlternateColorCodes('&', in);
    }

    public static String stripColor(String in) {
        return ChatColor.stripColor(in);
    }
}
