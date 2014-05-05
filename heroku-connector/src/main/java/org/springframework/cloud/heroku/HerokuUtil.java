package org.springframework.cloud.heroku;

public class HerokuUtil {
    public static String computeServiceName(String envVar) {
        String[] stripSuffices = new String[]{"_URL", "_URI"};
        
        for (String suffix: stripSuffices) {
            if (envVar.endsWith(suffix)) {
                return envVar.substring(0, envVar.length() - suffix.length());
            }
        }
        
        return envVar;
    }
}
