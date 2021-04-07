package rs2;

import rs2.util.LoggerUtils;
import rs2.util.Stopwatch;

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

        final Stopwatch timer = new Stopwatch().reset();

        new Bootstrap().build().bind();

        uptime = new Stopwatch();

        logger.info("Server Initialized. [Took " + timer.elapsed(TimeUnit.SECONDS) + " seconds]");

    }

    public static Stopwatch getUptime() {
        return uptime;
    }

}
