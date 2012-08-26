package com.keithlawless.plugins.SimpleTips;

/**
 * @author Acrobot
 */
public enum MessageOrder {
    SEQUENTIAL,
    RANDOM;

    public static MessageOrder getMessageOrder(String string) {
        try {
            return valueOf(string.toUpperCase());
        } catch (IllegalArgumentException exception) {
            SimpleTips.log.severe("There is no \"" + string + "\" order. Please choose sequential or random.");
            return SEQUENTIAL; //By default, we return sequential
        }
    }
}
