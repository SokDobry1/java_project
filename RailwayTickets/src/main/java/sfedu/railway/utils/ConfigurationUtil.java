package sfedu.railway.utils;

import java.io.*;
import java.util.Properties;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class ConfigurationUtil {

    Logger logger = LoggerFactory.getLogger(ConfigurationUtil.class);

    private Properties properties;
    private String filePath;

    public ConfigurationUtil(String filePath) {
        this.filePath = filePath;
        properties = new Properties();
        try(FileInputStream fis = new FileInputStream(filePath)){
            properties.load(fis);
        }catch(IOException e){
            logger.error("Cannot load properties from file: {}", e.getMessage());
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void createProperty(String key, String value) {
        properties.setProperty(key, value);
        saveProperties();
    }

    public void updateProperty(String key, String newValue) {
        if (properties.containsKey(key)) {
            properties.setProperty(key, newValue);
            saveProperties();
        } else {
            logger.error("Cannot update key {}, not found", key);
        }
    }

    public void deleteProperty(String key) {
        if (properties.remove(key) != null) {
            saveProperties();
        } else {
            logger.error("Cannot delete key {}, not found", key);
        }
    }

    private void saveProperties() {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            properties.store(fos, null);
        } catch (IOException e) {
            logger.error("Cannot save properties to file: {}", e.getMessage());
        }
    }
}
