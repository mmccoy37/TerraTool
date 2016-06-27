package edu.gatech.mmccoy37.GraffitiTools;

import edu.gatech.mmccoy37.GraffitiTools.Commands.CommandCore;
import edu.gatech.mmccoy37.GraffitiTools.Data.PlayerStates;
import edu.gatech.mmccoy37.GraffitiTools.Listener.Draw;
import edu.gatech.mmccoy37.GraffitiTools.Objects.VoidMaterials;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by matt on 5/14/16.
 */
public class Main extends JavaPlugin {

    public static final String PLUGIN_NAME = "GraffitiTools";
    @Override
    public void onEnable() {
        super.onEnable();
        PlayerStates.getInstance();
//        VoidMaterials.
        this.getCommand("graffititools").setExecutor(new CommandCore());
        getServer().getPluginManager().registerEvents(new Draw(), this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
