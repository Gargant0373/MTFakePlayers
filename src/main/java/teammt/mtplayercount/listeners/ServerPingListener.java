package teammt.mtplayercount.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedServerPing;

import masecla.mlib.classes.Registerable;
import masecla.mlib.main.MLib;
import teammt.mtplayercount.classes.PlayercountManagement;

public class ServerPingListener extends Registerable {

	private PlayercountManagement playercount;

	public ServerPingListener(MLib lib, PlayercountManagement playercount) {
		super(lib);
		this.playercount = playercount;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void register() {
		super.register();
		ProtocolLibrary.getProtocolManager().addPacketListener(
				new PacketAdapter(lib.getPlugin(), ListenerPriority.HIGHEST, PacketType.Status.Server.OUT_SERVER_INFO) {
					@Override
					public void onPacketSending(PacketEvent event) {
						handlePing(event.getPacket().getServerPings().read(0));
					}
				});
	}

	private void handlePing(WrappedServerPing ping) {
		if (!isEnabled())
			return;
		ping.setPlayersMaximum(playercount.getMaximumPlayers());
		ping.setPlayersOnline(playercount.getOnlinePlayers());
	}

	private boolean isEnabled() {
		return lib.getConfigurationAPI().getConfig().getBoolean("enabled");
	}

}
