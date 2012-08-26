package com.keithlawless.plugins.SimpleTips.MessageRunners;

import com.keithlawless.plugins.SimpleTips.Configuration;
import com.keithlawless.plugins.SimpleTips.MessageOrder;
import com.keithlawless.plugins.SimpleTips.SimpleTips;
import org.bukkit.Bukkit;

/**
 * @author Acrobot
 */
public class MessageRunner implements Runnable {
    private static MessageOrder messageOrder;

    private int currentMessage = 0;
    private int messageCount;

    public MessageRunner() {
        messageOrder = MessageOrder.getMessageOrder(Configuration.msgOrder);
        messageCount = Configuration.msgList.size();
    }

    public void run() {
        messageCount = Configuration.msgList.size();

        if (messageCount < 1) {
            return;
        }

        String message;

        if (messageOrder == MessageOrder.RANDOM) {
            int randomInt = SimpleTips.random.nextInt(Configuration.msgList.size());
            message = Configuration.msgList.get(randomInt);
        } else {
            message = Configuration.msgList.get(currentMessage);
        }

        message = Configuration.colorize(message);
        Bukkit.broadcastMessage(message);

        currentMessage = (currentMessage + 1) % messageCount;
    }
}
