package picky;

import org.yaml.snakeyaml.Yaml;

import picky.node.Role;

/**
 * Configuration, load from yml and write to yml.
 * 
 * @author FrankNPC
 *
 */
public final class Configuration {

	private static Yaml yaml = new Yaml();
	private static volatile Configuration configuration;
	public static Configuration getConfiguration() {
		if (configuration==null) {
			synchronized(Configuration.class) {
				if (configuration==null) {
					configuration = yaml.loadAs("", Configuration.class);
				}
			}
		}
		return configuration;
	}
	public static Configuration loadConfiguration(String fileName) {
		if (configuration==null) {
			synchronized(Configuration.class) {
				if (configuration==null) {
					configuration = yaml.loadAs(
							Configuration.class.getClassLoader().getResourceAsStream(fileName), 
							Configuration.class);
				}
			}
		}
		return configuration;
	}
	public void persist() {
		yaml.dump(this);
	}

	private String appName;
	private Role role;
	private String token;
	private int port;
	private String syncHost;
	private String boardHost;
	private boolean compress;
	private String dataFolder;
	private int getAgentAvailable;
	private int getAgentPercentage;
	private int getStorageAvailable;
	private int getStoragePercentage;
	private boolean doubleSet;

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
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getSyncHost() {
		return syncHost;
	}
	public void setSyncHost(String syncHost) {
		this.syncHost = syncHost;
	}
	public String getBoardHost() {
		return boardHost;
	}
	public void setBoardHost(String boardHost) {
		this.boardHost = boardHost;
	}
	public boolean getCompress() {
		return compress;
	}
	public void setCompress(boolean compress) {
		this.compress = compress;
	}
	public String getDataFolder() {
		return dataFolder;
	}
	public void setDataFolder(String dataFolder) {
		this.dataFolder = dataFolder;
	}
	public int getGetAgentAvailable() {
		return getAgentAvailable;
	}
	public void setGetAgentAvailable(int getAgentAvailable) {
		this.getAgentAvailable = getAgentAvailable;
	}
	public int getGetAgentPercentage() {
		return getAgentPercentage;
	}
	public void setGetAgentPercentage(int getAgentPercentage) {
		this.getAgentPercentage = getAgentPercentage;
	}
	public int getGetStorageAvailable() {
		return getStorageAvailable;
	}
	public void setGetStorageAvailable(int getStorageAvailable) {
		this.getStorageAvailable = getStorageAvailable;
	}
	public int getGetStoragePercentage() {
		return getStoragePercentage;
	}
	public void setGetStoragePercentage(int getStoragePercentage) {
		this.getStoragePercentage = getStoragePercentage;
	}
	public boolean getDoubleSet() {
		return doubleSet;
	}
	public void setDoubleSet(boolean doubleSet) {
		this.doubleSet = doubleSet;
	}
	
}
