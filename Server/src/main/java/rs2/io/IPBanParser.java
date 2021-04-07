package rs2.io;

import rs2.game.model.World;
import rs2.util.TextFileParser;

import java.io.IOException;
import java.util.Scanner;

/**
 * Parses the ip bans
 *
 * @author Seven
 */
public final class IPBanParser extends TextFileParser {

	/**
	 * Creates a new {@link IPBanParser}.
	 */
	public IPBanParser() {
		super("./data/punishment/ip_bans");
	}

	@Override
	public void parse(Scanner reader) throws IOException {
		String ip_bans = reader.nextLine();
		World.world.getIpBans().add(ip_bans);
	}

}
