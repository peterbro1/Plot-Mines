package me.gmx.plotmines.util;


import com.google.common.collect.ImmutableMap;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;

import java.util.HashMap;

public class ItemMetadata {

    public static Class<?> getNMSClass(String name) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
    }

    public static void addNBTDataString(net.minecraft.server.v1_12_R1.ItemStack item, String name, String value){
        NBTTagCompound o;

        o = item.hasTag() ? item.getTag() : new NBTTagCompound();

        o = item.getTag();
        o.setString(name, value);
        item.setTag(o);
    }

    public static void addNBTDataString(org.bukkit.inventory.ItemStack i, String name, String value){
        NBTTagCompound o;
        net.minecraft.server.v1_12_R1.ItemStack item = CraftItemStack.asNMSCopy(i);
        o = item.hasTag() ? item.getTag() : new NBTTagCompound();
        o.setString(name, value);
        item.setTag(o);
    }

    public static void removeNBTDataString(org.bukkit.inventory.ItemStack i, String name){
        NBTTagCompound o;
        net.minecraft.server.v1_12_R1.ItemStack item = CraftItemStack.asNMSCopy(i);
        o = item.hasTag() ? item.getTag() : new NBTTagCompound();
        o.remove(name);
        item.setTag(o);
    }

    public static org.bukkit.inventory.ItemStack newWithRemovedNBTData(org.bukkit.inventory.ItemStack stack, String data){
        net.minecraft.server.v1_12_R1.ItemStack craftstack = CraftItemStack.asNMSCopy(stack);

        NBTTagCompound o;

        o = craftstack.hasTag() ? craftstack.getTag() : new NBTTagCompound();

        o.remove(data);
        craftstack.setTag(o);
        return CraftItemStack.asBukkitCopy(craftstack);

    }


    public static org.bukkit.inventory.ItemStack newWithNBTData(org.bukkit.inventory.ItemStack stack, String data, String value){
        net.minecraft.server.v1_12_R1.ItemStack craftstack = CraftItemStack.asNMSCopy(stack);

        NBTTagCompound o;

        o = craftstack.hasTag() ? craftstack.getTag() : new NBTTagCompound();

        o.setString(data, value);
        craftstack.setTag(o);
        return CraftItemStack.asBukkitCopy(craftstack);

    }
    public static org.bukkit.inventory.ItemStack newWithNBTData(org.bukkit.inventory.ItemStack stack,HashMap<String,String> map){
        net.minecraft.server.v1_12_R1.ItemStack craftstack = CraftItemStack.asNMSCopy(stack);

        NBTTagCompound o;

        o = craftstack.hasTag() ? craftstack.getTag() : new NBTTagCompound();
        if (!map.isEmpty())
            for (String s : map.keySet()) {
                o.setString(s, map.get(s));
            }
        craftstack.setTag(o);
        return CraftItemStack.asBukkitCopy(craftstack);

    }

    public static org.bukkit.inventory.ItemStack newWithNBTData(org.bukkit.inventory.ItemStack stack, ImmutableMap<String,String> map){
        net.minecraft.server.v1_12_R1.ItemStack craftstack = CraftItemStack.asNMSCopy(stack);

        NBTTagCompound o;

        o = craftstack.hasTag() ? craftstack.getTag() : new NBTTagCompound();
        if (!map.isEmpty())
            for (String s : map.keySet()) {
                o.setString(s, map.get(s));
            }
        craftstack.setTag(o);
        return CraftItemStack.asBukkitCopy(craftstack);

    }



    public static String getNBTDataString(net.minecraft.server.v1_12_R1.ItemStack item, String key) {
        if (!item.hasTag()) {
            return null;
        }
        NBTTagCompound tag = item.getTag();
        if (tag.hasKey(key)) {
            return tag.getString(key);
        }
        return null;
    }


    public static boolean hasNBTDataString(net.minecraft.server.v1_12_R1.ItemStack item, String key, String value) {
        if (!item.hasTag()) {
            return false;
        }
        NBTTagCompound tag = item.getTag();
        if (tag.hasKey(key)) {
            if (tag.getString(key).equals(value)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasNBTDataString(org.bukkit.inventory.ItemStack itemstac, String key) {
        net.minecraft.server.v1_12_R1.ItemStack item = CraftItemStack.asNMSCopy(itemstac);

        if (!item.hasTag()) {
            return false;
        }
        NBTTagCompound tag = item.getTag();
        if (tag.hasKey(key)) {
            return true;
        }
        return false;
    }


}