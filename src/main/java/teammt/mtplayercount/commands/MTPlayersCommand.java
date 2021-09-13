package teammt.mtplayercount.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
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
	public void addPlayersContainer(Player p) {
		lib.getContainerAPI().openFor(p, AddPlayersContainer.class);
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
		lib.getMessagesAPI().sendMessage("doing.adding-players", p, new Replaceable("%players%", players),
				new Replaceable("%seconds%", 0));
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
		if (delay < 0) {
			lib.getMessagesAPI().sendMessage("error.negative-delay", p);
			return;
		}
		new DelayedPlayerAdding(lib, 0, players, playercount, p).register();
		lib.getMessagesAPI().sendMessage("doing.adding-players", p, new Replaceable("%players%", players),
				new Replaceable("%seconds%", delay));
	}

	@SubcommandInfo(subcommand = "removeplayers", permission = "mtplayers.admin.addplayers")
	public void removePlayersContainer(Player p) {
		lib.getContainerAPI().openFor(p, AddPlayersContainer.class);
	}

	@SubcommandInfo(subcommand = "removeplayers", permission = "mtplayers.admin.addplayers")
	public void removePlayers(Player p, String playerCount) {
		int players = 0;
		try {
			players = Integer.parseInt(playerCount);
		} catch (NumberFormatException e) {
			lib.getMessagesAPI().sendMessage("error.invalid-playercount", p, new Replaceable("%players%", playerCount));
			return;
		}
		new DelayedPlayerAdding(lib, 0, -players, playercount, p).register();
		lib.getMessagesAPI().sendMessage("doing.removing-players", p, new Replaceable("%players%", -players),
				new Replaceable("%seconds%", 0));
	}

	@SubcommandInfo(subcommand = "addplayers", permission = "mtplayers.admin.addplayers")
	public void removePlayersDelay(Player p, String playerCount, String delayCount) {
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
		if (delay < 0) {
			lib.getMessagesAPI().sendMessage("error.negative-delay", p);
			return;
		}
		new DelayedPlayerAdding(lib, 0, -players, playercount, p).register();
		lib.getMessagesAPI().sendMessage("doing.removing-players", p, new Replaceable("%players%", -players),
				new Replaceable("%seconds%", delay));
	}

	@Override
	public void fallbackCommand(CommandSender sender, String[] args) {
		if (!sender.hasPermission("mtplayers.admin.view")) {
			lib.getMessagesAPI().sendMessage("no-permission", sender);
			return;
		}
		sender.sendMessage(
				ChatColor.translateAlternateColorCodes('&', "&c&l&oMTPlayers &7- &ea TeamMT Creation &7(Gargant)"));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eRun &c/mtplayers help &efor help."));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
				"&eRunning version: &c&l&o" + lib.getPlugin().getDescription().getVersion()));
	}

}
