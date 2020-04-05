package me.gmx.plotmines.util;

import me.gmx.plotmines.PlotMines;
import me.gmx.plotmines.objects.Plotmine;
import net.minecraft.server.v1_12_R1.BlockStainedHardenedClay;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.material.MaterialData;

import java.util.*;

public class ContentUtils {


    public static Set<MaterialData> deserialize(String id){
        Set<MaterialData> set = new HashSet<>();
        if (PlotMines.getInstance().getContentConfig().getConfigurationSection(id) != null) {
            ConfigurationSection sec = PlotMines.getInstance().getContentConfig().getConfigurationSection(id);
            List<String> content = sec.getStringList("blocks");

            for (String entry : content){
                set.add(new MaterialData(Material.getMaterial(entry.split("\\(")[0])
                        ,Byte.valueOf(

                                entry.split("\\(")[1].replace(")","")

                )));
            }


        }
        return set;
    }

    public static void saveToConfig(Set<MaterialData> set, String name) {
        ConfigurationSection sec = PlotMines.getInstance().getContentConfig().getConfigurationSection(name);
        if (sec == null)
            PlotMines.getInstance().getContentConfig().createSection(name);
        sec = PlotMines.getInstance().getContentConfig().getConfigurationSection(name);
        List<String> l = new ArrayList<String>();
        for(MaterialData b : set){
            l.add(b.toString());
        }

        sec.set("blocks",l);
        PlotMines.getInstance().saveContentConfig();
    }

    public static boolean isType(String id){
        return (PlotMines.getInstance().getContentConfig().isConfigurationSection(id));
    }
}
