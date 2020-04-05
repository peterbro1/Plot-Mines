package me.gmx.plotmines.command;

import me.gmx.plotmines.PlotMines;
import me.gmx.plotmines.command.plotmines.*;
import me.gmx.plotmines.config.Lang;
import me.gmx.plotmines.core.BCommand;
import me.gmx.plotmines.core.BSubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CmdPlotMines extends BCommand implements CommandExecutor {

    public CmdPlotMines(PlotMines ins) {
        super(ins);
        this.subcommands.add(new CmdPlotMinesGive());
        this.subcommands.add(new CmdPlotMinesList());
        this.subcommands.add(new CmdPlotMinesHelp());
        this.subcommands.add(new CmdPlotMinesReload());
        this.subcommands.add(new CmdPlotMinesReset());
    }

    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        if (arg3.length < 1) {
            arg0.sendMessage(Lang.MSG_PLOTMINE_USAGE.toMsg());
            return true;
        }

        for (BSubCommand cmd : this.subcommands) {
            if (cmd.aliases.contains(arg3[0])) {
                cmd.execute(arg0,arg3);
                return true;
            }
        }
        arg0.sendMessage(Lang.MSG_PLOTMINE_USAGE.toMsg());

        return true;
    }

}
