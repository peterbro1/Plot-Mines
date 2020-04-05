package me.gmx.plotmines.objects;

import com.google.common.collect.ImmutableMap;
import me.gmx.plotmines.config.Lang;
import me.gmx.plotmines.util.ItemMetadata;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PlotmineItem extends ItemStack {

    private int size;
    private String type;
    private UUID id;

    public PlotmineItem(Plotmine mine){
        super(Material.BEDROCK,1);
        this.size = mine.getSize();
        this.type = mine.getType();
        this.id = mine.getUUID();
        ItemMeta meta = getItemMeta();
        meta.addEnchant(Enchantment.DURABILITY,5,true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName(Lang.PLOTMINE_TITLE.toString());
        List<String> l = new ArrayList<String>();
        for (String s : Lang.PLOTMINE_LORE.getStringList()){
            l.add(s.replace("%size%",String.valueOf(mine.getSize()))
           .replace("%type%",mine.getType()));
        }
        meta.setLore(l);
        setItemMeta(meta);
    }

    public PlotmineItem(int size, String type){
        super(Material.BEDROCK,1);
        //this.internal = mine;
        this.size = size;
        this.type = type;
        this.id = UUID.randomUUID();
        ItemMeta meta = getItemMeta();
        meta.addEnchant(Enchantment.DURABILITY,5,true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName(Lang.PLOTMINE_TITLE.toString());
        List<String> l = new ArrayList<String>();
        for (String s : Lang.PLOTMINE_LORE.getStringList()){
            l.add(s.replace("%size%",String.valueOf(size))
        .replace("%type%",type));
        }
        meta.setLore(l);
        setItemMeta(meta);
    }


    public void give(Player p){
        p.getInventory().addItem(ItemMetadata.newWithNBTData(this,ImmutableMap.of(
                "size",String.valueOf(size)
        , "type",type
        , "pm_uuid", id.toString())));

    }



}
