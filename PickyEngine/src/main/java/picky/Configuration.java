package picky;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import picky.node.Role;

/**
 * 
 * @author FrankNPC
 *
 */
public final class Configuration {

	private static volatile Configuration configuration;
	public static Configuration getConfiguration() {
		if (configuration==null) {
			synchronized(Configuration.class) {
				if (configuration==null) {
					try {
						configuration = loadConfiguration(
											Configuration.class.getClassLoader()
												.getResourceAsStream("picky.properties"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return configuration;
	}
	public static Configuration loadConfiguration(String fileName) throws IOException {
		if (configuration==null) {
			synchronized(Configuration.class) {
				if (configuration==null) {
					configuration = loadConfiguration(new FileInputStream(fileName));
				}
			}
		}
		return configuration;
	}
	public static Configuration loadConfiguration(InputStream inputStream) throws IOException {
		if (configuration==null) {
			synchronized(Configuration.class) {
				if (configuration==null) {
					Properties properties = new Properties();
					properties.load(inputStream);
					configuration = loadConfiguration(properties);
				}
			}
		}
		return configuration;
	}
	public static Configuration loadConfiguration(Properties properties) {
		if (configuration==null) {
			synchronized(Configuration.class) {
				if (configuration==null) {
					configuration = new Configuration();
					configuration.appName = properties.getProperty("ap_name");
					configuration.role = Role.valueOf(properties.getProperty("role"));
					configuration.accessCredentials = properties.getProperty("access_credentials");
					configuration.host = properties.getProperty("host");
					configuration.setPort(Integer.parseInt(properties.getProperty("port")));
					configuration.commandTimeout = Long.parseLong(properties.getProperty("command_timeout"));
					configuration.lockTimeout = Long.parseLong(properties.getProperty("lock_timeout"));
					configuration.dataFolder = properties.getProperty("data_folder");
				}
			}
		}
		return configuration;
	}

	private String appName;
	private Role role;
	private String accessCredentials;
	private String host;
	private int port;
	private long commandTimeout;
	private long lockTimeout;
	private String dataFolder;

	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}

	public String getAccessCredentials() {
		return accessCredentials;
	}
	public void setAccessCredentials(String accessCredentials) {
		this.accessCredentials = accessCredentials;
	}

	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}

	public long getCommandTimeout() {
		return commandTimeout;
	}
	public void setCommandTimeout(long commandTimeout) {
		this.commandTimeout = commandTimeout;
	}

	public String getDataFolder() {
		return dataFolder;
	}
	public void setDataFolder(String dataFolder) {
		this.dataFolder = dataFolder;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public long getLockTimeout() {
		return lockTimeout;
	}
	public void setLockTimeout(long lockTimeout) {
		this.lockTimeout = lockTimeout;
	}
	
}
