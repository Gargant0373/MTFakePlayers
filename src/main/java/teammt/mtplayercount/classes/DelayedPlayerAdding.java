package teammt.mtplayercount.classes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import masecla.mlib.main.MLib;

public class DelayedPlayerAdding {

	private int secondsDelay;
	private int playerCount;

	private boolean add;

	private MLib lib;
	private PlayercountManagement playercount;
	private Player player;

	public DelayedPlayerAdding(MLib lib, int secondsDelay, int playerCount, PlayercountManagement playercount,
			Player player) {
		this.lib = lib;
		this.secondsDelay = secondsDelay;
		this.playerCount = playerCount;
		this.playercount = playercount;
		this.player = player;
		this.add = this.playerCount > 0;
	}

	public void register() {
		this.addPlayer();
	}

	private void addPlayer() {
		if (playerCount == 0)
			return;
		if (playerCount > 0) {
			playercount.decrementOnlinePlayers();
			this.playerCount--;
		} else {
			this.playerCount++;
			playercount.incrementOnlinePlayers();
		}
		if (playerCount != 0) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(lib.getPlugin(), () -> {
				this.addPlayer();
			}, 20 * secondsDelay);
		} else {
			if (add)
				lib.getMessagesAPI().sendMessage("success.added-players", player);
			else
				lib.getMessagesAPI().sendMessage("success.removed-players", player);
		}
	}
}
