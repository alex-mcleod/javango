package co.gitm.javango.config;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

public final class Config {
	
	public static Configuration getConfig(String file){
		Configuration config = new XMLConfiguration();
		try {
			config = new XMLConfiguration(file);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		return config;
	}
	
}
