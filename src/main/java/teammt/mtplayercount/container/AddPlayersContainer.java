package teammt.mtplayercount.container;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

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

import masecla.mlib.classes.Replaceable;
import masecla.mlib.containers.generic.ImmutableContainer;
import masecla.mlib.main.MLib;
import net.md_5.bungee.api.ChatColor;
import revive.retab.classes.skin.Skin;
import teammt.mtplayercount.classes.DelayedPlayerAdding;
import teammt.mtplayercount.classes.PlayercountManagement;

public class AddPlayersContainer extends ImmutableContainer {

	private PlayercountManagement playercount;

	private int secondsDelay = 1;
	private int playerCount = 0;

	public AddPlayersContainer(MLib lib, PlayercountManagement playercount) {
		super(lib);
		this.playercount = playercount;
	}

	@Override
	public void onTopClick(InventoryClickEvent event) {
		event.setCancelled(true);
		Player p = (Player) event.getWhoClicked();
		if (event.getSlot() == 49) {
			p.closeInventory();
			return;
		}
		if (event.getSlot() == 31) {
			new DelayedPlayerAdding(lib, secondsDelay, playerCount, playercount, p).register();
			if (playerCount > 0)
				lib.getMessagesAPI().sendMessage("doing.adding-players", p, new Replaceable("%players%", playerCount),
						new Replaceable("%seconds%", secondsDelay));
			if (playerCount < 0)
				lib.getMessagesAPI().sendMessage("doing.removing-players", p, new Replaceable("%players%", playerCount),
						new Replaceable("%seconds%", secondsDelay));
			if (playerCount == 0) {
				lib.getMessagesAPI().sendMessage("error.invalid-playercount", p,
						new Replaceable("%players%", playerCount));
				return;
			}
			p.closeInventory();
			return;
		}

		if (event.getSlot() == 43) {
			this.playercount.setAddedPlayers(0);
			p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.8f, 1);
			return;
		}

		if (!event.getCurrentItem().getType().equals(Material.WOOL))
			return;

		@SuppressWarnings("deprecation")
		String tag = lib.getNmsAPI().getNBTTagValueString(event.getCurrentItem(), "INCREMENT");
		if (tag == null)
			return;
		String type = tag.split("_")[0];
		int amount = Integer.parseInt(tag.split("_")[1]);

		switch (type) {
		case "players":
			this.playerCount += amount;
			p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.8f, 1);
			break;
		case "delay":
			if (this.secondsDelay + amount <= 0) {
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0.8f);
				return;
			}
			this.secondsDelay += amount;
			p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.8f, 1);
			break;
		}

	}

	@Override
	public int getSize() {
		return 54;
	}

	@Override
	public int getUpdatingInterval() {
		return 10;
	}

	@Override
	public boolean requiresUpdating() {
		return true;
	}

	@Override
	public Inventory getInventory(Player p) {
		Inventory inv = Bukkit.createInventory(p, getSize(), ChatColor.YELLOW + "Add Players");

		inv.setItem(10, this.getIncrement("Players", -10));
		inv.setItem(11, this.getIncrement("Players", -5));
		inv.setItem(12, this.getIncrement("Players", -1));
		inv.setItem(13, this.getPlayers());
		inv.setItem(14, this.getIncrement("Players", 1));
		inv.setItem(15, this.getIncrement("Players", 5));
		inv.setItem(16, this.getIncrement("Players", 10));

		inv.setItem(19, this.getIncrement("Delay", -10));
		inv.setItem(20, this.getIncrement("Delay", -5));
		inv.setItem(21, this.getIncrement("Delay", -1));
		inv.setItem(22, this.getDelay());
		inv.setItem(23, this.getIncrement("Delay", 1));
		inv.setItem(24, this.getIncrement("Delay", 5));
		inv.setItem(25, this.getIncrement("Delay", 10));

		inv.setItem(31, this.getGenerate());

		inv.setItem(43, this.getAddedPlayers());

		inv = this.applyMarginalBars(inv);
		inv.setItem(49, this.getInventoryClose());

		return inv;
	}

	private ItemStack getAddedPlayers() {
		ItemStack s = new ItemStack(Material.REDSTONE);
		ItemMeta m = s.getItemMeta();
		m.setDisplayName(ChatColor.YELLOW + "Added Players");
		m.setLore(
				Arrays.asList("", ChatColor.YELLOW + "Amount: " + ChatColor.WHITE + this.playercount.getAddedPlayers(),
						"", ChatColor.GRAY + "Click to set to 0!"));
		s.setItemMeta(m);
		return s;
	}

	private ItemStack getDelay() {
		ItemStack s = new ItemStack(Material.FEATHER);
		ItemMeta m = s.getItemMeta();
		m.setDisplayName(ChatColor.YELLOW + "Delay");
		m.setLore(Arrays.asList("",
				ChatColor.translateAlternateColorCodes('&',
						"&6" + this.secondsDelay + " &eSecond" + (this.secondsDelay == 1 ? "" : "s") + " Delay"),
				"", ChatColor.GRAY + "!Between players getting added!"));
		s.setItemMeta(m);
		return s;

	}

	private ItemStack getPlayers() {
		ItemStack s = new ItemStack(Material.SKULL_ITEM);
		ItemMeta m = s.getItemMeta();
		m.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&ePlayers"));
		m.setLore(Arrays.asList("",
				ChatColor.translateAlternateColorCodes('&',
						"&6" + Math.abs(playerCount) + " &ePlayer" + (Math.abs(playerCount) == 1 ? "" : "s") + " to be "
								+ (playerCount >= 0 ? "Added" : "Removed"))));
		s.setItemMeta(m);
		return s;
	}

	private ItemStack getIncrement(String type, int amount) {
		ItemStack s = new ItemStack(Material.WOOL, 1, (byte) this.getColorFor(amount));
		ItemMeta meta = s.getItemMeta();
		meta.setDisplayName(
				ChatColor.translateAlternateColorCodes('&', "&6" + (amount < 0 ? "" : "+") + amount + " &e" + type));
		meta.setLore(Arrays.asList("", ChatColor.GRAY + "Click to change!"));
		s.setItemMeta(meta);
		return lib.getNmsAPI().write().tagString("INCREMENT", type.toLowerCase() + "_" + amount).applyOn(s);
	}

	private int getColorFor(int count) {
		count = Math.abs(count);
		if (count == 1)
			return 13;
		if (count == 5)
			return 1;
		return 14;
	}

	private ItemStack getGenerate() {
		ItemStack result = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
		SkullMeta theMeta = (SkullMeta) result.getItemMeta();
		GameProfile profile = new Skin(
				"ewogICJ0aW1lc3RhbXAiIDogMTYxMjAwNzgzNjI0MSwKICAicHJvZmlsZUlkIiA6ICIxYWZhZjc2NWI1ZGY0NjA3YmY3ZjY1ZGYzYWIwODhhOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJMb3lfQmxvb2RBbmdlbCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS80YTFlYjQzYmU5OTc2ZDRlZjBiZDI4NzFiNzAxNGZlNzllY2RhZWMzZDJkMzMzNjE5ZGE4ZDBhNmM5MGE2ODJjIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=",
				"e0xfq5M96ce61hrcCjKXebaPBEX6OUWC6EdMd/hwuFUFTz070LG2RMAH+eiP0R+TqiHyAPwLVlYky531J8BbSMoV9lhhFFUvsQ6tab9cPxhhVOP5jxSkM52OGch6X2jxBXyZxkBZR4LQXHaih5HqcdvgWPKlqI/d4wmOX9VaGUP3lnbOOiViQ1k4DZNDeVcgXmlaUqh0oVM+Sue6TmtY0lvjp0zrslle13B41DPnDXuW1hDTtfACD9sluwADZYi/youWBQSzD255e5CXaQFLC4sJTYO+KCj/Cufg0lQsh0Eudt8PiD7zNjv0qSafUoyobwrP2Koi6sammDKfP5Nf7k9a6ZDycrruXOSqGKUqdzmlU+iQvhZTbLEPfybWs9Vt3S1rMkKZvn3KaaoDoULPFrYfxMLtSS+R2IxkdG9gaQ7yAbrp2l8wBkKMa5AtkYUaKFzn54XZCAjU1OSIV5GZYkQjEKorOPrd9H3fKyY+ew/+Kvn2bELfcFIKhHK2RpNG5rZytf9mTULyy+J8GQuA4cHu7dwI9h6S3fUcExF4pVfgJkf2sb95a9H1oP8T9EMiJbIv6QOaUBibtsVfsOwGvpXO51bBT+KlkpMfksYKB/894iuWlrO/em1+05lwZr19+Ss/TXjM+XR5jmMWfawrnjGeQWlii4a3W2wlOwv2m7c=")
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
		theMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aAdd Players!"));
		result.setItemMeta(theMeta);
		return result;
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
		thelore.add(" ");
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
}
