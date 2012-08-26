/*
 * SimpleTips (http://github.com/keithlawless/Tips--Bukkit-Plugin-)
 * Copyright (C) 2011 Keith Lawless
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.keithlawless.plugins.SimpleTips;

import com.keithlawless.plugins.SimpleTips.MessageRunners.GroupMessageRunner;
import com.keithlawless.plugins.SimpleTips.MessageRunners.MessageRunner;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;
import java.util.logging.Logger;

public class SimpleTips extends JavaPlugin {
    public static Logger log;
    public static Random random = new Random();

    int scheduledTaskID;

    public void onEnable() {
        log = getLogger();

        Configuration.load(this);

        Runnable runnable = (Configuration.groupMsgEnabled ? new GroupMessageRunner() : new MessageRunner());
        scheduledTaskID = Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, runnable, Configuration.firstMsgDelay, Configuration.nextMsgDelay);

        if (scheduledTaskID < 0) {
            log.severe("Error! Failed to schedule tip display.");
        } else {
            log.info("Success! SimpleTips will be displayed on your schedule.");
        }
    }

    public void onDisable() {
        Bukkit.getScheduler().cancelTask(scheduledTaskID);
    }

    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!command.getName().equalsIgnoreCase("tip")) {
            return false;
        }

        if (args.length < 1) {
            if (Configuration.msgList.size() < 1) {
                sender.sendMessage("No tips have been defined.");
                return true;
            }

            int randomInt = random.nextInt(Configuration.msgList.size());
            String message = Configuration.msgList.get(randomInt);

            sender.sendMessage(Configuration.colorize(message));
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            if (!sender.hasPermission("tip.list")) {
                sender.sendMessage("(SimpleTips) You don't have permission to run that command.");
                return true;
            }

            for (int i = 0; i < Configuration.msgList.size(); ++i) {
                sender.sendMessage("(" + i + ") " + Configuration.colorize(Configuration.msgList.get(i)));
            }

            return true;
        }

        if (args.length >= 2) {
            if (args[0].equalsIgnoreCase("add")) {
                if (!sender.hasPermission("tip.add")) {
                    sender.sendMessage("(SimpleTips) You don't have permission to run that command.");
                    return true;
                }

                String message = joinArray(args, 1);

                Configuration.msgList.add(message);

                getConfig().set("msgList", Configuration.msgList);
                saveConfig();

                sender.sendMessage("(SimpleTips) Tip has been added.");

                return true;
            } else if (args[0].equalsIgnoreCase("del")) {
                if (!sender.hasPermission("tip.del")) {
                    sender.sendMessage("(SimpleTips) You don't have permission to run that command.");
                    return true;
                }

                try {
                    int messageNo = Integer.parseInt(args[1]);
                    Configuration.msgList.remove(messageNo);

                    getConfig().set("msgList", Configuration.msgList);
                    saveConfig();

                    sender.sendMessage("(SimpleTips) Tip has been deleted.");

                    return true;
                } catch (NumberFormatException ex) {
                    return false;
                } catch (IndexOutOfBoundsException ex) {
                    sender.sendMessage("(SimpleTips) Tip was not found.");
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("replace")) {
                if (!sender.hasPermission("tip.replace")) {
                    sender.sendMessage("(SimpleTips) You don't have permission to run that command.");
                    return true;
                }

                if (args.length < 3) {
                    return false;
                }

                try {
                    int messageNo = Integer.parseInt(args[1]);
                    String message = joinArray(args, 2);

                    Configuration.msgList.set(messageNo, message);

                    getConfig().set("msgList", Configuration.msgList);
                    saveConfig();

                    sender.sendMessage("(SimpleTips) Tip has been replaced.");
                    return true;
                } catch (NumberFormatException nfe) {
                    sender.sendMessage("(SimpleTips) This is not a valid tip number.");
                    return true;
                } catch (IndexOutOfBoundsException e) {
                    sender.sendMessage("(SimpleTips) Tip was not found.");
                    return true;
                }
            }

        }

        return false;
    }

    private static String joinArray(String[] array, int start) {
        StringBuilder message = new StringBuilder(array.length * 10);

        for (int i = start; i < array.length; ++i) {
            if (i != start) {
                message.append(' ');
            }

            message.append(array[i]);
        }

        return message.toString();
    }
}
