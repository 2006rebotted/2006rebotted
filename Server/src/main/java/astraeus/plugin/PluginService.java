package astraeus.plugin;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import astraeus.game.event.EventSubscriber;
import astraeus.game.model.World;
import astraeus.util.LoggerUtils;

/**
 * The service that services plugins.
 * 
 * @author Vult-R
 */
public final class PluginService {

	/**
	 * The single logger for this class.
	 */
	private static final Logger logger = LoggerUtils.getLogger(PluginService.class);	

	/**
	 * The list of subscribers registered to the server.
	 */
	private static final List<EventSubscriber<?>> subscribers = new ArrayList<>();	
	
	/**
	 * The single instance of gson to deserialize the plugin meta data.
	 */	
	private static final Gson gson = new GsonBuilder().create();	

	/**
	 * Loads the plugins.
	 */
	public void load() {
		try {
			Collection<PluginMetaData[]> plugins = findPlugins();
			
			plugins.stream().forEach($it -> register($it));			
		} catch (IOException e) {
			logger.log(Level.SEVERE, "A problem was encountered while trying to load plugins.", e);
		}
		logger.info("Loaded: " + subscribers.size() + " plugins.");
	}
	
	/**
	 * Finds plugins in a given directory.
	 *
	 * @throws IOException
	 */
	private Collection<PluginMetaData[]> findPlugins() throws IOException {
		return findPlugins(new File("./plugins/"));
	}
	
	/**
	 * Finds plugins in a specified directory.
	 * 
	 * @param dir
	 * 		The directory to check for plugins.
	 * 
	 * @throws IOException
	 * 
	 * @return The collection of plugin data.
	 */
	private Collection<PluginMetaData[]> findPlugins(File dir) throws IOException {		
		Collection<PluginMetaData[]> plugins = new ArrayList<>();
		for(File file : dir.listFiles()) {			
			if (file.isDirectory()) {
				
				File json = new File(file, "plugins.json");
				
				if (json.exists()) {
					PluginMetaData[] meta = gson.fromJson(new FileReader(json), PluginMetaData[].class);
					
					plugins.add(meta);
				} else {
					plugins.addAll(findPlugins(file));
				}
				
			}
		}
		return Collections.unmodifiableCollection(plugins);
	}
	
	/**
	 * Assigns a plugin to a subscriber
	 * 
	 * @param metas
	 *		The meta deta for each plugin.
	 */
	private void register(PluginMetaData[] metas) {
		for(PluginMetaData meta : metas) {
			String base = meta.getBase();

			Class<?> clazz;

			try {

				clazz = Class.forName(base);

			} catch (Exception ex) {
				logger.warning(base + " could not be found.");
				continue;
			}

			if (EventSubscriber.class.isAssignableFrom(clazz)) {
				try {
					final EventSubscriber<?> subscriber = (EventSubscriber<?>) clazz.newInstance();

					World.world.provideSubscriber(subscriber);

					subscribers.add(subscriber);
				} catch (Exception ex) {
					logger.warning(base + " could not be created.");
					continue;
				}
			}
		}
	}

	/**
	 * Reloads plugins.
	 */
	public void reload() {
		throw new UnsupportedOperationException("This is currently not supported.");
	}

	/**
	 * Gets the list of subscriber registered to the server.
	 */
	public List<EventSubscriber<?>> getSubscribers() {
		return subscribers;
	}

}
