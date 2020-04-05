package me.gmx.plotmines.command.plotmines;

import me.gmx.plotmines.PlotMines;
import me.gmx.plotmines.config.Lang;
import me.gmx.plotmines.core.BSubCommand;

public class CmdPlotMinesList extends BSubCommand {
    public CmdPlotMinesList(){
        this.aliases.add("list");
        this.correctUsage = "/plotmines list";
        this.senderMustBePlayer=false;
        this.permission = "plotmines.list";
    }

    @Override
    public void execute(){
        if (args.length > 0){
            sendCorrectUsage();
            return;
        }

        for (String s : PlotMines.getInstance().getContentConfig().getKeys(false)){
            msg(Lang.PREFIX + "&4"+s);
        }
    }
}
