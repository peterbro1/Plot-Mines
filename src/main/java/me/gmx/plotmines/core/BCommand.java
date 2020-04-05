package me.gmx.plotmines.core;


import me.gmx.plotmines.PlotMines;

import java.util.ArrayList;

public class BCommand {

    public PlotMines main;
    public ArrayList<BSubCommand> subcommands;

    public BCommand(PlotMines ins) {
        this.main = ins;
        subcommands = new ArrayList<BSubCommand>();
    }

}
