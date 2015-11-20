/*
 * Copyright (c) 2015 Eisphoenix. Dieses Dokument darf nur mit der Zustimmung des Authors weitergegeben, vervielfältigt oder modifiziert werden
 */

package de.eisphoenix.giveall;

import org.bukkit.Bukkit;

/**
 * Created by Eisphoenix in 2015.
 */
public final class ReflectionUtil {

    static String getVersion() {
        final String name = Bukkit.getServer().getClass().getPackage().getName();
        return name.substring(name.lastIndexOf('.') + 1);
    }

    static Class<?> getNMSClass(final String className) {
        final String fullName = "net.minecraft.server." + getVersion() + "." + className;
        try {
            return Class.forName(fullName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    static Class<?> getCBClass(final String className) {
        final String fullName = "org.bukkit.craftbukkit." + getVersion() + "." + className;
        try {
            return Class.forName(fullName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
