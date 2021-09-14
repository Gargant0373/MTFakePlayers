package teammt.mtplayercount.classes;

import org.bukkit.Bukkit;

import masecla.mlib.main.MLib;

public class PlayercountManagement {

	private MLib lib;

	private int playersAdded = 0;

	public PlayercountManagement(MLib lib) {
		this.lib = lib;
	}

	public int getOnlinePlayers() {
		int playerNumber = 0;
		String path = "online-players";
		if (lib.getConfigurationAPI().getConfig().isString(path)) {
			String unparsedPlayers = (String) lib.getConfigurationAPI().getConfig().get(path);

			unparsedPlayers = unparsedPlayers.replace("%players%", Bukkit.getOnlinePlayers().size() + "");

			try {
				playerNumber = Integer.parseInt(unparsedPlayers);
			} catch (NumberFormatException e) {
				playerNumber = (int) lib.getRandomnessAPI().eval(unparsedPlayers);
			}
		}
		if (lib.getConfigurationAPI().getConfig().isInt(path))
			playerNumber = lib.getConfigurationAPI().getConfig().getInt(path);
		if (lib.getConfigurationAPI().getConfig().isDouble(path)) {
			playerNumber = (int) lib.getConfigurationAPI().getConfig().getDouble(path);
		}
		if (playerNumber < 0) {
			playerNumber = 0;
			lib.getLoggerAPI().error("Online players are smaller than 0.");
		}
		if (playerNumber + playersAdded < 0) {
			playersAdded = 0;
		}
		return playerNumber + playersAdded;
	}

	public int getMaximumPlayers() {
		int playerNumber = 0;
		String path = "maximum-players";
		if (lib.getConfigurationAPI().getConfig().isString(path)) {
			String unparsedPlayers = (String) lib.getConfigurationAPI().getConfig().get(path);

			if (unparsedPlayers.contains("%players%"))
				unparsedPlayers.replace("%players%", Bukkit.getOnlinePlayers() + "");

			try {
				playerNumber = Integer.parseInt(unparsedPlayers);
			} catch (NumberFormatException e) {
				playerNumber = (int) lib.getRandomnessAPI().eval(unparsedPlayers);
			}
		}
		if (lib.getConfigurationAPI().getConfig().isInt(path))
			playerNumber = lib.getConfigurationAPI().getConfig().getInt(path);
		if (lib.getConfigurationAPI().getConfig().isDouble(path)) {
			playerNumber = (int) lib.getConfigurationAPI().getConfig().getDouble(path);
		}
		if (playerNumber < 0) {
			playerNumber = 0;
			lib.getLoggerAPI().error("Maximum players are smaller than 0.");
		}
		return playerNumber;
	}

	public int getAddedPlayers() {
		return this.playersAdded;
	}

	public void setAddedPlayers(int number) {
		this.playersAdded = number;
	}

	public void setOnlinePlayers(String value) {
		lib.getConfigurationAPI().getConfig().set("online-players", value);
	}

	public void setMaximumPlayers(String value) {
		lib.getConfigurationAPI().getConfig().set("maximum-players", value);
	}

	public void incrementOnlinePlayers(int amount) {
		this.playersAdded += amount;
	}
}
