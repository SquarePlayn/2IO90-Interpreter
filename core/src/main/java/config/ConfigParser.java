package config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import logger.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 *Utility class to parse th config json file into proper variables. Uses the Gson library to parse the json
 */
public class ConfigParser {

    private JsonObject config;

    /**
     * Constructor
     *
     * @param logger Logger
     * @param configFile config.json file
     *
     * @throws RuntimeException If the file cannot be found
     */
    public ConfigParser(Logger logger, File configFile) {

        try {
            config = (new Gson()).fromJson(new FileReader(configFile), JsonObject.class);
        } catch (FileNotFoundException exception) {
            logger.error(exception.getMessage());
            // Exit program
            throw new RuntimeException();
        }

    }

    /**
     * Returns the integer value for the specified section : property pair
     *
     * @param section Section in the config file
     * @param property Name of the property
     * @return Integer value of the property
     */
    public int getIntValue(String section, String property) {

        return getValue(section, property).getAsInt();

    }

    /**
     * Returns the float value for the specified section : property pair
     *
     * @param section Section in the config file
     * @param property Name of the property
     * @return Float value of the property
     */
    public float getFloatValue(String section, String property) {

        return getValue(section, property).getAsFloat();

    }

    public String getStringValue(String section, String property) {

        return getValue(section, property).getAsString();

    }

    /**
     * Returns the JsonElement which holds the value data for the property
     *
     * @param section Section in the config
     * @param property Property name
     * @return JsonElement of the value field
     */
    private JsonElement getValue(String section, String property) {

        return config.get(section).getAsJsonObject().get(property).getAsJsonObject().get("value");

    }

}
