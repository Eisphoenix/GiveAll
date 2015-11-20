/*
 * Copyright (c) 2015 Eisphoenix. Dieses Dokument darf nur mit der Zustimmung des Authors weitergegeben, vervielfältigt oder modifiziert werden
 */

package de.eisphoenix.giveall;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;

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
            sender.sendMessage(ChatColor.BOLD.toString() + ChatColor.DARK_RED + "Du musst ein Spieler sein");
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(ChatColor.BOLD + "Verwendung: " + ChatColor.RESET + ChatColor.DARK_RED + "/Giveall <Menge>");
            return true;
        }
        try {
            if (Integer.parseInt(args[0]) < 1) {
                sender.sendMessage(ChatColor.DARK_RED + "Menge muss über 0 sein");
                return true;
            }
        } catch (NumberFormatException ex) {
            sender.sendMessage(ChatColor.BOLD + "Verwendung: " + ChatColor.RESET + ChatColor.DARK_RED + "/Giveall <Menge>");
            return true;
        }
        final Player p = (Player) sender;
        if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR) {
            p.sendMessage(ChatColor.BOLD.toString() + ChatColor.DARK_RED + "Du musst ein Item in der Hand halten!");
            return true;
        }
        final ItemStack is = p.getItemInHand().clone();
        is.setAmount(Integer.parseInt(args[0]));

        final BaseComponent[] broadcastMessage = getBaseComponentMessage(p, is);
        if (broadcastMessage != null)
            Bukkit.spigot().broadcast(broadcastMessage);

        for (final Player player : Bukkit.getOnlinePlayers())
            player.getInventory().addItem(is);
        return true;
    }

    private BaseComponent[] getBaseComponentMessage(final Player p, final ItemStack is) {
        final Class<?> craftItemClazz = ReflectionUtil.getCBClass("inventory.CraftItemStack");
        final Class<?> nmsItemStackClazz = ReflectionUtil.getNMSClass("ItemStack");
        final Class<?> nmsNBTTagCompoundClazz = ReflectionUtil.getNMSClass("NBTTagCompound");
        if (craftItemClazz == null || nmsItemStackClazz == null || nmsNBTTagCompoundClazz == null)
            return null;
        final Method asNMSMethod;
        final Method saveNMSItemStackMethod;
        final Object nmsItemStack;
        final Object finalNMSItemStack;
        try {
            asNMSMethod = craftItemClazz.getMethod("asNMSCopy", ItemStack.class);
            saveNMSItemStackMethod = nmsItemStackClazz.getMethod("save", nmsNBTTagCompoundClazz);
            nmsItemStack = asNMSMethod.invoke(null, is);
            finalNMSItemStack = saveNMSItemStackMethod.invoke(nmsItemStack, nmsNBTTagCompoundClazz.newInstance());
        } catch (ReflectiveOperationException ex) {
            return null;
        }
        return new ComponentBuilder("[Event]").color(net.md_5.bungee.api.ChatColor.LIGHT_PURPLE)
                .append(" Der Spieler ").color(net.md_5.bungee.api.ChatColor.GREEN)
                .append(p.getName())
                .append(" verteilt ").color(net.md_5.bungee.api.ChatColor.GREEN)
                .append("dieses Item").color(net.md_5.bungee.api.ChatColor.DARK_GREEN).underlined(true)
                .event(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new BaseComponent[]{new TextComponent(finalNMSItemStack.toString())}))
                .append("!").color(net.md_5.bungee.api.ChatColor.GREEN).underlined(false).create();
    }
}
