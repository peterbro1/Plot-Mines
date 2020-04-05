package me.gmx.plotmines.command.plotmines;

import me.gmx.plotmines.PlotMines;
import me.gmx.plotmines.config.Lang;
import me.gmx.plotmines.core.BSubCommand;
import me.gmx.plotmines.objects.Plotmine;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class CmdPlotMinesReset extends BSubCommand {
    public CmdPlotMinesReset(){
        this.aliases.add("reset");
        this.correctUsage = "/plotmines reset";
        this.permission = "plotmines.reset";
        this.senderMustBePlayer=true;
    }

    @Override
    public void execute(){
        for(Plotmine m : PlotMines.getInstance().handler.getMines(player)){
            if (m.isInsideArena(player.getLocation())){
                player.teleport(m.interactBlock);
                if (m.fill()) {
                    msg(Lang.MSG_PLOTMINE_RESET.toMsg());
                    return;
                }else{
                    msg(Lang.MSG_ERROR.toMsg());
                    return;
                }
            }
        }
        msg(Lang.MSG_NO_PLOTMINES.toMsg());
    }
}
