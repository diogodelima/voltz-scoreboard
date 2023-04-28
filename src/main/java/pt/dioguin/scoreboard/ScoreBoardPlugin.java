package pt.dioguin.scoreboard;

import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import pt.dioguin.scoreboard.listener.PlayerListener;
import pt.dioguin.scoreboard.user.manager.UserManager;

import java.util.logging.Logger;

public final class ScoreBoardPlugin extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");
    private static Economy econ = null;
    private static PlayerPoints playerPoints;
    private UserManager userManager;

    @Override
    public void onEnable() {

        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (!hookPlayerPoints()){
            log.severe(String.format("[%s] - Disabled due to no PlayerPoints dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        saveDefaultConfig();

        userManager = new UserManager();
        userManager.runTaskTimer(this, 0, 20);

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }

    @Override
    public void onDisable() {

    }

    private boolean hookPlayerPoints() {
        final Plugin plugin = this.getServer().getPluginManager().getPlugin("PlayerPoints");
        playerPoints = (PlayerPoints) plugin;
        return playerPoints != null;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static ScoreBoardPlugin getInstance(){
        return getPlugin(ScoreBoardPlugin.class);
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public static Economy getEcon() {
        return econ;
    }

    public static PlayerPoints getPlayerPoints() {
        return playerPoints;
    }
}
