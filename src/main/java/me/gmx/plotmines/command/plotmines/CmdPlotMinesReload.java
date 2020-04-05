package me.gmx.plotmines.command.plotmines;

import me.gmx.plotmines.PlotMines;
import me.gmx.plotmines.config.Lang;
import me.gmx.plotmines.core.BSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class CmdPlotMinesReload extends BSubCommand {
    public CmdPlotMinesReload(){
        this.aliases.add("reload");
        this.correctUsage = "/plotmines reload";
        this.senderMustBePlayer=false;
        this.permission = "plotmines.reload";
    }

    @Override
    public void execute(){
        PlotMines.getInstance().reloadMineConfig();
        PlotMines.getInstance().reloadConfig();
        PlotMines.getInstance().reloadContentConfig();
        msg(Lang.MSG_CONFIG_RELOADED.toMsg());
    }
}
