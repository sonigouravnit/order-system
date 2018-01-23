package com.gourav.utils;

import java.util.ArrayList;

/**
 * Created by gouravsoni on 23/01/18.
 */
public class CommonUtils {

    public static String StringListToCommaSeperated(ArrayList<String> list) {
        return "" + list.toString().replace("[", "").replace("]", "").replace(", ", ",");
    }

    public static boolean validateUrl(String url) {
        return url.matches("^([a-zA-Z0-9.])*[a-zA-Z0-9]+(:[0-9]{1,4}){0,1}$");
    }

}
