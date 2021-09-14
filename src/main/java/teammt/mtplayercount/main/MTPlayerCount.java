package teammt.mtplayercount.main;

import org.bukkit.plugin.java.JavaPlugin;

import masecla.mlib.main.MLib;
import teammt.mtplayercount.classes.PlayercountManagement;
import teammt.mtplayercount.commands.MTPlayersCommand;
import teammt.mtplayercount.container.AddPlayersContainer;
import teammt.mtplayercount.container.SettingsContainer;
import teammt.mtplayercount.listeners.ServerPingListener;

public class MTPlayerCount extends JavaPlugin {

	private MLib lib;

	@Override
	public void onEnable() {
		this.lib = new MLib(this);
		lib.getConfigurationAPI().requireAll();

		PlayercountManagement playercount = new PlayercountManagement(lib);

		// Register Containers
		new AddPlayersContainer(lib, playercount).register();
		new SettingsContainer(lib, playercount).register();

		// Register Commands
		new MTPlayersCommand(lib, playercount).register();

		new ServerPingListener(lib, playercount).register();
	}
}
