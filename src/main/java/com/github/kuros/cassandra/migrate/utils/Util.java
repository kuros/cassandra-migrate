package com.github.kuros.cassandra.migrate.utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Util {

    public static boolean isOption(String arg) {
        return arg.startsWith("--") && !arg.trim().endsWith("=");
    }

    public static File file(File path, String fileName) {
        return new File(path.getAbsolutePath() + File.separator + fileName);
    }

    public static String horizontalLine(String caption, int length) {
        final StringBuilder builder = new StringBuilder();
        builder.append("==========");
        if (caption.length() > 0) {
            caption = " " + caption + " ";
            builder.append(caption);
        }
        for (int i = 0; i < length - caption.length() - 10; i++) {
            builder.append("=");
        }
        return builder.toString();
    }

    public static boolean isEmpty(String... params) {
        return params == null || params.length < 1 || params[0] == null || params[0].length() < 1;
    }
}
