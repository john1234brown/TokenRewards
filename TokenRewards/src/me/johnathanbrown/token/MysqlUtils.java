package me.johnathanbrown.token;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;




public class MysqlUtils implements Listener{
	//Main plugin = Main.getPlugin("Main.class");
	
	private Main plugin;
	
	public MysqlUtils (Main mainInstance) {
	this.plugin = mainInstance;
	}
	
	@EventHandler
	public void PlayerCheck(PlayerJoinEvent e){
		Player player = e.getPlayer();
		
		createPlayer(player.getUniqueId(), player.getName().toString());
	}

	public boolean setUpgrade(UUID uuid, int i, int a){
		//Integer a is the type upgrade then the integer i is the amount for what level of the upgrade
		switch(a){
		case 0:
			try{
				PreparedStatement statement = plugin.getConnection().prepareStatement("UPDATE upgrades SET SPAWNER=?, SPAWNER=? WHERE UUID=?");
				statement.setInt(1, getUpgrade(uuid, 0));
				statement.setInt(2, i);
				statement.setString(3, uuid.toString());
				statement.executeUpdate();
				return true;
			}catch (SQLException e){	e.printStackTrace();	return false;	}
		case 1:
			try{
				PreparedStatement statement = plugin.getConnection().prepareStatement("UPDATE upgrades SET FARM=?, FARM=? WHERE UUID=?");
				statement.setInt(1, getUpgrade(uuid, 1));
				statement.setInt(2, i);
				statement.setString(3, uuid.toString());
				statement.executeUpdate();
				return true;
			}catch (SQLException e){	e.printStackTrace();	return false;	}
		case 2:
			try{
				PreparedStatement statement = plugin.getConnection().prepareStatement("UPDATE upgrades SET LOOT=?, LOOT=? WHERE UUID=?");
				statement.setInt(1, getUpgrade(uuid, 2));
				statement.setInt(2, i);
				statement.setString(3, uuid.toString());
				statement.executeUpdate();
				return true;
			}catch (SQLException e){	e.printStackTrace();	return false;	}
		case 3:
			try{
				PreparedStatement statement = plugin.getConnection().prepareStatement("UPDATE upgrades SET FURNACE=?, FURNACE=? WHERE UUID=?");
				statement.setInt(1, getUpgrade(uuid, 3));
				statement.setInt(2, i);
				statement.setString(3, uuid.toString());
				statement.executeUpdate();
				return true;
			}catch (SQLException e){	e.printStackTrace();	return false;	}
		case 4:
			try{
				PreparedStatement statement = plugin.getConnection().prepareStatement("UPDATE upgrades SET BREWING=?, BREWING=? WHERE UUID=?");
				statement.setInt(1, getUpgrade(uuid, 4));
				statement.setInt(2, i);
				statement.setString(3, uuid.toString());
				statement.executeUpdate();
				return true;
			}catch (SQLException e){	e.printStackTrace();	return false;	}
				}
return false;}
	
	public int getUpgrade(UUID uuid,int i){
		/* To get spawner upgrade specify 0 for the int i in the function  *
		 * Farming 1, Loot drop 2, Furnace 3, Brewing 4                    */
		int result = 1;
		switch (i) {
		//Spawner
		case 0:
			try{
			PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT SPAWNER FROM upgrades WHERE UUID=?");
			statement.setString(1, uuid.toString());
			ResultSet results = statement.executeQuery();
			if (results.next() == true){
			result = results.getInt("SPAWNER");}
			}catch (SQLException e){
				e.printStackTrace();
			}
			return result+1;
		//Farming
		case 1:
			try{
				PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT FARM FROM upgrades WHERE UUID=?");
				statement.setString(1, uuid.toString());
				ResultSet results = statement.executeQuery();
				if (results.next() == true){
				result = results.getInt("FARM");}
				}catch (SQLException e){
					e.printStackTrace();
				}
				return result+1;
		//Looting
		case 2:
			try{
				PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT LOOT FROM upgrades WHERE UUID=?");
				statement.setString(1, uuid.toString());
				ResultSet results = statement.executeQuery();
				if (results.next() == true){
				result = results.getInt("LOOT");}
				}catch (SQLException e){
					e.printStackTrace();
				}
				return result+1;
		//Furnace
		case 3:
			try{
				PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT FURNACE FROM upgrades WHERE UUID=?");
				statement.setString(1, uuid.toString());
				ResultSet results = statement.executeQuery();
				if (results.next() == true){
				result = results.getInt("FURNACE");}
				}catch (SQLException e){
					e.printStackTrace();
				}
				return result+1;
		//Brewing
		case 4:
			try{
				PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT BREWING FROM upgrades WHERE UUID=?");
				statement.setString(1, uuid.toString());
				ResultSet results = statement.executeQuery();
				if (results.next() == true){
				result = results.getInt("BREWING");}
				result = result+1;
				}catch (SQLException e){
					e.printStackTrace();
				}
				//System.out.println(result);
				return result;
			}
		
		return result;
		}
	
	public boolean PlayerExists(UUID uuid){
		try {
			PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT UUID FROM " + plugin.table + " WHERE UUID=?;");
			statement.setString(1, uuid.toString());
			
			ResultSet results = statement.executeQuery();
			
			if (results.next()){
				//System.out.println("Player Exists!");
				return true;}	} catch (SQLException e) {	e.printStackTrace();	}  return false;}
	
	public boolean NameChange(UUID uuid, String name){
		try {
		//if (PlayerExists(uuid)){
		PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT NAME FROM " + plugin.table + " WHERE UUID=?;");
		statement.setString(1, uuid.toString());
		ResultSet results = statement.executeQuery();
		if (results.next() == true){
		String ogname = results.getString("NAME");
		if(ogname.equalsIgnoreCase(name)){
			//System.out.println(ChatColor.GREEN +""+ ChatColor.BOLD+"[Tokens] Player name has not changed " + name);
			return false;}
		if(!(ogname.equalsIgnoreCase(name))){
			//System.out.println(ChatColor.GREEN +""+ ChatColor.BOLD+"[Tokens] Player name has not changed " + name);
			updatename(uuid, name, ogname);
			return true;} }	}catch (SQLException e) {	e.printStackTrace();	}	return true;	}
	
	public void updatename(final UUID uuid, String newname, String ogname){
		try{
			PreparedStatement set = plugin.getConnection().prepareStatement(
					"UPDATE "+plugin.table+" SET NAME="+ogname+", NAME="+newname+" WHERE UUID="+uuid.toString()+";");
			PreparedStatement up = plugin.getConnection().prepareStatement("UPDATE upgrades SET NAME="+ogname+", NAME="+newname+" WHERE UUID="+uuid.toString()+";");
			//System.out.println(ChatColor.DARK_PURPLE+""+ChatColor.BOLD+"[Tokens] Player name change detected updated " +ogname+ " to there new name "+newname+" in the database!");
			set.executeUpdate();	up.executeUpdate();}catch (SQLException nx){	nx.printStackTrace();	}	return;}   
	
	public void createPlayer(final UUID uuid, String player){
		try{
			PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.table + " WHERE UUID=?;");
			statement.setString(1, uuid.toString());
			ResultSet results = statement.executeQuery();
			results.next();
			if (PlayerExists(uuid) != true){
				PreparedStatement insert = plugin.getConnection().prepareStatement(
						"INSERT INTO " +plugin.table+" (UUID,NAME,TOKENS) VALUE (?,?,?)");
				insert.setString(1, uuid.toString());
				insert.setString(2, player);
				insert.setDouble(3, 0.00);
				insert.executeUpdate();
				PreparedStatement up = plugin.getConnection().prepareStatement("INSERT INTO upgrades "
						+ "(UUID,NAME,SPAWNER,FARM,LOOT,FURNACE,BREWING) VALUE (?,?,?,?,?,?,?)");
				up.setString(1, uuid.toString());
				up.setString(2, player);
				up.setInt(3, 0);
				up.setInt(4, 0);
				up.setInt(5, 0);
				up.setInt(6, 0);
				up.setInt(7, 0);
				up.executeUpdate();
				//System.out.println(ChatColor.GREEN +""+ ChatColor.BOLD+"[Tokens] Player name has not changed " + player);}
			}
			if (PlayerExists(uuid) == true){
				NameChange(uuid, player);
				//System.out.println("Running name change check!");
			}
			
		}catch (SQLException e){	e.printStackTrace();	}	return;}
	
	public double getTokens(final UUID uuid){
		double bal = -666.666;
		try{
			PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT TOKENS FROM " + plugin.table + " WHERE UUID=?");
			statement.setString(1, uuid.toString());
			ResultSet results = statement.executeQuery();
			if (results.next() == true){
			bal = results.getDouble("TOKENS");}
		}catch (SQLException e){
			e.printStackTrace();
		}
		if (bal == -666.666){System.out.println(ChatColor.DARK_RED+""+ChatColor.BOLD+"====================================");System.out.println(ChatColor.DARK_RED+""+ChatColor.BOLD+"[TOKENS ERROR Retrivieng player balance] "+uuid); System.out.println(ChatColor.DARK_RED+""+ChatColor.BOLD+"====================================");}
		return bal;
		
	}
	
	public boolean hasEnough(final UUID uuid, int a){
		double bal = -666.666;
		try{
			PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT TOKENS FROM " + plugin.table + " WHERE UUID=?");
			statement.setString(1, uuid.toString());
			ResultSet results = statement.executeQuery();
			if (results.next() == true){
			bal = results.getDouble("TOKENS");
			if (bal>=a){
				return true;
			}
			}
		}catch (SQLException e){
			e.printStackTrace();
		}
		return false;
		
	}
	
	public boolean addTokens(final UUID uuid, double amount){
		//if (!PlayerExists(uuid)){return false;/*createPlayer(uuid, player);	*/}
		if (PlayerExists(uuid)){	double bal = getTokens(uuid);	double Newbal = bal+amount;
			try{
			PreparedStatement set = plugin.getConnection().prepareStatement(
					"UPDATE "+plugin.table+" SET TOKENS=?, TOKENS=? WHERE UUID=?");
			set.setDouble(1, bal);
			set.setDouble(2, Newbal);
			set.setString(3, uuid.toString());
			set.executeUpdate();
			return true;
			}catch (SQLException e){e.printStackTrace();}		}	return false;}
	
	public boolean takeTokens(final UUID uuid, double amount){
		//if (!PlayerExists(uuid)){ return false; /*createPlayer(uuid, player);*/	}
		if (PlayerExists(uuid)){	double bal = getTokens(uuid);	double Newbal = bal-amount;
			if (Newbal>=0){
			try{
			PreparedStatement set = plugin.getConnection().prepareStatement(
					"UPDATE "+plugin.table+" SET TOKENS=?, TOKENS=? WHERE UUID=?");
			set.setDouble(1, bal);
			set.setDouble(2, Newbal);
			set.setString(3, uuid.toString());
			set.executeUpdate();
			return true;
			}catch (SQLException e){e.printStackTrace();}	}	}	return false;}
	
	public boolean resetTokens(final UUID uuid, String player){
		//if (!PlayerExists(uuid)){ return false;/*createPlayer(uuid, player);*/	}
		if (PlayerExists(uuid)){	double bal = getTokens(uuid);	double Newbal = 0;
			try{
			PreparedStatement set = plugin.getConnection().prepareStatement(
					"UPDATE "+plugin.table+" SET TOKENS=?, TOKENS=? WHERE UUID=?");
			set.setDouble(1, bal);
			set.setDouble(2, Newbal);
			set.setString(3, uuid.toString());
			set.executeUpdate();
			return true;
			}catch (SQLException e){e.printStackTrace();}	}	return false;}
	
}
