package edu.gatech.mmccoy37.GraffitiTools.Data;

import edu.gatech.mmccoy37.GraffitiTools.Brushes.Brush;
import edu.gatech.mmccoy37.GraffitiTools.Brushes.DefaultBrush;
import edu.gatech.mmccoy37.GraffitiTools.Tools.DefaultTool;
import edu.gatech.mmccoy37.GraffitiTools.Tools.Tool;
import net.minecraft.server.v1_9_R1.BlockPurpurSlab;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by matt on 5/14/16.
 */

public class PlayerStates {
    private static Map<Player, Tool> activePlayers;
    private static PlayerStates ourInstance = new PlayerStates();

    public static PlayerStates getInstance() {
        return ourInstance;
    }

    private PlayerStates() {
    }


    //new methods

    public static void add(Player player) {
        if (activePlayers == null)
            activePlayers = new HashMap<>();
        activePlayers.putIfAbsent(player, new DefaultTool());
    }

    public static void remove(Player player) {
        if (activePlayers != null) {
            activePlayers.remove(player);
        }
    }

    public static void setTool(Player player, Tool tool) {
        if (activePlayers.get(player) == null) {
            add(player);
        }
        activePlayers.put(player, tool);
    }

    public static boolean hasPlayer(Player player) {

        if (activePlayers == null || player == null) {
            return false;
        } else {
            return activePlayers.containsKey(player);
        }
    }

    public static Tool getTool(Player player) throws NullPointerException {
        if (activePlayers == null) {
            Log.error("error getting tool for " + player.getPlayerListName());
            throw new NullPointerException("active players not instantiated");
        }
        DefaultBrush brush = new DefaultBrush();
        DefaultTool tool = new DefaultTool();
        tool.setBrush(brush);
        return activePlayers.getOrDefault(player, tool);
    }

    public static String getString() {
        if (activePlayers != null)
            return activePlayers.toString();
        return "null";
    }
}
