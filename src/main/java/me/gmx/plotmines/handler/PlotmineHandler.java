package me.gmx.plotmines.handler;

import me.gmx.plotmines.PlotMines;
import me.gmx.plotmines.objects.Plotmine;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.*;

public class PlotmineHandler {
    private PlotMines ins;
    private Collection<Plotmine> loadedMines;
    public PlotmineHandler(PlotMines ins){
        this.ins = ins;
        this.loadedMines = new ArrayDeque<>();
        //test();
    }

    private void test(){
        new BukkitRunnable(){
            public void run(){
                for (Plotmine m : loadedMines){
                }
            }
        }.runTaskTimer(ins,20,20);
    }


    public boolean registerMine(Plotmine mine){
        if (loadedMines.contains(mine)){
            return false;
        }

        loadedMines.add(mine);
        return true;
    }

    public Set<Plotmine> getMines(Player p){
        Set<Plotmine> set = new HashSet<>();
        for (String s : ins.getMineConfig().getKeys(false)){
            if (p.getUniqueId().equals(UUID.fromString(s))){
                for (String mine_u : ins.getMineConfig().getConfigurationSection(s).getKeys(false))
                set.add(Plotmine.loadFromConfig(UUID.fromString(mine_u)));
            }
        }
        return set;
    }

    public Set<Plotmine> getActiveMine(Player p){
        Set<Plotmine> set = new HashSet<>();
        for (Plotmine mine : loadedMines){
            if (mine.getOwner().equals(Bukkit.getOfflinePlayer(p.getUniqueId()))){
                set.add(mine);
            }
        }
        return set;
    }

    public boolean unregisterMine(Plotmine mine){
        if (loadedMines.contains(mine)){
            loadedMines.remove(mine);
            return true;
        }
        return false;
    }

    public Plotmine borderFromLocation(Location loc){
        for (Plotmine mine : loadedMines){
            if (mine.getBorders().contains(loc.getBlock())){
                return mine;
            }else if (mine.interactBlock.equals(loc)){
                return mine;
            }

            /*int x = loc.getBlockX();
            int y = loc.getBlockY();
            int z = loc.getBlockZ();
            int bx = mine.getBotLeft().getBlockX();
            int bz = mine.getBotLeft().getBlockZ();
            int tx = mine.getTopRight().getBlockX();
            int tz = mine.getTopRight().getBlockZ();
            if (!loc.getWorld().equals(mine.getWorld())){
                continue;
            }
            if (x == bx || z == bz)
                return true;
            else if (z == tz || x == tx)
                return true;
            else if (y == mine.getBotLeft().getBlockY())
                return true;


*/
        }
        return null;
    }

    public boolean isBorder(Location l){
        for (Plotmine mine : loadedMines) {
            //interactBlock is a location field in Plotmine
            if (mine.interactBlock.equals(l)) {
                return true;
            } else
                for (Location loc : mine.getBorders()) {
                    if (loc.equals(l)) {
                        return true;
                    }


                }
        }
        return false;
    }
}
