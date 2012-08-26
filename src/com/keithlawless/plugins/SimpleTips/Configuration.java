package com.keithlawless.plugins.SimpleTips;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author md5 and Acrobot
 */
public class Configuration {
    public static int firstMsgDelay = 30 * 20; //20 ticks = 1 second in perfect no-lag conditions
    public static int nextMsgDelay = 30 * 20;

    public static String msgOrder = "sequential";
    public static List<String> msgList = Lists.newArrayList("Put your messages here!");

    public static boolean groupMsgEnabled = false;
    public static Map<String, List<String>> groupMsgList = new HashMap<String, List<String>>() {{ put("sampleGroup", Lists.newArrayList("Message shown only to users with tips.show.sampleGroup permission!")); }};

    public static void load(Plugin plugin) {
        FileConfiguration config = plugin.getConfig();

        for (Field field : Configuration.class.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers())) {
                String path = field.getName().replaceAll("_", ".");

                try {
                    if (config.isSet(path)) {
                        Object object = config.get(path);

                        if (object instanceof MemorySection) {
                            Map<String, List<String>> map = new HashMap<String, List<String>>();

                            for (String message : config.getConfigurationSection(path).getKeys(false)) {
                                map.put(message, config.getConfigurationSection(path).getStringList(message));
                            }

                            field.set(null, map);
                        } else {
                            field.set(null, object);
                        }
                    } else {
                        config.set(path, field.get(null));
                    }
                } catch (IllegalAccessException ignored) {
                }
            }
        }
        plugin.saveConfig();
    }

    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('%', string);
    }
}