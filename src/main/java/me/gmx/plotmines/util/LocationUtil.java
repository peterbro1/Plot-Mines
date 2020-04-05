package me.gmx.plotmines.util;

import me.gmx.plotmines.PlotMines;
import me.gmx.plotmines.objects.Plotmine;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LocationUtil {


    public static List<Block> getSquareAroundLocation(Location location, int radius){
        List<Block> blocks = new ArrayList<Block>();
        for (int x = location.getBlockX()-radius;x<location.getBlockX()+radius;x++){
            for (int y = location.getBlockY()-radius;y<location.getBlockY()+radius;y++){
                if ((x == location.getBlockX()-radius || x == location.getBlockX()+radius) && (y ==location.getBlockY()-radius || y == location.getBlockY()+radius)){
                    blocks.add(location.getWorld().getHighestBlockAt(x,y));
                }
            }
        }
        return blocks;
    }




    public static List<Block> getBorders(Location bl, Location tr){
        List<Block> blocks = new ArrayList<Block>();
        int y = (bl.getBlockY());
        for (int x = bl.getBlockX();x<=tr.getBlockX();x++){
            for (int z = bl.getBlockZ();z<=tr.getBlockZ();z++){
                if ((x == bl.getBlockX() || x == tr.getBlockX()) || (z == bl.getBlockZ() || z == tr.getBlockZ()))
                blocks.add(bl.getWorld().getBlockAt(x,y,z));
            }
        }

        return blocks;
    }

    public static List<Plotmine> getAllInChunk(Chunk c){
        List<Plotmine> l = new ArrayList<>();
        for(String s : PlotMines.getInstance().getMineConfig().getKeys(false)){ //player uuid
            for (String m : PlotMines.getInstance().getMineConfig().getConfigurationSection(s).getKeys(false)){
                if (PlotMines.getInstance().getMineConfig().get(s+"."+m+".chunk_key").equals(c.getChunkKey())){
                    l.add(Plotmine.loadFromConfig(UUID.fromString(m)));

                }
            }

        }
        return l;
    }


    public static Location getMiddleBlock(Location botLeft, Location topRight){
        Location loc = new Location(botLeft.getWorld(),
                (botLeft.getBlockX()+topRight.getBlockX())/2,
                (botLeft.getBlockY()+topRight.getBlockY())/2,
                (botLeft.getBlockZ()+topRight.getBlockZ())/2);
        return loc;
    }
}
