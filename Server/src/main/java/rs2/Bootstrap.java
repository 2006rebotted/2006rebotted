package rs2;

import rs2.game.model.World;
import rs2.io.*;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakDetector.Level;
import rs2.net.channel.ChannelPiplineInitializer;
import rs2.net.packet.IncomingPacketHandlerRegistration;
import rs2.service.GameService;
import rs2.service.GameServiceSequencer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * The bootstrap that will prepare the game and network.
 * 
 * @author Vult-R
 */
public final class Bootstrap {

	/**
	 * The single logger for this class.
	 */
	public static final Logger logger = Logger.getLogger(Bootstrap.class.getName());

	/**
	 * The {@link ExecutorService} that will run the startup services.
	 */
	private final ExecutorService serviceLoader = Executors
			.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("ServiceLoaderThread").build());

	/**
	 * The engine that manages the games logic.
	 */
	private final GameService service = new GameServiceSequencer();

	/**
	 * Builds the game by executing any startup services, and starting the game
	 * loop.
	 * 
	 * @return The instance of this bootstrap.
	 */
	public Bootstrap build() throws Exception {
		logger.info("Unpacking game resources...");
		// load and cache data
		executeStartupServices();

		serviceLoader.shutdown();

		if (!serviceLoader.awaitTermination(15, TimeUnit.MINUTES)) {
			throw new IllegalStateException("The background service load took too long!");
		}

		logger.info("Preparing game engine...");
		// run the service for the game loop
		service.start();
		logger.info("Game Engine has been built");
		return this;
	}

	/**
	 * Builds the network by creating the netty server bootstrap and binding to
	 * a specified port.
	 * 
	 * @return The instance of this bootstrap.
	 */
	public Bootstrap bind() throws InterruptedException {
		logger.info("Building network");
		ResourceLeakDetector.setLevel(Level.DISABLED);
		EventLoopGroup loopGroup = new NioEventLoopGroup();

		ServerBootstrap bootstrap = new ServerBootstrap();

		bootstrap.group(loopGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelPiplineInitializer())
				.bind(43594).syncUninterruptibly();
		Server.serverStarted = true;
		logger.info("Network has been bound");
		return this;
	}

	/**
	 * Executes external files to be used in game.
	 */
	private void executeStartupServices() {

		logger.info("Initializing packets...");
		serviceLoader.execute(() -> {
			new PacketSizeParser().run();
			new IncomingPacketHandlerRegistration();
		});
		
		logger.info("Loading startup files..");
		serviceLoader.execute(() -> {
			new GlobalObjectParser().run();
			new ItemDefinitionParser().run();
			new NpcDefinitionParser().run();
			new NpcSpawnParser().run();
			new IPBanParser().run();
			new UUIDBanParser().run();
			new EquipmentDefinitionParser().run();
			//MapDecoder.load();
		});		
		
		logger.info("Loading plugins");
		serviceLoader.execute(() -> World.world.getPluginService().load());

	}

}
