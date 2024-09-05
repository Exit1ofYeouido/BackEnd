package com.example.Search.Common.Util;

public class KafkaUtil {
    public static String getJsonTypeMapping(Class<?> ...classes){
        StringBuilder sb=new StringBuilder();
        for (Class<?> c:classes){
            sb.append(c.getSimpleName());
            sb.append(":");
            sb.append(c.getName());
            sb.append(",");
        }

        sb.setLength(sb.length()-1);

        return sb.toString();
    }
}
