package me.gmx.plotmines.command.plotmines;

import me.gmx.plotmines.config.Lang;
import me.gmx.plotmines.core.BSubCommand;
import me.gmx.plotmines.objects.Plotmine;
import me.gmx.plotmines.objects.PlotmineItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CmdPlotMinesGive extends BSubCommand {


    public CmdPlotMinesGive(){
        this.aliases.add("give");
        this.permission = "plotmines.give";
        this.correctUsage = "/plotmines give [player] [tier] [size]";
        this.senderMustBePlayer = false;
    }

    @Override
    public void execute() {
        if (args.length < 1 || args.length > 3){
            sendCorrectUsage();
            return;
        }
        if (Bukkit.getPlayer(args[0]) != null) {
            Player p = Bukkit.getPlayer(args[0]);
            if (p.isOnline()){
                try{
                    PlotmineItem i = new PlotmineItem(Integer.parseInt(args[2]),args[1]);
                    i.give(p);
                }catch(Exception e){
                    msg("&4&lPlease type valid numbers!");
                    e.printStackTrace();
                }
            }else{
                msg(Lang.MSG_PLAYER_NULL.toMsg());

            }

        }else{
            msg(Lang.MSG_PLAYER_NULL.toMsg());
        }




    }


}
