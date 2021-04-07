package rs2.plugin;

public final class PluginMetaData {
	
	private final String name;
	
	private final String description;
	
	private final String group;
	
	private final String base;

	private final String[] authors;

	private final double version;

	public PluginMetaData(String name, String description, String group, String base, String[] authors, double version) {
		this.name = name;
		this.description = description;
		this.group = group;
		this.base = base;
		this.authors = authors;
		this.version = version;
	}
	
	public String getName() {
		return name;
	}

	public String[] getAuthors() {
		return authors;
	}

	public String getDescription() {
		return description;
	}
	
	public String getGroup() {
		return group;
	}
	
	public String getBase() {
		return base;
	}

	public double getVersion() {
		return version;
	}

}
