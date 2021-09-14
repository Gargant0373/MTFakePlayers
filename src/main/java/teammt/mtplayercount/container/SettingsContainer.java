package teammt.mtplayercount.container;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;

import masecla.mlib.containers.generic.ImmutableContainer;
import masecla.mlib.main.MLib;
import net.md_5.bungee.api.ChatColor;
import net.wesjd.anvilgui.AnvilGUI;
import revive.retab.classes.skin.Skin;
import teammt.mtplayercount.classes.PlayercountManagement;

public class SettingsContainer extends ImmutableContainer {

	private PlayercountManagement playercount;

	public SettingsContainer(MLib lib, PlayercountManagement playercount) {
		super(lib);
		this.playercount = playercount;
	}

	@Override
	public void onTopClick(InventoryClickEvent event) {
		event.setCancelled(true);

		Player p = (Player) event.getWhoClicked();
		switch (event.getSlot()) {
		case 49:
			lib.getContainerAPI().closeFor(p);
			break;
		case 20:
			new AnvilGUI.Builder().text("Enter new value.").title("New Value").itemLeft(getInstructions())
					.onComplete((c, r) -> {
						lib.getConfigurationAPI().getConfig().set("online-players", r);
						return AnvilGUI.Response.close();
					}).onClose(c -> lib.getContainerAPI().openFor(p, SettingsContainer.class)).plugin(lib.getPlugin())
					.open(p);
			break;
		case 21:
			new AnvilGUI.Builder().text("Enter new value.").title("New Value").itemLeft(getInstructions())
					.onComplete((c, r) -> {
						lib.getConfigurationAPI().getConfig().set("online-players", r);
						return AnvilGUI.Response.close();
					}).onClose(c -> lib.getContainerAPI().openFor(p, SettingsContainer.class)).plugin(lib.getPlugin())
					.open(p);
			break;
		case 24:
			lib.getConfigurationAPI().getConfig().set("enabled",
					!lib.getConfigurationAPI().getConfig().getBoolean("enabled", true));
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0.8f);
			break;
		}
	}

	@Override
	public int getSize() {
		return 54;
	}

	@Override
	public int getUpdatingInterval() {
		return 5;
	}

	@Override
	public boolean requiresUpdating() {
		return true;
	}

	@Override
	public Inventory getInventory(Player p) {
		Inventory inv = Bukkit.createInventory(p, getSize(), ChatColor.YELLOW + "Settings");

		inv.setItem(29, this.getInstructions());
		inv.setItem(20, this.getOnlinePlayers());
		inv.setItem(21, this.getMaximumPlayers());

		inv.setItem(23, this.getActualPlayers());
		inv.setItem(24, this.getTogglePlugin());

		inv = this.applyMarginalBars(inv);
		inv.setItem(49, this.getInventoryClose());

		return inv;
	}

	private ItemStack getInventoryClose() {
		ItemStack result = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
		SkullMeta theMeta = (SkullMeta) result.getItemMeta();
		GameProfile profile = new Skin(
				"ewogICJ0aW1lc3RhbXAiIDogMTYxNjI1ODY1NTExMiwKICAicHJvZmlsZUlkIiA6ICJiMGQ3MzJmZTAwZjc0MDdlOWU3Zjc0NjMwMWNkOThjYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJPUHBscyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9lNDRlYWUyNTAxMzk4M2E5MWQ2YTY5YjFiZjExMjdjMjY0NTk5MWExZDIwNWY2ZTUzODY1OTQ3ZjE0ZDVmNDVlIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=",
				"XH3TZYlsF7xmneUF7Z5sKBFBSrMTI+tY64PbAeFJcH4MrHGz38Z5Lc9kub3qG20LJFa861mHFeIAq+Ca6aLFdE3+5hHrxGJalj+JoF8gFP2Fx0u9myy633IAU+Uj/MKCq1BXpEVeAcDBUMTn6S0/j5YUVi3Q9PWsBoBH5rQk4HYetW0LN0qmd9BJwxfB16xHJQxMrfrIGlpLfts8Md58p+Q1cvHU5ZiJjnmA5lP66YP15eduX6APwhW5somS8A7Cl/P4k+P08SjAudkvQzvM+TGj+Mz2jNFG3qSp2z2p/X/jooRByKWfPzwvQ/O1KuJrWKUC3kzXGPrMBHLXGv/Gti3EvMoKNM1+KKcq5s7T5ZYTlMhSnKBFKgB6W9fQtSyZtlmVN1MPI1tnUxOgU4lEYuVRAaSgM/VcYSSivHW+0+moRESIG0CpFluJsHtMQ/B4aMqEJXDVXxu+jC/SDkQ+MNJb62gleTGbEn8TUyHVLLLngVXMasGmh4HjZuazsLB1nCLjxXzXvMe72b9ovpwF0o3xRzDbcc6JvGk0ajshzneDbkl0ucStCv7xCfXP8zKXupGVfyo/0kuYhdMv5SpskVrfxSsFGoK/xjgY95hADQbCTuRoOyK+1PbQyYrHr9vfb/LmlonF0c/AbwCP7UrYfHVPuK+VM8HLT7Rk70oC5dA=")
						.createProfile();
		Field profileField = null;
		try {
			profileField = theMeta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(theMeta, profile);
		} catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException
				| SecurityException exception) {
			// empty catch block
		}
		theMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cClose!"));
		ArrayList<String> thelore = new ArrayList<String>();
		thelore.add(" ");
		thelore.add(ChatColor.translateAlternateColorCodes('&', "&7Close this menu!"));
		theMeta.setLore(thelore);
		result.setItemMeta(theMeta);
		return result;
	}

	private Inventory applyMarginalBars(Inventory inv) {
		ItemStack marginalBar = this.getMarginalBar();
		for (int i = 0; i < 5; i++) {
			// Top bar
			inv.setItem(i, marginalBar);
			inv.setItem(9 - i, marginalBar);

			// Bottom bar
			inv.setItem(i + this.getSize() - 9, marginalBar);
			inv.setItem(this.getSize() - i - 1, marginalBar);

			// Side bars
			inv.setItem(i * 9, marginalBar);
			inv.setItem(i * 9 + 8, marginalBar);
		}

		return inv;
	}

	private ItemStack getMarginalBar() {
		ItemStack s = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
		ItemMeta meta = s.getItemMeta();
		meta.setDisplayName(ChatColor.BLACK + "+");
		s.setItemMeta(meta);
		return s;
	}

	private ItemStack getInstructions() {
		ItemStack s = new ItemStack(Material.NAME_TAG);
		ItemMeta m = s.getItemMeta();
		m.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&ePlayer Value Instructions"));
		List<String> lore = new ArrayList<>();
		lore.add("");
		lore.add(ChatColor.WHITE + "Integer Values");
		lore.add(ChatColor.GRAY + "You can set constant values such as 1 or 20.");
		lore.add(ChatColor.GRAY + "You can do that from the items on your right or");
		lore.add(ChatColor.GRAY + "from the config file.");
		lore.add("");
		lore.add(ChatColor.WHITE + "Complex Values");
		lore.add(ChatColor.GRAY + "You can set complex values such as \"2*3\".");
		lore.add(ChatColor.GRAY + "This setting supports the \"%players%\"");
		lore.add(ChatColor.GRAY + "placeholder that represents the amount of online");
		lore.add(ChatColor.GRAY + "players there are.");
		m.setLore(lore);
		s.setItemMeta(m);
		return s;
	}

	private ItemStack getTogglePlugin() {
		boolean enabled = lib.getConfigurationAPI().getConfig().getBoolean("enabled", true);
		ItemStack s = new ItemStack(Material.WOOL, 1, (byte) (enabled ? 13 : 14));
		ItemMeta m = s.getItemMeta();
		m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
				"&fPlugin is " + (enabled ? "&a&lENABLED" : "&c&lDISABLED")));
		m.setLore(Arrays.asList("", ChatColor.GRAY + "Click to toggle!"));
		s.setItemMeta(m);
		return s;
	}

	private ItemStack getOnlinePlayers() {
		ItemStack s = new ItemStack(Material.SIGN);
		ItemMeta m = s.getItemMeta();
		m.setDisplayName(ChatColor.YELLOW + "Online Players");
		m.setLore(Arrays.asList("", ChatColor.YELLOW + "Value: " + ChatColor.WHITE + this.getValueFor("online-players"),
				ChatColor.YELLOW + "Actual Value: " + ChatColor.WHITE + playercount.getOnlinePlayers(), "",
				ChatColor.GRAY + "Click to change!"));
		s.setItemMeta(m);
		return s;
	}

	private ItemStack getMaximumPlayers() {
		ItemStack s = new ItemStack(Material.SIGN);
		ItemMeta m = s.getItemMeta();
		m.setDisplayName(ChatColor.YELLOW + "Maximum Players");
		m.setLore(
				Arrays.asList("", ChatColor.YELLOW + "Value: " + ChatColor.WHITE + this.getValueFor("maximum-players"),
						ChatColor.YELLOW + "Actual Value: " + ChatColor.WHITE + playercount.getMaximumPlayers(), "",
						ChatColor.GRAY + "Click to change!"));
		s.setItemMeta(m);
		return s;
	}

	private ItemStack getActualPlayers() {
		ItemStack s = new ItemStack(Material.BOOK);
		ItemMeta m = s.getItemMeta();
		m.setDisplayName(ChatColor.YELLOW + "Online Players: " + ChatColor.WHITE + Bukkit.getOnlinePlayers().size());
		s.setItemMeta(m);
		return s;
	}

	public String getValueFor(String path) {
		if (lib.getConfigurationAPI().getConfig().isString(path))
			return lib.getConfigurationAPI().getConfig().getString(path, "N/A");
		if (lib.getConfigurationAPI().getConfig().isDouble(path))
			return lib.getConfigurationAPI().getConfig().getDouble(path, 0.00) + "";
		return lib.getConfigurationAPI().getConfig().getInt(path, 0) + "";
	}

}
