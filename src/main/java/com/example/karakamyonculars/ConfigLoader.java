package com.example.karakamyonculars;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static Properties properties = new Properties();

    static {
        try(InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties")){
            if(input != null){
                properties.load(input);
            }
            else
                throw new RuntimeException("error");
        }
        catch (IOException e){
            throw new RuntimeException("error", e);
        }
    }
    public static String get(String key){
        return properties.getProperty(key);
    }
}
