package me.gmx.plotmines.handler;

import com.sk89q.worldguard.bukkit.event.block.BreakBlockEvent;
import me.gmx.plotmines.PlotMines;
import me.gmx.plotmines.config.Lang;
import me.gmx.plotmines.objects.MineState;
import me.gmx.plotmines.objects.Plotmine;
import me.gmx.plotmines.util.ItemMetadata;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftItem;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

import static javax.swing.text.html.HTML.Attribute.LANG;

public class PlayerListener implements Listener {

    @EventHandler
    public void blockPlace(BlockPlaceEvent e){
        if (e.getItemInHand().getType() == Material.BEDROCK){
            ItemStack stack = e.getItemInHand();
            if (ItemMetadata.hasNBTDataString(stack,"pm_uuid")){
                //confirmed

                if (Plotmine.exists(UUID.fromString(ItemMetadata.getNBTDataString(CraftItemStack.asNMSCopy(stack),"pm_uuid")))){
                    e.getPlayer().sendMessage(Lang.MSG_FAILED_PLACE.toMsg());
                    e.setCancelled(true);
                    return;
                }
                Location l = e.getBlock().getLocation().subtract(0,1,0);
                try{
                    int size = Integer.valueOf(ItemMetadata.getNBTDataString(CraftItemStack.asNMSCopy(stack),"size"));
                    String type = ItemMetadata.getNBTDataString(CraftItemStack.asNMSCopy(stack),"type");
                    Plotmine p = new Plotmine(UUID.fromString(ItemMetadata.getNBTDataString(CraftItemStack.asNMSCopy(stack),"pm_uuid"))
                            ,l.clone().subtract(size,size,size)
                            ,l,type
                            ,e.getPlayer().getUniqueId());

                    if (!p.render()){
                        e.getPlayer().sendMessage(Lang.MSG_FAILED_PLACE.toMsg());
                        e.setCancelled(true);
                        return;
                    }
                    e.getPlayer().sendMessage(Lang.MSG_PLOTMINE_PLACE.toMsg());
                    p.register();

                }catch(Exception ex){
                    e.getPlayer().sendMessage(Lang.MSG_FAILED_PLACE.toMsg());
                    e.setCancelled(true);
                    ex.printStackTrace();
                    return;
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void interact(PlayerInteractEvent e){
        if(e.getHand() == EquipmentSlot.OFF_HAND) return;
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK){
            return;
        }
        if (!e.getClickedBlock().getType().equals(Material.BEDROCK)){
            return;
        }
        if (e.isCancelled())
            return;




        Plotmine mine = PlotMines.getInstance().handler.borderFromLocation(e.getClickedBlock().getLocation());
        if (mine == null){
            return;
        }
            if (!mine.getOwner().getUniqueId().equals(e.getPlayer().getUniqueId())){
                e.getPlayer().sendMessage(Lang.MSG_DONT_OWN.toMsg());
                return;
            }

            if (mine.state == MineState.VERIFY && e.getPlayer().isSneaking()) {
                mine.destroy(false).give(e.getPlayer());
                return;
            } else if (mine.state == MineState.VERIFY){
                if (!mine.canEnable()){
                    e.getPlayer().sendMessage(Lang.MSG_FAILED_PLACE.toMsg());
                    return;
                }
                mine.enable();
                e.getPlayer().sendMessage(Lang.MSG_MINE_CONFIRMED.toMsg());
                mine.writeToDisk();
                return;
            }else if (mine.state == MineState.ACTIVE){
                if (e.getPlayer().isSneaking()){
                    if (e.getPlayer().getInventory().firstEmpty() == -1){
                        e.getPlayer().sendMessage(Lang.MSG_INV_FULL.toMsg());
                        return;
                    }else{
                        new BukkitRunnable(){
                            public void run(){
                                mine.destroy(true).give(e.getPlayer());
                            }
                        }.runTask(PlotMines.getInstance());
                    }
                }else{
                            if (mine.isEmpty()){
                                mine.fill();
                                e.getPlayer().teleport(new Location(mine.getWorld(),mine.interactBlock.getBlockX(),mine.interactBlock.getBlockY()+1,mine.interactBlock.getBlockZ(),e.getPlayer().getLocation().getYaw(),e.getPlayer().getLocation().getPitch()));
                                e.getPlayer().sendMessage(Lang.MSG_MINE_REFILLED.toMsg());
                                return;
                            }else{
                                e.getPlayer().sendMessage(Lang.MSG_NOT_EMPTY.toMsg());
                                return;
                            }


                }
            }else if (mine.state == MineState.INACTIVE){
                mine.enable();
            }else{
                e.getPlayer().sendMessage("Contact GMX-- Code: 552");
            }


        e.setCancelled(true);



    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void breakEvent(BlockBreakEvent e){
        if (e.getBlock().getType().equals(Material.BEDROCK)){
            new BukkitRunnable(){
                public void run(){
                    if (PlotMines.getInstance().handler.isBorder(e.getBlock().getLocation())){
                        new BukkitRunnable(){
                            public void run(){
                                e.getBlock().setType(Material.BEDROCK);
                                //e.setCancelled(true);
                                e.getPlayer().sendMessage(Lang.MSG_FAILED_BREAK.toMsg());
                            }
                        }.runTask(PlotMines.getInstance());
                    }
                }
            }.runTaskAsynchronously(PlotMines.getInstance());
            return;
        }/*else if (PlotMines.getInstance().handler.hasActiveMine(e.getPlayer())){

            new BukkitRunnable(){
                public void run(){
                    Plotmine mine;
                    if ((mine = PlotMines.getInstance().handler.borderFromLocation(e.getBlock().getLocation())) != null){
                        if (mine.isEmpty()){
                            e.getPlayer().teleport(mine.interactBlock);
                            new BukkitRunnable(){
                                public void run(){
                                    mine.fill();
                                }
                            }.runTaskLater(PlotMines.getInstance(),1);
                        }
                    }
                }
            }.runTaskAsynchronously(PlotMines.getInstance());

        }*/
    }
    @EventHandler
    public void logout(PlayerQuitEvent e){
        for (Plotmine mine : PlotMines.getInstance().handler.getActiveMine(e.getPlayer())){
            mine.unregister();
        }
    }

    @EventHandler
    public void login(PlayerJoinEvent e){
        for (Plotmine p : PlotMines.getInstance().handler.getMines(e.getPlayer())){
            p.register();
            p.enable();
        }
    }
}
