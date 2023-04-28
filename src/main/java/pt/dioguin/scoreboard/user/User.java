package pt.dioguin.scoreboard.user;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import pt.dioguin.scoreboard.ScoreBoardPlugin;
import pt.dioguin.scoreboard.fastboard.FastBoard;
import pt.dioguin.scoreboard.utils.Formatter;

import java.util.stream.Collectors;

public class User {

    private final Player player;
    private final FastBoard board;

    public User(Player player){
        this.player = player;
        this.board = new FastBoard(player);
        ScoreBoardPlugin.getInstance().getUserManager().getUsers().add(this);
        updateBoard();
    }

    public void updateBoard(){

        FileConfiguration config = ScoreBoardPlugin.getInstance().getConfig();
        MPlayer mPlayer = MPlayer.get(this.player);
        Faction faction = BoardColl.get().getFactionAt(PS.valueOf(this.player.getLocation()));
        String worldName = config.contains("scoreboard." + this.player.getWorld().getName()) ? this.player.getWorld().getName() : "default";
        String coins = new Formatter().formatNumber(ScoreBoardPlugin.getEcon().getBalance(this.player));
        String cash = new Formatter().formatNumber(ScoreBoardPlugin.getPlayerPoints().getAPI().look(this.player.getUniqueId()));
        String currentPower = String.valueOf((int) mPlayer.getPower());
        String maxPower = String.valueOf((int) mPlayer.getPowerMax());

        this.board.updateTitle(config.getString("scoreboard." + worldName + ".title").replace("&", "§")
                .replace("§2", "§f").replace("§4", "§f").replace("§6", "§f")
                .replace("{coins}", coins).replace("{cash}", cash).replace("{current_power}", currentPower)
                .replace("{power_max}", maxPower).replace("{faction_tag}", faction.getTag()).replace("{faction_name}", faction.getName()));

        if (mPlayer.hasFaction())
            this.board.updateLines(
                    config.getStringList("scoreboard." + worldName + ".lines").stream().map(str -> str.replace("&", "§")
                            .replace("{coins}", coins).replace("{cash}", cash).replace("{current_power}", currentPower)
                            .replace("{power_max}", maxPower).replace("{faction_tag}", faction.getTag()).replace("{faction_name}", faction.getName())
                            .replace("{your_faction_name}", mPlayer.getFactionName()).replace("{your_faction_tag}", mPlayer.getFactionTag())
                            .replace("{your_faction_current_online}", String.valueOf(mPlayer.getFaction().getOnlinePlayers().size()))
                            .replace("{your_faction_max_online}", String.valueOf(mPlayer.getFaction().getMPlayers().size()))
                            .replace("{your_faction_current_power}", String.valueOf((int) mPlayer.getFaction().getPower()))
                            .replace("{your_faction_power_max}", String.valueOf((int) mPlayer.getFaction().getPowerMax()))
                            .replace("{your_faction_land_count}", String.valueOf(mPlayer.getFaction().getLandCount())))
                            .collect(Collectors.toList())
            );
        else
            this.board.updateLines(
                    config.getStringList("scoreboard." + worldName + ".no-faction.lines").stream().map(str -> str.replace("&", "§")
                                    .replace("{coins}", coins).replace("{cash}", cash).replace("{current_power}", currentPower)
                                    .replace("{power_max}", maxPower).replace("{faction_tag}", faction.getTag()).replace("{faction_name}", faction.getName())
                                    .replace("{your_faction_name}", mPlayer.getFactionName()).replace("{your_faction_tag}", mPlayer.getFactionTag())
                                    .replace("{your_faction_current_online}", String.valueOf(mPlayer.getFaction().getOnlinePlayers().size()))
                                    .replace("{your_faction_max_online}", String.valueOf(mPlayer.getFaction().getMPlayers().size()))
                                    .replace("{your_faction_current_power}", String.valueOf((int) mPlayer.getFaction().getPower()))
                                    .replace("{your_faction_power_max}", String.valueOf((int) mPlayer.getFaction().getPowerMax()))
                                    .replace("{your_faction_land_count}", String.valueOf(mPlayer.getFaction().getLandCount())))
                            .collect(Collectors.toList())
            );

    }

    public void delete(){
        this.board.delete();
        ScoreBoardPlugin.getInstance().getUserManager().getUsers().remove(this);
    }

    public Player getPlayer() {
        return player;
    }
}
