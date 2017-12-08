package config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import logger.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 *
 */
public class ConfigParser {

    private JsonObject config;

    public ConfigParser(Logger logger, File configFile) {

        try {
            config = (new Gson()).fromJson(new FileReader(configFile), JsonObject.class);
        } catch (FileNotFoundException exception) {
            logger.error(exception.getMessage());
            // Exit program
            throw new RuntimeException();
        }

    }

    public int getIntValue(String section, String property) {

        return getValue(section, property).getAsInt();

    }

    public float getFloatValue(String section, String property) {

        return getValue(section, property).getAsFloat();

    }

    private JsonElement getValue(String section, String property) {

        return config.get(section).getAsJsonObject().get(property).getAsJsonObject().get("value");

    }

}
