package pt.dioguin.scoreboard.user.manager;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pt.dioguin.scoreboard.user.User;

import java.util.ArrayList;
import java.util.List;

public class UserManager extends BukkitRunnable {

    private final List<User> users;

    public UserManager(){
        this.users = new ArrayList<>();
    }

    public boolean hasUser(Player player){
        return this.users.stream().anyMatch(user -> user.getPlayer().getName().equals(player.getName()));
    }

    public User getUser(Player player){
        return this.users.stream().filter(user -> user.getPlayer().getName().equals(player.getName())).findAny().orElse(null);
    }

    public List<User> getUsers() {
        return users;
    }

    @Override
    public void run() {
        this.users.forEach(User::updateBoard);
    }
}
