package org.springframework.cloud.heroku;

public class HerokuUtil {
    public static String computeServiceName(String envVar) {
        String stripSuffix = "_URL";
        if (envVar.endsWith(stripSuffix)) {
            return envVar.substring(0, envVar.length() - stripSuffix.length());
        } else {
            return envVar;
        }
    }
}
