/*
 * Copyright (c) 2015 Eisphoenix. Dieses Dokument darf nur mit der Zustimmung des Authors weitergegeben, vervielfältigt oder modifiziert werden
 */

package de.eisphoenix.giveall;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Eisphoenix in 2015.
 */
public final class GiveAll extends JavaPlugin {

    @Override
    public final void onEnable() {
        Bukkit.getPluginCommand("giveall").setExecutor(this);
        getLogger().info("Wurde hochgefahren!");
    }

    @Override
    public final void onDisable() {
        getLogger().info("Wird heruntergefahren!");
    }

    @Override
    public final boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!cmd.getName().equalsIgnoreCase("giveall"))
            return false;
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.BOLD.toString() + ChatColor.DARK_RED + "You must be a Player");
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(ChatColor.BOLD + "USAGE: " + ChatColor.RESET + ChatColor.DARK_RED + "/Giveall <Amount>");
            return true;
        }
        try {
            if (Integer.parseInt(args[0]) < 1) {
                sender.sendMessage(ChatColor.DARK_RED + "Amount must be about 0");
                return true;
            }
        } catch (NumberFormatException ex) {
            sender.sendMessage(ChatColor.BOLD + "USAGE: " + ChatColor.RESET + ChatColor.DARK_RED + "/Giveall <Amount>");
            return true;
        }
        final Player p = (Player) sender;
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) {
            p.sendMessage(ChatColor.BOLD.toString() + ChatColor.DARK_RED + "You need to have an item in your hand");
            return true;
        }
        final ItemStack is = p.getItemInHand().clone();
        is.setAmount(Integer.parseInt(args[0]));
        for (final Player player : Bukkit.getOnlinePlayers())
            player.getInventory().addItem(is);
        return true;
    }

    private String firstCharUpperCase(final String s) {
        if (s.length() == 0)
            return "";
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
