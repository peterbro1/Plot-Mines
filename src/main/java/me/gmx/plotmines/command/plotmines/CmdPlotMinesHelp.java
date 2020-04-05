package me.gmx.plotmines.command.plotmines;

import me.gmx.plotmines.PlotMines;
import me.gmx.plotmines.config.Lang;
import me.gmx.plotmines.core.BSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class CmdPlotMinesHelp extends BSubCommand {
    public CmdPlotMinesHelp(){
        this.aliases.add("help");
        this.correctUsage = "/plotmines help";
        this.senderMustBePlayer=false;
        this.permission = "plotmines.help";
    }

    @Override
    public void execute(){
        msg(Lang.PREFIX.toString() + "&6&l----Plot Mine Help----");
        msg(Lang.PREFIX.toString() + "&6/plotmine help " + "&9- Displays this menu.");
        msg(Lang.PREFIX.toString() + "&6/plotmine give [player] [tier] [size] &9- Gives a player the specified plotmine tier at the specified size.");
        msg(Lang.PREFIX.toString() + "&6/plotmine list &9- Lists all current loaded plotmine tiers.");
        msg(Lang.PREFIX.toString() + "&6/plotmine reload &9- Reloads config.");
        msg(Lang.PREFIX.toString() + "&6/plotmine reset &9- Refills the current mine if you're standing inside it.");
       /* msg(Lang.PREFIX.toString() + "&6/pkoth resume " + "&9- Starts the countdown to next KOTH");
        msg(Lang.PREFIX.toString() + "&6/pkoth stop " + "&9- Attempts to stop the current KOTH (silently).");
        msg(Lang.PREFIX.toString() + "&6/pkoth create [arena2] [region2] " + "&9- Creates a KOTH arena with the given name from the given Worldguard region.");
        msg(Lang.PREFIX.toString() + "&6/pkoth list " + "&9- Lists all loaded KOTH arenas.");
        msg(Lang.PREFIX.toString() + "&6/pkoth reload " + "&9- Reloads config values.");
        msg(Lang.PREFIX.toString() + "&6/pkoth remove [arena2] " + "&9- Removes a given KOTH arena.");
        msg(Lang.PREFIX.toString() + "&6/pkoth loot " + "&9- Opens the loot menu.");
        msg(Lang.PREFIX.toString() + "&6/pkoth reset " + "&9- Sets time until next KOTH to default.");
        msg(Lang.PREFIX.toString() + "&6/pkoth set [5h32m55s]" + "&9- Sets time to next koth");*/
        msg(Lang.PREFIX.toString() + "&4&lVersion: " + ChatColor.AQUA + PlotMines.getInstance().getDescription().getVersion());

    }
}
