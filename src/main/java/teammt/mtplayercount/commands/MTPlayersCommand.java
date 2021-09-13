package teammt.mtplayercount.commands;

import org.bukkit.entity.Player;

import masecla.mlib.annotations.RegisterableInfo;
import masecla.mlib.annotations.RequiresPlayer;
import masecla.mlib.annotations.SubcommandInfo;
import masecla.mlib.classes.Registerable;
import masecla.mlib.classes.Replaceable;
import masecla.mlib.main.MLib;
import teammt.mtplayercount.classes.DelayedPlayerAdding;
import teammt.mtplayercount.classes.PlayercountManagement;
import teammt.mtplayercount.container.AddPlayersContainer;

@RequiresPlayer
@RegisterableInfo(command = "mtplayers")
public class MTPlayersCommand extends Registerable {

	private PlayercountManagement playercount;

	public MTPlayersCommand(MLib lib, PlayercountManagement playercount) {
		super(lib);
		this.playercount = playercount;
	}

	@SubcommandInfo(subcommand = "addplayers", permission = "mtplayers.admin.addplayers")
	public void addPlayers(Player p, String playerCount) {
		int players = 0;
		try {
			players = Integer.parseInt(playerCount);
		} catch (NumberFormatException e) {
			lib.getMessagesAPI().sendMessage("error.invalid-playercount", p, new Replaceable("%players%", playerCount));
			return;
		}
		new DelayedPlayerAdding(lib, 0, players, playercount, p).register();
		lib.getMessagesAPI().sendMessage("adding-players", p, new Replaceable("%players%", players),
				new Replaceable("%seconds%", 0));
	}

	@SubcommandInfo(subcommand = "addplayers", permission = "mtplayers.admin.addplayers")
	public void addPlayersContainer(Player p) {
		lib.getContainerAPI().openFor(p, AddPlayersContainer.class);
	}

	@SubcommandInfo(subcommand = "addplayers", permission = "mtplayers.admin.addplayers")
	public void addPlayersDelay(Player p, String playerCount, String delayCount) {
		int players = 0;
		try {
			players = Integer.parseInt(playerCount);
		} catch (NumberFormatException e) {
			lib.getMessagesAPI().sendMessage("error.invalid-playercount", p, new Replaceable("%players%", playerCount));
			return;
		}
		int delay = 0;
		try {
			delay = Integer.parseInt(delayCount);
		} catch (NumberFormatException e) {
			lib.getMessagesAPI().sendMessage("error.invalid-playercount", p, new Replaceable("%players%", playerCount));
			return;
		}
		new DelayedPlayerAdding(lib, 0, players, playercount, p).register();
		lib.getMessagesAPI().sendMessage("adding-players", p, new Replaceable("%players%", players),
				new Replaceable("%seconds%", delay));
	}

}
