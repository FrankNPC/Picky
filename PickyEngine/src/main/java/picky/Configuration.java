package picky;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import picky.node.Role;

/**
 * 
 * @author FrankNPC
 *
 */
public class Configuration {

	private static volatile Configuration configuration;
	public static Configuration getConfiguration() {
		if (configuration==null) {
			synchronized(Configuration.class) {
				if (configuration==null) {
					try {
						configuration = getConfiguration(
											Configuration.class.getClassLoader()
												.getResourceAsStream("picky.yml"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return configuration;
	}
	public static Configuration getConfiguration(String fileName) throws IOException {
		if (configuration==null) {
			synchronized(Configuration.class) {
				if (configuration==null) {
					configuration = getConfiguration(new FileInputStream(fileName));
				}
			}
		}
		return configuration;
	}
	public static Configuration getConfiguration(InputStream inputStream) throws IOException {
		if (configuration==null) {
			synchronized(Configuration.class) {
				if (configuration==null) {
					Properties properties = new Properties();
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
					String line = null;
					while((line=reader.readLine())!=null) {
						if (line.indexOf(':')>0) {
							properties.put(line.substring(0, line.indexOf(':')).trim(), 
									line.substring(line.indexOf(':')+1).trim());
						}
					}
					configuration = getConfiguration(properties);
				}
			}
		}
		return configuration;
	}
	public static Configuration getConfiguration(Properties properties) {
		if (configuration==null) {
			synchronized(Configuration.class) {
				if (configuration==null) {
					configuration = new Configuration();
					configuration.nodeName = properties.getProperty("node_name");
					configuration.role = Role.forName(properties.getProperty("role"));
					configuration.accessCredentials = properties.getProperty("access_credentials");
					configuration.host = properties.getProperty("host");
					configuration.setPort(Integer.parseInt(properties.getProperty("port")));
					configuration.commandTimeout = Long.parseLong(properties.getProperty("command_timeout"));
					configuration.lockTimeout = Long.parseLong(properties.getProperty("lock_timeout"));
					configuration.storageCacheTimeout = Long.parseLong(properties.getProperty("storage_cache_timeout"));
					configuration.dataFolder = properties.getProperty("data_folder");
				}
			}
		}
		return configuration;
	}

	private String nodeName;
	private Role role;
	private String accessCredentials;
	private String host;
	private int port;
	private long commandTimeout;
	private long lockTimeout;
	private long storageCacheTimeout;
	private String dataFolder;

	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
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
	public long getStorageCacheTimeout() {
		return storageCacheTimeout;
	}
	public void setStorageCacheTimeout(long storageCacheTimeout) {
		this.storageCacheTimeout = storageCacheTimeout;
	}
	
}
