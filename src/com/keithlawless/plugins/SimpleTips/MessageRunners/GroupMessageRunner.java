package com.keithlawless.plugins.SimpleTips.MessageRunners;

import com.keithlawless.plugins.SimpleTips.Configuration;
import com.keithlawless.plugins.SimpleTips.SimpleTips;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * @author Acrobot
 */
public class GroupMessageRunner implements Runnable {
    public void run() {
        Player[] players = Bukkit.getOnlinePlayers();

        for (Player player : players) {
            Set<String> groups = Configuration.groupMsgList.keySet();

            for (String group : groups) {
                String permissionNode = "tip.show." + group;

                if (!player.hasPermission(permissionNode)) {
                    continue;
                }

                int messageCount = Configuration.groupMsgList.get(group).size();

                if (messageCount < 1) {
                    continue;
                }

                String message = Configuration.groupMsgList.get(group).get(SimpleTips.random.nextInt(messageCount));
                message = Configuration.colorize(message);

                player.sendMessage(message);
            }
        }
    }
}
