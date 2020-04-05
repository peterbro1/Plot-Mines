package me.gmx.plotmines.util;

import me.gmx.plotmines.PlotMines;
import me.gmx.plotmines.objects.Plotmine;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class FileUtils {

    public static void copy(InputStream input, File file){

        try{
            FileOutputStream output = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int i;
            while ((i = input.read(b)) > 0) {
                output.write(b,0,i);
            }
            output.close();
            input.close();


        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void mkDir(File file){
        try{
            file.mkdir();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static void writeMine(Plotmine mine){
        if (!mineExists(mine)){
            PlotMines.getInstance().getMineConfig().createSection(mine.getOwner().getUniqueId().toString()+"."+mine.getUUID().toString());
        }
        ConfigurationSection as = PlotMines.getInstance().getMineConfig().getConfigurationSection(mine.getOwner().getUniqueId().toString()+"."+mine.getUUID().toString());
        as.set("world",mine.getWorld().getName());
        //as.set("chunk",mine.getBotLeft().getChunk());
        as.set("min-x",mine.getBotLeft().getBlockX());
        as.set("min-y",mine.getBotLeft().getBlockY());
        as.set("min-z",mine.getBotLeft().getBlockZ());

        as.set("max-x",mine.getTopRight().getBlockX());
        as.set("max-y",mine.getTopRight().getBlockY());
        as.set("max-z",mine.getTopRight().getBlockZ());
        as.set("type",mine.getType());
        as.set("chunk_key",mine.getChunk().getChunkKey());
        as.set("state",mine.state.toString());
        //as.set("uuid",mine.getUUID().toString());
        PlotMines.getInstance().saveMineConfig();
    }
    public static boolean sectionExists(UUID s){

        if (!PlotMines.getInstance().getMineConfig().isConfigurationSection(s.toString()) || PlotMines.getInstance().getMineConfig().getConfigurationSection(s.toString()) == null){
            return false;
        }
        return true;
    }

    public static boolean mineExists(Plotmine m){
        if (!PlotMines.getInstance().getMineConfig().isConfigurationSection(m.getOwner().getUniqueId().toString()+"."+m.getUUID().toString()) || PlotMines.getInstance().getMineConfig().getConfigurationSection(m.getOwner().getUniqueId().toString()+"."+m.getUUID().toString()) == null){
            return false;
        }
        return true;
    }


    public static void removeMine(Plotmine a)throws NullPointerException{
        if (mineExists(a)){
            PlotMines.getInstance().getMineConfig().set(a.getOwner().getUniqueId().toString()+"."+a.getUUID().toString(),null);
            PlotMines.getInstance().saveMineConfig();
        }else{
            throw new NullPointerException("Mine could not be found in config");
        }
    }





}
