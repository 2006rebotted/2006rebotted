package rs2;

import rs2.util.LoggerUtils;
import rs2.util.Stopwatch;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * The core class of the server.
 *
 * @author Vult-R
 */
public final class Server {

    /**
     * The single logger for this class.
     */
    private static final Logger logger = LoggerUtils.getLogger(Server.class);

    /**
     * To check if the server needs to be updated.
     */
    public static boolean updateServer = false;    

    /**
     * Determines if the server has started.
     */
    public static boolean serverStarted = false;    

    /**
     * The elapsed time the server has been running.
     */
    public static Stopwatch uptime; 

    /**
     * The main entry point to the server.
     *
     * @param args The command line arguments.
     *
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        if (!new File("data").exists()) {
            System.out.println("************************************");
            System.out.println("************************************");
            System.out.println("************************************");
            System.out.println("WARNING: I could not find the /data folder. You are LIKELY running this in the wrong directory!");
            System.out.println("In IntelliJ, fix it by clicking \"Server\" > Edit Configurations at the top of your screen");
            System.out.println("Then changing the \"Working Directory\" to be in \"2006Scape/Server\", instead of just \"2006Scape\"");
            System.out.println("************************************");
            System.out.println("************************************");
            System.out.println("************************************");
            System.exit(1);
        }

        final Stopwatch timer = new Stopwatch().reset();

        new Bootstrap().build().bind();

        uptime = new Stopwatch();

        logger.info("Server Initialized. [Took " + timer.elapsed(TimeUnit.SECONDS) + " seconds]");

    }

    public static Stopwatch getUptime() {
        return uptime;
    }

}
