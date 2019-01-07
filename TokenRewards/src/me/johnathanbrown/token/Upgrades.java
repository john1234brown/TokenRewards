package me.johnathanbrown.token;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Crops;
import org.bukkit.scheduler.BukkitScheduler;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.minecraft.server.v1_12_R1.TileEntityBrewingStand;
import net.minecraft.server.v1_12_R1.TileEntityFurnace;

public class Upgrades implements Listener {
	
	private static GriefPrevention griefPrevention = GriefPrevention.instance;
	private Main plugin;
	//public HashMap<String, Integer> Spawner = new HashMap<String, Integer>();
	//public HashMap<String, Integer> Farm = new HashMap<String, Integer>();
	//public HashMap<String, Integer> Loot = new HashMap<String, Integer>();
	//public HashMap<String, Integer> Furnace = new HashMap<String, Integer>();
	//public HashMap<String, Integer> Brewing = new HashMap<String, Integer>();
	public List<String> S1 = new ArrayList<String>();
	public List<String> S2 = new ArrayList<String>();
	public List<String> S3 = new ArrayList<String>();
	public List<String> F1 = new ArrayList<String>();
	public List<String> F2 = new ArrayList<String>();
	public List<String> F3 = new ArrayList<String>();
	public List<String> L1 = new ArrayList<String>();
	public List<String> L2 = new ArrayList<String>();
	public List<String> L3 = new ArrayList<String>();
	public List<String> Fu1 = new ArrayList<String>();
	public List<Block> FU1 = new ArrayList<Block>();
	public List<String> Fu2 = new ArrayList<String>();
	public List<Block> FU2 = new ArrayList<Block>();
	public List<String> Fu3 = new ArrayList<String>();
	public List<Block> FU3 = new ArrayList<Block>();
	public List<String> B1 = new ArrayList<String>();
	public List<Block> BU1 = new ArrayList<Block>();
	public List<String> B2 = new ArrayList<String>();
	public List<Block> BU2 = new ArrayList<Block>();
	public List<String> B3 = new ArrayList<String>();
	public List<Block> BU3 = new ArrayList<Block>();
	public int s1;
	public int s2;
	public int s3;
	public int f1;
	public int f2;
	public int f3;
	public int l1;
	public int l2;
	public int l3;
	public int fu1;
	public int fu2;
	public int fu3;
	public int b1;
	public int b2;
	public int b3;
		 
	public Upgrades (Main mainInstance) {
	this.plugin = mainInstance;
	refresh();
	}
	
    @EventHandler
	public void OnFurnaceClose(InventoryClickEvent e){
		if (e.getView().getTopInventory() == null){return;}
    	if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR){return;}
		if (e.getView().getTopInventory().getType() == InventoryType.FURNACE && e.getView().getTopInventory().getLocation().getBlock().getType() == Material.FURNACE){
		Claim claim = griefPrevention.dataStore.getClaimAt(e.getInventory().getLocation(), true, null);
		if (claim != null){
			String name = claim.getOwnerName();
			if (Fu1.contains(name)){plugin.runnable(e.getView().getTopInventory().getLocation().getBlock(), fu1);}
			if (Fu2.contains(name)){plugin.runnable(e.getView().getTopInventory().getLocation().getBlock(), fu2);}
			if (Fu3.contains(name)){plugin.runnable(e.getView().getTopInventory().getLocation().getBlock(), fu3);}
			}
		}
		}
	
    @EventHandler
    public void OnBrewClick(InventoryClickEvent e){
    	if (e.getView().getTopInventory() == null){return;}
    	if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR){return;}
    	Claim claim = griefPrevention.dataStore.getClaimAt(e.getInventory().getLocation(), true, null);
		if (claim != null && e.getCurrentItem() != null){
    	if(e.getView().getTopInventory().getType() == InventoryType.BREWING && e.getView().getTopInventory().getLocation().getBlock().getType() == Material.BREWING_STAND);
    		String name = claim.getOwnerName();
    		if (B1.contains(name)){plugin.runnableB(e.getView().getTopInventory().getLocation().getBlock(), b1);}
    		if (B2.contains(name)){plugin.runnableB(e.getView().getTopInventory().getLocation().getBlock(), b2);}
    		if (B3.contains(name)){plugin.runnableB(e.getView().getTopInventory().getLocation().getBlock(), b3);}
    	}		}
	
    @EventHandler
	public void BurnEvent(FurnaceSmeltEvent event) {
    	Claim claim = griefPrevention.dataStore.getClaimAt(event.getBlock().getLocation(), true, null);
		if (claim != null){
		String name = claim.getOwnerName();
		if (Fu1.contains(name)){plugin.runnable(event.getBlock(), fu1); }
		if (Fu2.contains(name)){plugin.runnable(event.getBlock(), fu2); }
		if (Fu3.contains(name)){plugin.runnable(event.getBlock(), fu3); }
		}
	}
	
	@EventHandler
	public void ForceThingEvent(FurnaceBurnEvent event) {
    	Claim claim = griefPrevention.dataStore.getClaimAt(event.getBlock().getLocation(), true, null);
		if (claim != null){
		String name = claim.getOwnerName();
		if (Fu1.contains(name)){plugin.runnable(event.getBlock(), fu1); }
		if (Fu2.contains(name)){plugin.runnable(event.getBlock(), fu2); }
		if (Fu3.contains(name)){plugin.runnable(event.getBlock(), fu3); }
	}
	}
	
	  public void setCookSpeedFast(Block block, int efficiencyLevel)
	  {
		if (!(block.getState().getChunk().isLoaded())){return;}
	    try
	    {
	    TileEntityFurnace furnaceNMS = (TileEntityFurnace) ((CraftWorld) block.getWorld()).getTileEntityAt(block.getX(), block.getY(), block.getZ());
	    if (!(furnaceNMS.isBurning())){ return;}
	    Field cttField = TileEntityFurnace.class.getDeclaredField("cookTimeTotal");
	    cttField.setAccessible(true);
	    cttField.set(furnaceNMS, Integer.valueOf(Math.max(1,  350 / (efficiencyLevel + 2))));
	    furnaceNMS.update();
	    //System.out.println("Changed cooking speed!");
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }
	  }
	  
	  public void setBrewSpeedFast(Block block, int efficiencyLevel)
	  {
		if (!(block.getState().getChunk().isLoaded())){ return;}
	    try
	    {
	    TileEntityBrewingStand brewNMS = (TileEntityBrewingStand) ((CraftWorld) block.getWorld()).getTileEntityAt(block.getX(), block.getY(), block.getZ());
	    if (brewNMS.getProperty(0) == 0){return;}
	    Field cttField = TileEntityBrewingStand.class.getDeclaredField("brewTime");
	    cttField.setAccessible(true);
	    cttField.set(brewNMS, Integer.valueOf(Math.max(1,  700 / (efficiencyLevel + 2))));
	    brewNMS.update();
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }
	  }
	
	@EventHandler
	public void onDropLoot(EntityDeathEvent e){
		if (e.getEntityType() != EntityType.PLAYER){
			Claim claim = griefPrevention.dataStore.getClaimAt(e.getEntity().getLocation(), true, null);
			if (claim != null){
				String name = claim.getOwnerName();
				if (L1.contains(name)){LootIncrease(e,l1);}
				if (L2.contains(name)){LootIncrease(e,l2);}
				if (L3.contains(name)){LootIncrease(e,l3);}
				}
			}
		}
	
	private void LootIncrease(EntityDeathEvent e, int d){
		List<ItemStack> drops = e.getDrops();
		for(int i=0; i<drops.size() ;i++){
			ItemStack item = drops.get(i);
			int lel = Math.round(item.getAmount()+d);
			item.setAmount(lel);
		}
		return;
	}
	
	//Farm Upgrades Handled!
    @EventHandler
    public void onCropGrow(BlockGrowEvent e) {
    	
    	Claim claim = griefPrevention.dataStore.getClaimAt(e.getBlock().getLocation(), true, null);
		if (claim != null){
			String name = claim.getOwnerName();
			int chance = -1;
			if (F1.contains(name)){chance = f1;}
			if (F2.contains(name)){chance = f2;}
			if (F3.contains(name)){chance = f3;}
			if (chance >= 0){
            	int randomNum = ThreadLocalRandom.current().nextInt(1, 100 + 1);
                if (randomNum <= chance)
                    growCrop(e);}	}	
    	}
	
    private void growCrop(BlockGrowEvent e) {
        if (e.getBlock().getType().equals(plugin.CROPS)) {
            e.setCancelled(true);
            Crops c = new Crops(CropState.RIPE);
            BlockState bs = e.getBlock().getState();
            bs.setData(c);
            bs.update();
        }
        
        Block below = e.getBlock().getLocation().subtract(0, 1, 0).getBlock();

        if (below.getType() == plugin.SUGAR_CANE_BLOCK) {
            Block above = e.getBlock().getLocation().add(0, 1, 0).getBlock();
            
            if (above.getType() == Material.AIR && above.getLocation().add(0, - 2, 0).getBlock().getType() != Material.AIR) {
                above.setType(plugin.SUGAR_CANE_BLOCK);
            }

        }
        else if (below.getType() == Material.CACTUS) {
            Block above = e.getBlock().getLocation().add(0, 1, 0).getBlock();

            if (above.getType() == Material.AIR && above.getLocation().add(0, - 2, 0).getBlock().getType() != Material.AIR) {
                above.setType(Material.CACTUS);
            }
        }
    }
	
	
	
	//Spawner Upgrades Function Handling!!!
	@EventHandler
	public void onMobSpawn(SpawnerSpawnEvent e){
		Claim claim = griefPrevention.dataStore.getClaimAt(e.getLocation(), true, null);
		if (claim != null){
			String name = claim.getOwnerName();
			if (S1.contains(name)){
				lowerSpawnerDelay(e, s1);	return;		}
			if (S2.contains(name)){
				lowerSpawnerDelay(e, s2);	return;		}
			if (S3.contains(name)){
				lowerSpawnerDelay(e, s3);	return;		}
		}	}
    private void lowerSpawnerDelay(SpawnerSpawnEvent e, double multiplier) {
        int lowerby = (int) Math.round(e.getSpawner().getDelay() * multiplier);
        e.getSpawner().setDelay(e.getSpawner().getDelay() - lowerby);
    }
	
	public void refresh(){
		S1.clear(); S2.clear(); S3.clear(); F1.clear(); F2.clear(); F3.clear(); L1.clear(); L2.clear(); L3.clear(); Fu1.clear(); Fu2.clear(); Fu3.clear(); B1.clear(); B2.clear(); B3.clear();
		s1 = plugin.getConfig().getInt("s1");
		s2 = plugin.getConfig().getInt("s2");
		s3 = plugin.getConfig().getInt("s3");
		f1 = plugin.getConfig().getInt("f1");
		f2 = plugin.getConfig().getInt("f2");
		f3 = plugin.getConfig().getInt("f3");
		l1 = plugin.getConfig().getInt("l1");
		l2 = plugin.getConfig().getInt("l2");
		l3 = plugin.getConfig().getInt("l3");
		fu1 = plugin.getConfig().getInt("fu1");
		fu2 = plugin.getConfig().getInt("fu2");
		fu3 = plugin.getConfig().getInt("fu3");
		b1 = plugin.getConfig().getInt("b1");
		b2 = plugin.getConfig().getInt("b2");
		b3 = plugin.getConfig().getInt("b3");
		try{
			PreparedStatement foor = plugin.getConnection().prepareStatement("SELECT * FROM upgrades");
			ResultSet results = foor.executeQuery();
			while(results.next()){
				if (results.getInt("SPAWNER") == 1){S1.add(results.getString("NAME"));/* System.out.println("ADDING Player "+results.getString("NAME")+" To S1");*/}
				if (results.getInt("SPAWNER") == 2){S2.add(results.getString("NAME"));/* System.out.println("ADDING Player "+results.getString("NAME")+" To S2");*/}
				if (results.getInt("SPAWNER") == 3){S3.add(results.getString("NAME"));/* System.out.println("ADDING Player "+results.getString("NAME")+" To S3");*/}
				if (results.getInt("FARM") == 1){F1.add(results.getString("NAME"));/* System.out.println("ADDING Player "+results.getString("NAME")+" To F1");*/}
				if (results.getInt("FARM") == 2){F2.add(results.getString("NAME"));/* System.out.println("ADDING Player "+results.getString("NAME")+" To F2");*/}
				if (results.getInt("FARM") == 3){F3.add(results.getString("NAME"));/* System.out.println("ADDING Player "+results.getString("NAME")+" To F3");*/}
				if (results.getInt("LOOT") == 1){L1.add(results.getString("NAME"));/* System.out.println("ADDING Player "+results.getString("NAME")+" To L1");*/}
				if (results.getInt("LOOT") == 2){L2.add(results.getString("NAME"));/* System.out.println("ADDING Player "+results.getString("NAME")+" To L2");*/}
				if (results.getInt("LOOT") == 3){L3.add(results.getString("NAME"));/* System.out.println("ADDING Player "+results.getString("NAME")+" To L3");*/}
				if (results.getInt("FURNACE") == 1){Fu1.add(results.getString("NAME"));/* System.out.println("ADDING Player "+results.getString("NAME")+" To Fu1");*/}
				if (results.getInt("FURNACE") == 2){Fu2.add(results.getString("NAME"));/* System.out.println("ADDING Player "+results.getString("NAME")+" To Fu2");*/}
				if (results.getInt("FURNACE") == 3){Fu3.add(results.getString("NAME"));/* System.out.println("ADDING Player "+results.getString("NAME")+" To Fu3");*/}
				if (results.getInt("BREWING") == 1){B1.add(results.getString("NAME"));/* System.out.println("ADDING Player "+results.getString("NAME")+" To B1");*/}
				if (results.getInt("BREWING") == 2){B2.add(results.getString("NAME"));/* System.out.println("ADDING Player "+results.getString("NAME")+" To B2");*/}
				if (results.getInt("BREWING") == 3){B3.add(results.getString("NAME")); /*System.out.println("ADDING Player "+results.getString("NAME")+" To B3");*/}
			}
		}catch (SQLException error){
			error.printStackTrace();	}	}
	
	
}
