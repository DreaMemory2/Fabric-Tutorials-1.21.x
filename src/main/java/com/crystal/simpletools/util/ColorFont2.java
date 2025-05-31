package com.crystal.simpletools.util;

import net.minecraft.util.Formatting;

/**
* ClassName: ColorFont2<br>
* Description: <br>
* Datetime: 2025/5/31 18:16<br>
* @author Crystal
* @version 1.0
* @since 1.0
*/
public class ColorFont2 {
    private static final Formatting[] colour = new Formatting[] {
            Formatting.BLUE, Formatting.BLUE, Formatting.BLUE, Formatting.BLUE, Formatting.WHITE, Formatting.BLUE, Formatting.WHITE, Formatting.WHITE, Formatting.BLUE, Formatting.WHITE,
            Formatting.WHITE, Formatting.BLUE, Formatting.RED, Formatting.WHITE, Formatting.GRAY, Formatting.GRAY, Formatting.GRAY, Formatting.GRAY, Formatting.GRAY, Formatting.GRAY,
            Formatting.GRAY, Formatting.GRAY, Formatting.GRAY, Formatting.GRAY, Formatting.GRAY, Formatting.GRAY, Formatting.GRAY, Formatting.GRAY, Formatting.GRAY };

    public static String formatting(String input, Formatting[] colours, double delay) {
        StringBuilder sb = new StringBuilder(input.length() * 3);
        if (delay <= 0.0D)
            delay = 0.001D;
        int offset = (int)Math.floor((System.currentTimeMillis() & 0x3FFFL) / delay) % colours.length;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            sb.append(colours[(colours.length + i - offset) % colours.length].toString());
            sb.append(c);
        }
        return sb.toString();
    }

    public static String makeColour(String input) {
        return formatting(input, colour, 80.0D);
    }

    public static String makeColour2(String input) {
        return formatting(input, colour, 59.5D);
    }
}
