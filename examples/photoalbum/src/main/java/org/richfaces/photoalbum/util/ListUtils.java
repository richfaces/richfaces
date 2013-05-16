package org.richfaces.photoalbum.util;

import java.util.Arrays;
import java.util.List;

public class ListUtils {
    public static String sListToString(List<String> sList) {
        if (sList == null) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();

        for(String s : sList) {
           sb.append(s).append(',');
        }

        sb.deleteCharAt(sb.length()-1);

        return sb.toString();
    }
    
    public static List<String> StringToSList(String s) {
        if (s == null) {
            return null;
        }
        return Arrays.asList(s.split(","));
    }
}
