package com.example.Mypage.Mypage.Kafka.Util;

public class KafkaUtil {
    public static String getJsonTypeMapping(Class<?>... classes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < classes.length; i++) {
            Class<?> c = classes[i];
            sb.append(c.getSimpleName());
            sb.append(":");
            sb.append(c.getName());

            if (i < classes.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

}
