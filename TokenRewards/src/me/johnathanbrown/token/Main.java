package me.johnathanbrown.token;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.PlayerData;


public class Main extends JavaPlugin implements Listener {

	private Connection connection;
	public String host, database ,username, password, table;
	public int port;
	private static GriefPrevention griefPrevention = GriefPrevention.instance;
	static HashMap<String, Integer> NewTest = new HashMap<String, Integer>();
    public Material SUGAR_CANE_BLOCK, BANNER, CROPS, REDSTONE_LAMP_ON,
    STAINED_GLASS, STATIONARY_WATER, STAINED_CLAY, WOOD_BUTTON,
    SOIL, MOB_SPANWER, THIN_GLASS, IRON_FENCE, NETHER_FENCE, FENCE,
    WOODEN_DOOR, TRAP_DOOR, FENCE_GATE, BURNING_FURNACE, DIODE_BLOCK_OFF,
    DIODE_BLOCK_ON, ENCHANTMENT_TABLE, FIREBALL;
	private Upgrades uputils;
	private MysqlUtils sqlutils;
	public int st1, st2, st3, ft1, ft2, ft3, lt1, lt2, lt3, fut1, fut2, fut3, bt1, bt2, bt3, cbt, rpt1, rpt2, rpt3;
	
	public void onEnable(){
		 if (!Bukkit.getPluginManager().isPluginEnabled("GriefPrevention")) {
		        getLogger().severe("*** GriefPrevention is not installed or not enabled. ***");
		        getLogger().severe("*** This plugin will be disabled. ***");
		        this.setEnabled(false);
		        return;
		 }
		registerGlow();
		loadConfig();
		mysqlSetup();
		Bukkit.getPluginManager().registerEvents(new MysqlUtils(this), this);
		Bukkit.getPluginManager().registerEvents(new Upgrades(this), this);
		Bukkit.getPluginManager().registerEvents(this, this);
		CROPS = Material.valueOf("CROPS");
        SUGAR_CANE_BLOCK = Material.valueOf("SUGAR_CANE_BLOCK");
        SOIL = Material.valueOf("SOIL");
        MOB_SPANWER = Material.valueOf("MOB_SPAWNER");
        BURNING_FURNACE = Material.valueOf("BURNING_FURNACE");
        ENCHANTMENT_TABLE = Material.valueOf("ENCHANTMENT_TABLE");
        FIREBALL = Material.valueOf("FIREBALL");
        uputils = new Upgrades(this);
        sqlutils = new MysqlUtils(this);
        reloadPrices();
	}
	
	public void runnable(final Block block, final int e){
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
              uputils.setCookSpeedFast(block, e);
            }
        }, 20L);
	}
	
	public void runnableB(final Block block, final int e){
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
              uputils.setBrewSpeedFast(block, e);
            }
        }, 20L);
	}
	
	public void onDisable(){
		Bukkit.getPluginManager().disablePlugin(this);
	}
	//private MysqlUtils sqlutils = new MysqlUtils(this);
	//private Upgrades uputils = new Upgrades(this);
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if (cmd.getName().equalsIgnoreCase("atokens")){
			
			if (args.length == 0){
				sender.sendMessage(ChatColor.RED +"Incorrect usage of command do /atokens help for help on commands");
				return true;
			}
			
			if (args.length == 1){
				if (sender.hasPermission("tokens.admin")){
				if (args[0].toString().equalsIgnoreCase("help")){
					sender.sendMessage(ChatColor.GREEN+"/atokens take <playername> <amount> take tokens from a player");	
					sender.sendMessage(ChatColor.GREEN+"/atokens give <playername> <amount> add tokens to someone :D");
					sender.sendMessage(ChatColor.GREEN+"/atokens reset <playername> will set a players tokens to 0");
					return true; }
				if (args[0].toString().equalsIgnoreCase("reload")){
					reloadConfig();
					
					mysqlSetup();
					
					uputils.refresh();
					reloadPrices();
					sender.sendMessage(ChatColor.GREEN+"[Tokens Config] Config File has been reloaded!");	
					return true; }
				}
			}
				
			
			if (args.length == 2){
			if (sender.hasPermission("tokens.admin")){
				if (args[0].equalsIgnoreCase("reset")){
						OfflinePlayer otherplayer = Bukkit.getServer().getOfflinePlayer(args[1].toString());
						if (!(sqlutils.PlayerExists(otherplayer.getUniqueId()))){
							sender.sendMessage(ChatColor.RED+""+ChatColor.BOLD+"[Tokens Error] Please check your captilization of letters and spelling it is Case Sensitive!");
						return false; }
					if (sqlutils.PlayerExists(otherplayer.getUniqueId())){
						sqlutils.resetTokens(otherplayer.getUniqueId(), otherplayer.getName().toString());
						sender.sendMessage(ChatColor.GREEN+""+ChatColor.BOLD+"[Tokens Admin] Reset "+otherplayer.getName().toString()+" tokens to 0 successfully!" );
						return true;	};	return false;}
			}	return false;} 
			
			if (args.length == 3){
				if (sender.hasPermission("tokens.admin")){
				if (args[0].toString().equalsIgnoreCase("give")){
						OfflinePlayer otherplayer = Bukkit.getServer().getOfflinePlayer(args[1].toString());
						sqlutils.addTokens(otherplayer.getUniqueId(), Double.parseDouble(args[2].toString().replaceAll("-", "")));
						return true;
					}
				
				if (args[0].toString().equalsIgnoreCase("take")){
						OfflinePlayer otherplayer = Bukkit.getServer().getOfflinePlayer(args[1].toString());
						sqlutils.takeTokens(otherplayer.getUniqueId(), Double.parseDouble(args[2].toString().replaceAll("-", "")));
						return true; }
				}
				}
		return false;}

		if (cmd.getName().equalsIgnoreCase("tokens")){
			if (args.length == 0){
				sender.sendMessage(ChatColor.RED +"Incorrect usage of command do /tokens help for help on commands");
			}
			
			if (args.length == 1){
				if (args[0].toString().equalsIgnoreCase("help")){
					sender.sendMessage(ChatColor.GREEN+"/tokens bal <playername> gets your current bal if no playername specified gets other players bal if specified");	
					sender.sendMessage(ChatColor.GREEN+"/tokens pay <playername> <amount> pay someone some of your tokens :D");
				
				}
				
				if(sender instanceof Player){
					Player player = (Player) sender;
				if (args[0].toString().equalsIgnoreCase("balance") || args[0].toString().equalsIgnoreCase("bal")){
					if (sqlutils.PlayerExists((player.getUniqueId()))){
						double balance = sqlutils.getTokens(player.getUniqueId());
						player.sendMessage(ChatColor.GREEN+""+ChatColor.BOLD+"[Tokens Balance] " + player.getName()+ ": " + balance);
						return true;
					}else{
						player.sendMessage(ChatColor.RED+""+ChatColor.BOLD+"[Tokens Database] You do not exist in the database please contact a Staff ASAP! with screenshot of this!");
						sqlutils.createPlayer(player.getUniqueId(), player.getName().toString());
						return false;	}	}
				if (args[0].toString().equalsIgnoreCase("shop")){
					if (sqlutils.PlayerExists((player.getUniqueId()))){
					OpenMainGui(player);
					}else{
						player.sendMessage(ChatColor.RED+""+ChatColor.BOLD+"[Tokens Database] You do not exist in the database please contact a Staff ASAP! with screenshot of this!");
						sqlutils.createPlayer(player.getUniqueId(), player.getName().toString());
					}	}	
				}			}
			if (args.length == 2){
				if (args[0].toString().equalsIgnoreCase("bal") || args[0].toString().equalsIgnoreCase("balance")){
					if (!(args[1].toString().equals(" "))){
					OfflinePlayer otherplayer = Bukkit.getServer().getOfflinePlayer(args[1].toString());
					if (!(sqlutils.PlayerExists(otherplayer.getUniqueId()))){
						sender.sendMessage(ChatColor.RED+""+ChatColor.BOLD+"[Tokens Error] Please check your captilization of letters and spelling it is Case Sensitive!");
						return false;	}
					if (sqlutils.PlayerExists(otherplayer.getUniqueId())){
						double balance = sqlutils.getTokens(otherplayer.getUniqueId());
						sender.sendMessage(ChatColor.GREEN+""+ChatColor.BOLD+"[Tokens Balance] "+ otherplayer.getName()+": "+balance);
						return true;	}	return false;}	 }
		}
			if (args.length == 3){
				if (sender instanceof Player){
					Player player = (Player) sender;
					if (args[0].toString().equalsIgnoreCase("pay")){
					OfflinePlayer otherplayer = Bukkit.getServer().getOfflinePlayer(args[1].toString());
					if (sqlutils.PlayerExists(otherplayer.getUniqueId())){
					//Transaction check
					if (sqlutils.getTokens(player.getUniqueId()) >= Double.parseDouble(args[2].toString())){
						sqlutils.takeTokens(player.getUniqueId(), Double.parseDouble(args[2].toString().replaceAll("-", "")));
						sqlutils.addTokens(otherplayer.getUniqueId(), Double.parseDouble(args[2].toString().replaceAll("-", "")));
						return true;
						}else{player.sendMessage(ChatColor.RED+"[Tokens Error] Could not transfer the tokens please check you have enough"); return false; }
					}else {sender.sendMessage(ChatColor.RED+""+ChatColor.BOLD+"[Tokens Error] Please check you have the right username the player was not found in the database! Case sensitive!"); return false;}
					}	} return false;}
		}	return false;}
	
	
	//Mysql And config file utilies
	public void loadConfig(){
		File config = new File(getDataFolder(), "config.yml");
	    if (config.exists())
	    {
	    	System.out.println("[Tokens] Config already generated!");
	    }
	    
	    if (!config.exists())
	    {
	    	System.out.println("[Tokens] Config being generated!");
	    	saveDefaultConfig();
	    }}
	
	
	
	@EventHandler
	public void TokenShopFunction(InventoryClickEvent e){
		if (e.getClickedInventory() == null){return;}
		if (e.getCurrentItem().getType() == Material.AIR || e.getCurrentItem() == null){return;}
		if (e.getClickedInventory().getName().equalsIgnoreCase(ChatColor.GREEN + "" + ChatColor.BOLD + "Tokens Shop")){
			if (e.getCurrentItem() != null){
				e.setCancelled(true);
				String itemname = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
				int q = e.getCurrentItem().getAmount();
				Player player = (Player) e.getWhoClicked();
				UUID uuid = player.getUniqueId();
				String playername = player.getName();
				int priceee=666999;
				if (itemname.contains("Spawner")){
					switch (q){case 1: priceee=st1;break; case 2: priceee=st2;break; case 3: priceee=st3;break;}
					if (priceee==666999 || q==4){player.sendMessage(ChatColor.RED+"[Tokens Transaction] You already have the max upgrade!"); return;}
					if (sqlutils.takeTokens(uuid, priceee)){
						sqlutils.setUpgrade(uuid, q, 0);
						player.sendMessage(ChatColor.GREEN+"[Tokens Transaction] You have successfuly bought the upgrade!");
						e.getView().close();
						return;
					}else{player.sendMessage(ChatColor.RED+"[Tokens Transaction] Error you do not have enough tokens for this upgrade!");}
					e.getView().close();
					return;
				}
				if (itemname.contains("Farming")){
					switch (q){case 1: priceee=ft1;break; case 2: priceee=ft2;break; case 3: priceee=ft3;break;}
					if (priceee==666999 || q==4){player.sendMessage(ChatColor.RED+"[Tokens Transaction] You already have the max upgrade!"); return;}
					if (sqlutils.takeTokens(uuid, priceee)){
						sqlutils.setUpgrade(uuid, q, 1);
						player.sendMessage(ChatColor.GREEN+"[Tokens Transaction] You have successfuly bought the upgrade!");
						e.getView().close();
						return;
					}else{player.sendMessage(ChatColor.RED+"[Tokens Transaction] Error you do not have enough tokens for this upgrade!");}
					e.getView().close();
					return;
				}
				if (itemname.contains("Loot")){
					switch (q){case 1: priceee=fut1;break; case 2: priceee=fut2;break; case 3: priceee=fut3;break;}
					if (priceee==666999 || q==4){player.sendMessage(ChatColor.RED+"[Tokens Transaction] You already have the max upgrade!"); return;}
					if (sqlutils.takeTokens(uuid, priceee)){
						sqlutils.setUpgrade(uuid, q, 2);
						player.sendMessage(ChatColor.GREEN+"[Tokens Transaction] You have successfuly bought the upgrade!");
						e.getView().close();
						return;
					}else{player.sendMessage(ChatColor.RED+"[Tokens Transaction] Error you do not have enough tokens for this upgrade!");}
					e.getView().close();
					return;
				}
				if (itemname.contains("Furnace")){
					switch (q){case 1: priceee=st1;break; case 2: priceee=st2;break; case 3: priceee=st3;break;}
					if (priceee==666999 || q==4){player.sendMessage(ChatColor.RED+"[Tokens Transaction] You already have the max upgrade!"); return;}
					if (sqlutils.takeTokens(uuid, priceee)){
						sqlutils.setUpgrade(uuid, q, 3);
						player.sendMessage(ChatColor.GREEN+"[Tokens Transaction] You have successfuly bought the upgrade!");
						e.getView().close();
						return;
					}else{player.sendMessage(ChatColor.RED+"[Tokens Transaction] Error you do not have enough tokens for this upgrade!");}
					e.getView().close();
					return;
				}
				if (itemname.contains("Brewing")){
					switch (q){case 1: priceee=bt1;break; case 2: priceee=bt2;break; case 3: priceee=bt3;break;}
					if (priceee==666999 || q==4){player.sendMessage(ChatColor.RED+"[Tokens Transaction] You already have the max upgrade!"); return;}
					if (sqlutils.takeTokens(uuid, priceee)){
						sqlutils.setUpgrade(uuid, q, 4);
						player.sendMessage(ChatColor.GREEN+"[Tokens Transaction] You have successfuly bought the upgrade!");
						e.getView().close();
						return;
					}else{player.sendMessage(ChatColor.RED+"[Tokens Transaction] Error you do not have enough tokens for this upgrade!");}
					e.getView().close();
					return;
				}
			
				if (itemname.contains("RANK")){
					switch (q){case 1: priceee=rpt1;break; case 2: priceee=rpt2;break; case 3: priceee=rpt3;break;}
					if (priceee==666999 || q==3){player.sendMessage(ChatColor.RED+"[Tokens Transaction] You already have the max upgrade!"); return;}
					if (sqlutils.takeTokens(uuid, priceee)){
						if (player.hasPermission("tokens.staff")){
							if (q == 1){Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "groupmanager:manuaddsub "+playername + " VIP");}
							if (q == 2){Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "groupmanager:manudelsub "+playername + " VIP"); Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "groupmanager:manuaddsub "+playername + " VIP+");}
							player.sendMessage(ChatColor.GREEN+"[Tokens Transaction] You have successfully bought a rank upgrade!");
							e.getView().close(); return;
						}else{
							if (q == 1){Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "groupmanager:manuadd "+playername + " VIP");}
							if (q == 2){Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "groupmanager:manuadd "+playername + " VIP+");}
							player.sendMessage(ChatColor.GREEN+"[Tokens Transaction] You have successfully bought a rank upgrade!");
							e.getView().close(); return;
						}
					}else{player.sendMessage(ChatColor.RED+"[Tokens Transaction] Error you do not have enough tokens for this upgrade!"); return;}
				}	}		}	}
	
	
	
	
	private void OpenMainGui(Player player) {
		Inventory inv = Bukkit.getServer().createInventory(null, 27,
				ChatColor.GREEN + "" + ChatColor.BOLD + "Tokens Shop");
		Glow glow = new Glow(666);
		String playername = player.getName();
		int ranklevel = 0;
		if (player.hasPermission("tokens.vip")){ranklevel=1;}
		if (player.hasPermission("tokens.vip+")){ranklevel=2;}
		if (player.hasPermission("tokens.vip++")){ranklevel=3;}
		
		ItemStack border1 = new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)9);
		ItemMeta bo1meta = border1.getItemMeta();
		ItemStack border2 = new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)0);
		ItemMeta bo2meta = border2.getItemMeta();
		ItemStack border3 = new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)5);
		ItemMeta bo3meta = border3.getItemMeta();
		ItemStack border4 = new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)6);
		ItemMeta bo4meta = border4.getItemMeta();
		ItemStack spawner = new ItemStack(Material.MOB_SPAWNER, sqlutils.getUpgrade(player.getUniqueId(), 0));
		ItemMeta spawnmeta = spawner.getItemMeta();
		ItemStack farm = new ItemStack(Material.WHEAT, sqlutils.getUpgrade(player.getUniqueId(), 1));
		ItemMeta farmmeta = farm.getItemMeta();
		ItemStack loot = new ItemStack(Material.BONE, sqlutils.getUpgrade(player.getUniqueId(), 2));
		ItemMeta lootmeta = loot.getItemMeta();
		ItemStack furnace = new ItemStack(Material.FURNACE, sqlutils.getUpgrade(player.getUniqueId(), 3));
		ItemMeta furnmeta = furnace.getItemMeta();
		ItemStack brewing = new ItemStack(Material.BREWING_STAND_ITEM, sqlutils.getUpgrade(player.getUniqueId(), 4));
		ItemMeta brewmeta = brewing.getItemMeta();
		ItemStack claim = new ItemStack(Material.SNOW_BLOCK, 1);
		ItemMeta claimmeta = claim.getItemMeta();
		ItemStack rank = new ItemStack(Material.PAPER, ranklevel);
		ItemMeta rankmeta = rank.getItemMeta();
			
		bo1meta.setDisplayName(ChatColor.GREEN+"Tokens: "+sqlutils.getTokens(player.getUniqueId())); bo1meta.addEnchant(glow, 1, true);
		border1.setItemMeta(bo1meta);
		bo2meta.setDisplayName(ChatColor.GREEN+"Tokens: "+sqlutils.getTokens(player.getUniqueId())); bo2meta.addEnchant(glow, 1, true);
		border2.setItemMeta(bo2meta);
		bo3meta.setDisplayName(ChatColor.GREEN+"Tokens: "+sqlutils.getTokens(player.getUniqueId())); bo3meta.addEnchant(glow, 1, true);
		border3.setItemMeta(bo3meta);
		bo4meta.setDisplayName(ChatColor.GREEN+"Tokens: "+sqlutils.getTokens(player.getUniqueId())); bo4meta.addEnchant(glow, 1, true);
		border4.setItemMeta(bo4meta);
		spawnmeta.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Spawner Rate: " + playername);
		spawnmeta.setLore(Arrays.asList(ChatColor.GOLD+"        "+ChatColor.UNDERLINE+"Tier 1: "+ChatColor.GOLD+""+ChatColor.BOLD + st1 +" Tokens",
				ChatColor.GRAY+"10% Increase in spawn rate.", 
				ChatColor.GOLD+"        "+ChatColor.UNDERLINE+"Tier 2: "+ChatColor.GOLD+""+ChatColor.BOLD + st2 +" Tokens", 
				ChatColor.GRAY+"20% Increase in spawn rate.", 
				ChatColor.GOLD+"        "+ChatColor.UNDERLINE+"Tier 3: "+ChatColor.GOLD+""+ChatColor.BOLD + st3 +" Tokens", 
				ChatColor.GRAY+"30% Increase in spawn rate.",
				ChatColor.GREEN+"Increases spawn rate in your claimed land"));
		if (spawner.getAmount() == 4){spawnmeta.addEnchant(glow, 1, true);}
		spawner.setItemMeta(spawnmeta);
		farmmeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Farming Speed: " + playername);
		farmmeta.setLore(Arrays.asList(ChatColor.GOLD+"        "+ChatColor.UNDERLINE+"Tier 1: "+ChatColor.GOLD+""+ChatColor.BOLD + ft1 +" Tokens",
				ChatColor.GRAY+"20% Increase in farm growth rate.", 
				ChatColor.GOLD+"        "+ChatColor.UNDERLINE+"Tier 2: "+ChatColor.GOLD+""+ChatColor.BOLD + ft2 +" Tokens", 
				ChatColor.GRAY+"30% Increase in farm growth rate.", 
				ChatColor.GOLD+"        "+ChatColor.UNDERLINE+"Tier 3: "+ChatColor.GOLD+""+ChatColor.BOLD + ft3 +" Tokens", 
				ChatColor.GRAY+"50% Increase in farm growth rate.",
				ChatColor.GREEN+"Increases farming speed in your claimed land"));
		if (farm.getAmount() == 4){farmmeta.addEnchant(glow, 1, true);}
		farm.setItemMeta(farmmeta);
		lootmeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Loot Drops: " + playername);
		lootmeta.setLore(Arrays.asList(ChatColor.GOLD+"        "+ChatColor.UNDERLINE+"Tier 1: "+ChatColor.GOLD+""+ChatColor.BOLD + lt1 +" Tokens",
				ChatColor.GRAY+"1.5x Multiplier Loot Drop.", 
				ChatColor.GOLD+"        "+ChatColor.UNDERLINE+"Tier 2: "+ChatColor.GOLD+""+ChatColor.BOLD + lt2 +" Tokens", 
				ChatColor.GRAY+"2.0x Multiplier Loot Drop.", 
				ChatColor.GOLD+"        "+ChatColor.UNDERLINE+"Tier 3: "+ChatColor.GOLD+""+ChatColor.BOLD + lt3 +" Tokens", 
				ChatColor.GRAY+"3.0x Multiplier Loot Drop.",
				ChatColor.GREEN+"Increases Loot Drop in your claimed land"));
		if (loot.getAmount() == 4){lootmeta.addEnchant(glow, 1, true);}
		loot.setItemMeta(lootmeta);
		furnmeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Furnace Speed: " + playername);
		furnmeta.setLore(Arrays.asList(ChatColor.GOLD+"        "+ChatColor.UNDERLINE+"Tier 1: "+ChatColor.GOLD+""+ChatColor.BOLD + fut1 +" Tokens",
				ChatColor.GRAY+"30% Increase in furnace smelt speed.", 
				ChatColor.GOLD+"        "+ChatColor.UNDERLINE+"Tier 2: "+ChatColor.GOLD+""+ChatColor.BOLD + fut2 +" Tokens", 
				ChatColor.GRAY+"60% Increase in furnace smelt speed.", 
				ChatColor.GOLD+"        "+ChatColor.UNDERLINE+"Tier 3: "+ChatColor.GOLD+""+ChatColor.BOLD + fut3 +" Tokens", 
				ChatColor.GRAY+"80% Increase in furnace smelt speed.",
				ChatColor.GREEN+"Increases furnace smelting speed in your claimed land"));
		if (furnace.getAmount() == 4){furnmeta.addEnchant(glow, 1, true);}
		furnace.setItemMeta(furnmeta);
		brewmeta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Brewing Speed: " + playername);
		brewmeta.setLore(Arrays.asList(ChatColor.GOLD+"        "+ChatColor.UNDERLINE+"Tier 1: "+ChatColor.GOLD+""+ChatColor.BOLD + bt1 +" Tokens",
				ChatColor.GRAY+"30% Increase in brewing speed.", 
				ChatColor.GOLD+"        "+ChatColor.UNDERLINE+"Tier 2: "+ChatColor.GOLD+""+ChatColor.BOLD + bt2 +" Tokens", 
				ChatColor.GRAY+"60% Increase in brewing speed.", 
				ChatColor.GOLD+"        "+ChatColor.UNDERLINE+"Tier 3: "+ChatColor.GOLD+""+ChatColor.BOLD + bt3 +" Tokens", 
				ChatColor.GRAY+"80% Increase in brewing speed.",
				ChatColor.GREEN+"Increases brewing speed in your claimed land"));
		if (brewing.getAmount() == 4){brewmeta.addEnchant(glow, 1, true);}
		brewing.setItemMeta(brewmeta);
		claimmeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Claim Blocks: " + playername);
		claimmeta.setLore(Arrays.asList(ChatColor.GREEN+""+ChatColor.BOLD+""+cbt+" Tokens",ChatColor.RED+""+ChatColor.BOLD+"200 claim blocks"));
		claim.setItemMeta(claimmeta);
		rankmeta.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "RANK UPGRADE: "+ChatColor.DARK_RED+""+ChatColor.BOLD+playername);
		rankmeta.setLore(Arrays.asList(ChatColor.GOLD+"        "+ChatColor.UNDERLINE+"Tier 1: "+ChatColor.GOLD+""+ChatColor.BOLD + rpt1 +" Tokens",
				ChatColor.GRAY+"Get the VIP Rank.", 
				ChatColor.GOLD+"        "+ChatColor.UNDERLINE+"Tier 2: "+ChatColor.GOLD+""+ChatColor.BOLD + rpt2 +" Tokens", 
				ChatColor.GRAY+"Get the VIP+ Rank.",  
				ChatColor.GREEN+"Donator ranks are not buyable via irl currencys."));
		if (ranklevel == 3){rankmeta.addEnchant(glow, 1, true);}
		rank.setItemMeta(rankmeta);
		inv.setItem(0, border1); inv.setItem(1, border2); inv.setItem(2, border3);
		inv.setItem(3, border1); inv.setItem(4, border4); inv.setItem(5, border1);
		inv.setItem(6, border2); inv.setItem(7, border4); inv.setItem(8, border1); inv.setItem(9, border3);
		inv.setItem(10, spawner);
		inv.setItem(11, farm);
		inv.setItem(12, loot);
		inv.setItem(14, furnace);
		inv.setItem(13, border3);
		inv.setItem(15, brewing);
		inv.setItem(16, rank);
		inv.setItem(17, border3); inv.setItem(18, border1); inv.setItem(19, border4); inv.setItem(20, border2);
		inv.setItem(21, border4); inv.setItem(22, border3); inv.setItem(23, border1);
		inv.setItem(24, border4); inv.setItem(25, border1); inv.setItem(26, border2);
		
		player.openInventory(inv);
		
 	return;}
	
	public void mysqlSetup(){
	    host = this.getConfig().getString("host");
	    port = this.getConfig().getInt("port");
	    database = this.getConfig().getString("database");
	    username = this.getConfig().getString("username");
	    password = this.getConfig().getString("password");
	    table = this.getConfig().getString("table");
	    try{
	        synchronized (this){
		    	//MySQL MySQL = new MySQL("host.name", port, "user", "pass");
		    	Connection c = null;
	        if(getConnection() != null && !getConnection().isClosed()){
	        return;
	        }
	    	Class.forName("com.mysql.jdbc.Driver");
	        	setConnection( DriverManager.getConnection("jdbc:mysql://" + this.host +":"+ this.port +"/"+ this.database, this.username, this.password));
	        	System.out.println(ChatColor.GREEN +""+ ChatColor.BOLD+"[TokenRewards] Mysql Connected successfully!");	}
	        }catch(SQLException e){	  e.printStackTrace();	}catch(ClassNotFoundException e){	e.printStackTrace();	}	 createTables();	}
	
	public void createTables(){
        try {
            PreparedStatement set = this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS "+table+" (UUID CHAR(200),NAME CHAR(16),TOKENS DOUBLE,PRIMARY KEY (UUID))");
            set.executeUpdate();
            PreparedStatement set2 = this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS upgrades (UUID CHAR(200),NAME CHAR(16),SPAWNER INT,FARM INT,LOOT INT,FURNACE INT,BREWING INT,PRIMARY KEY (UUID))");
            set2.executeUpdate();
        } catch (SQLException e) {            e.printStackTrace();	}	}
	
	public Connection getConnection(){	    return connection;	}
	    
	public void setConnection(Connection connection){	    this.connection = connection;}
	
	public void registerGlow() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Glow glow = new Glow(666);
            Enchantment.registerEnchantment(glow);
        }
        catch (IllegalArgumentException e){
        }
        catch(Exception e){
            e.printStackTrace();
        }
    return;}

	public void reloadPrices() {
		st1 = getConfig().getInt("spawner1");
		st2 = getConfig().getInt("spawner2");
		st3 = getConfig().getInt("spawner3");
		ft1 = getConfig().getInt("farm1");
		ft2 = getConfig().getInt("farm2");
		ft3 = getConfig().getInt("farm3");
		lt1 = getConfig().getInt("loot1");
		lt2 = getConfig().getInt("loot2");
		lt3 = getConfig().getInt("loot3");
		fut1 = getConfig().getInt("furnace1");
		fut2 = getConfig().getInt("furnace2");
		fut3 = getConfig().getInt("furnace3");
		bt1 = getConfig().getInt("brewing1");
		bt2 = getConfig().getInt("brewing2");
		bt3 = getConfig().getInt("brewing3");
		cbt = getConfig().getInt("claimblock");
		rpt1 = getConfig().getInt("rankupgrade1");
		rpt2 = getConfig().getInt("rankupgrade2");
		rpt3 = getConfig().getInt("rankupgrade3");
	return;}
}
