package me.gmx.plotmines.objects;

import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.config.Settings;
import com.intellectualcrafters.plot.object.Plot;
import me.gmx.plotmines.PlotMines;
import me.gmx.plotmines.util.ContentUtils;
import me.gmx.plotmines.util.FileUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;


public class Plotmine {
    private Location tr;
    private World world;
    public Location interactBlock;
    private Location bl;
    private Set<MaterialData> contents;
    private String type;
    private UUID owner;
    private UUID id;
    public MineState state;
    private int taskId;


    public Plotmine(UUID id, Location bl, Location tr, String type, UUID owner){
        this.owner = owner;
        this.interactBlock = tr.clone().add(0,1,0);
        this.id = id;
        this.world = bl.getWorld();
        this.bl = bl;
        this.tr = tr;
        this.type = type;
        this.state = MineState.VERIFY;
        this.contents = ContentUtils.deserialize(type);
    }


    public Plotmine(Location bl, Location tr, String type, UUID owner){
        new Plotmine(UUID.randomUUID(),bl,tr,type,owner);
    }


    public boolean isOwnerOnline(){
        if (Bukkit.getPlayer(owner) != null){
            return true;
        }else
            return false;
    }

    public static Plotmine loadFromConfig(UUID id)throws NullPointerException{
        Location botLeft, topRight;
        String type;
        UUID ownera;
        for (String o_uuid : PlotMines.getInstance().getMineConfig().getKeys(false)){
            for (String m_uuid : PlotMines.getInstance().getMineConfig().getConfigurationSection(o_uuid).getKeys(false)){

                if (id.toString().equals(m_uuid)){
                    try{
                        ConfigurationSection sec = PlotMines.getInstance().getMineConfig().getConfigurationSection(o_uuid+"."+m_uuid);
                        botLeft = new Location(Bukkit.getWorld(sec.getString("world")),sec.getInt("min-x"),sec.getInt("min-y"),sec.getInt("min-z"));
                        topRight = new Location(Bukkit.getWorld(sec.getString("world")),sec.getInt("max-x"),sec.getInt("max-y"),sec.getInt("max-z"));
                        ownera =UUID.fromString(o_uuid);
                        type = sec.getString("type");
                        return new Plotmine(UUID.fromString(m_uuid),botLeft,topRight,type,ownera);
                    }catch (Exception ex){
                        ex.printStackTrace();
                        throw new NullPointerException("Unable to grab locations from config!");
                    }
                }
            }
        }

        // BlockVector bl = new BlockVector(botLeft.getBlockX(),botLeft.getBlockY(),botLeft.getBlockZ());
        // BlockVector tr = new BlockVector(topRight.getBlockX(),topRight.getBlockY(),topRight.getBlockZ());
        throw new NullPointerException("Something went wrong!");
    }


    public static boolean exists(UUID m){
        if (PlotMines.getInstance().getMineConfig().getConfigurationSection(m.toString()) == null)
            return false;
        return true;
    }

    public PlotmineItem destroy(){
        revert();
        unregister();
        return new PlotmineItem(this);
    }

    public PlotmineItem destroy(boolean b){
        if (b) {
            revert();
        }
        unregister();
        interactBlock.getBlock().setType(Material.AIR);
        deleteFromDisk();
        return new PlotmineItem(this);
    }

    private void revert(){
        for (Location l : getBorders()){
            l.getBlock().setType(Material.AIR);
        }
        for (Block b : getInside()){
            b.setType(Material.AIR);
        }
        interactBlock.getBlock().setType(Material.AIR);
    }

    private Set<Block> getInside(){
        HashSet<Block> set = new HashSet<>();

        int[] dimensions = new int[]{getSize(), getSize(), getSize()};
        for (int x = 1; x < dimensions[0]-1; x++) {
            for (int y = 1; y < dimensions[1]-1; y++) {
                for (int z = 1; z < dimensions[2]-1; z++) {
                        set.add(interactBlock.clone().subtract(x, y, z).getBlock());
                }
            }
        }

        return set;
    }

    public boolean isEmpty(){
        Set<Block> a = getInside(); a.removeIf(b -> b.getType().equals(Material.AIR) || b.getType() == null);

        if (a.isEmpty())
            return true;
        else
            return false;
    }

    public Chunk getChunk(){
        return interactBlock.getChunk();
    }

    public void register(){
        PlotMines.getInstance().handler.registerMine(this);
    }

    public void writeToDisk(){
        FileUtils.writeMine(this);
        PlotMines.getInstance().reloadMineConfig();
    }
    public void unregister(){
        PlotMines.getInstance().handler.unregisterMine(this);
        this.disable();
    }
    public void deleteFromDisk(){
        FileUtils.removeMine(this);
    }

    public boolean isInsideArena(Location loc){
        return bl.getBlockX() < loc.getBlockX() && tr.getBlockX() > loc.getBlockX() &&
                bl.getBlockY() <= loc.getBlockY() && tr.getBlockY() > loc.getBlockY() &&
                bl.getBlockZ() < loc.getBlockZ() && tr.getBlockZ() > loc.getBlockZ();
    }





    public World getWorld(){
        return this.world;
    }
    public Location getBotLeft(){
        return this.bl;
    }

    public boolean render(){
       for (Location l : getBorders()){
           if (l.getBlock().getType().equals(Material.BEDROCK)
                   || l.getBlock().getType().equals(Material.CHEST)
                   || l.getBlock().getType().equals(Material.TRAPPED_CHEST)
                   || !Plot.getPlot(new com.intellectualcrafters.plot.object.Location(l.getWorld().getName(),l.getBlockX(),l.getBlockY(),l.getBlockZ())).isOwner(owner))
               return false;

           if (!canEnable()){
               return false;
           }

           if (l.getBlockY() == getTopRight().getBlockY()){
               l.getBlock().setType(Material.GLASS);
           }
       }
       this.state = MineState.VERIFY;
       return true;
    }

    public boolean canEnable(){
        for (Block b : getInside()){
            if (PlotMines.getInstance().handler.borderFromLocation(b.getLocation()) != null){
                return false;
            }
        }

        for (Location l : getBorders()){
            if (PlotMines.getInstance().handler.borderFromLocation(l) != null){
                return false;
            }
        }
        return true;
    }
    public void enable(){

        new BukkitRunnable(){
            public void run(){
                state = MineState.ACTIVE;
                fill();
                makeBorders();
            }
        }.runTask(PlotMines.getInstance());
        this.taskId = new BukkitRunnable(){
            public void run(){
                if (state == MineState.ACTIVE)
                getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE,interactBlock,3);
            }
        }.runTaskTimer(PlotMines.getInstance(),0,20).getTaskId();
    }

    public void init(){
        makeBorders();
        writeToDisk();
    }


    private void makeBorders(){
        for (Location l : getBorders()){
            l.getBlock().setType(Material.BEDROCK);
        }
    }

    public void disable(){
        this.state = MineState.INACTIVE;
        try{
            Bukkit.getScheduler().cancelTask(this.taskId);
        }catch(Exception e){

        }
    }

    public boolean fill(){
        if (this.state != MineState.ACTIVE){
            return false;
        }



        Random r = new Random();
        MaterialData m;
        if (contents.size() > 1) {
            int i = r.nextInt(contents.size() + 1);
            m = (MaterialData) contents.toArray()[i];
        }else
            m = (MaterialData)contents.toArray()[0];
        for (Block b : getInside()){
            b.setType(m.getItemType());
            b.setData(m.getData());
        }
        return true;

    }

    public Location getTopRight(){
        return this.tr;
    }

    public String getType(){
        return this.type;
    }

    public OfflinePlayer getOwner(){
        return Bukkit.getOfflinePlayer(this.owner);
    }

    public int getSize(){
//        return (int) Math.round(tr.toVector().subtract(bl.toVector()).length());
        return Math.abs(bl.getBlockX()-tr.getBlockX());
    }

    public UUID getUUID(){
        return this.id;
    }





   /* public Set<Block> borders(){
        Set<Block> s = new HashSet<>();
                for (int x = tr.getBlockX();x<=bl.getBlockX();x++){
                    for(int y = bl.getBlockY();y<=tr.getBlockY();y++){
                        for(int z = tr.getBlockZ();z<=bl.getBlockZ();z++){
                                if (x == bl.getBlockX() || z == bl.getBlockZ())
                                    s.add(a(x,y,z));
                                else if (z == tr.getBlockZ() || x == tr.getBlockX())
                                    s.add(a(x,y,z));
                                else if (y == bl.getBlockY())
                                    s.add(a(x,y,z));

                        }
                    }
                }

        return s;
    }*/


    public HashSet<Location> getBorders() {
        HashSet<Location> set = new HashSet<Location>();

        int[] dimensions = new int[]{getSize(), getSize(), getSize()};
        for (int x = 0; x < dimensions[0]; x++) {
            for (int y = 1; y < dimensions[1]; y++) {
                for (int z = 0; z < dimensions[2]; z++) {
                    if ((x == 0 || z == 0 || x == dimensions[0] - 1 || y == dimensions[1] - 1 || z == dimensions[2] - 1)) {
                        set.add(interactBlock.clone().subtract(x, y, z));
                    }
                }
            }
        }

    return set;
    }





}
