package me.gmx.plotmines.handler;

import me.gmx.plotmines.PlotMines;
import me.gmx.plotmines.config.Settings;
import me.gmx.plotmines.objects.MineState;
import me.gmx.plotmines.objects.Plotmine;
import me.gmx.plotmines.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class WorldListener implements Listener {

    @EventHandler
    public void chunkLoad(ChunkLoadEvent e){
        new BukkitRunnable(){
            public void run(){
                for (Plotmine m : LocationUtil.getAllInChunk(e.getChunk())) {
                    try {
                        if (m.getOwner().isOnline() && Settings.LOAD_OFFLINE_PLAYER_MINES.getBoolean()) {
                            m.register();
                            m.enable();
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }.runTaskAsynchronously(PlotMines.getInstance());
    }

    @EventHandler
    public void chunkUnload(ChunkUnloadEvent e){
        new BukkitRunnable(){
            public void run(){
                        for (Plotmine m : LocationUtil.getAllInChunk(e.getChunk())){
                            m.unregister();
                        }
            }
        }.runTaskAsynchronously(PlotMines.getInstance());
    }
}
