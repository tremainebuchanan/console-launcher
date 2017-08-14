package com.tremainebuchanan.consolelauncher.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by captain_kirk on 8/13/17.
 */

public class StringFilter {

    public static boolean contains(String source, String query){
        String q = "\\b"+ query + "\\b";
        Pattern pattern = Pattern.compile(q);
        Matcher matcher = pattern.matcher(source);
        return matcher.find();
    }
}
