package pt.dioguin.scoreboard.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pt.dioguin.scoreboard.ScoreBoardPlugin;
import pt.dioguin.scoreboard.user.User;
import pt.dioguin.scoreboard.user.manager.UserManager;

public class PlayerListener implements Listener {

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event){

        Player player = event.getPlayer();
        UserManager userManager = ScoreBoardPlugin.getInstance().getUserManager();

        if (!userManager.hasUser(player))
            new User(player);

    }

    @EventHandler
    void onPlayerQuit(PlayerQuitEvent event){

        Player player = event.getPlayer();
        UserManager userManager = ScoreBoardPlugin.getInstance().getUserManager();
        User user = userManager.hasUser(player) ? userManager.getUser(player) : new User(player);
        user.delete();

    }

}
