package rs2.net;

import com.google.common.collect.ImmutableList;
import com.moandjiezana.toml.Toml;

import rs2.net.channel.PlayerChannel;
import rs2.net.channel.UpstreamChannelHandler;
import io.netty.util.AttributeKey;

import java.io.File;
import java.math.BigInteger;

/**
 * A class which holds network-related constants.
 * 
 * @author Vult-R
 */
public final class NetworkConstants {

	static {

		Toml parser = new Toml().read(new File("./settings.toml")).getTable("network");

		try {
			CONNECTION_LIMIT = Math.toIntExact(parser.getLong("connection_limit"));
			JAGGRAB_PORT = Math.toIntExact(parser.getLong("jaggrab_port"));
			INPUT_TIMEOUT = Math.toIntExact(parser.getLong("connection_timeout"));
			PACKET_LIMIT = Math.toIntExact(parser.getLong("packet_limit"));
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}

	}

	public static final int CONNECTION_LIMIT;

	/**
	 * The JAGGRAB port.
	 */
	public static final int JAGGRAB_PORT;

	/**
	 * The number of seconds until a connection becomes idle.
	 */
	public static final int INPUT_TIMEOUT;

	/**
	 * The amount of packets that can be decoded in one sequence.
	 */
	public static final int PACKET_LIMIT;

	/**
	 * The attribute that contains the key for a players session.
	 */
	public static final AttributeKey<PlayerChannel> SESSION_KEY = AttributeKey.valueOf("session.key");

	public static final BigInteger RSA_EXPONENT = new BigInteger("102875294057923777792850609343436659953539251173029350569294750373465534552408165669599130685683867465642949755856101281107833473286344997717868738856176649239904245858443336156741296533091853923106542398481561854703567523687792523234526411901280439425328675765417028582964517545416274698153347746819045751633");
	public static final BigInteger RSA_MODULUS = new BigInteger("107600474739050266126335408872481341600968765925514683417543689736922241624685583132327650102102868256688456536962628028854020688787998405902359813249768594645318545356487058358067724294593134242609109653841885815224979234366900027792579379745974887642897165815893505403639948618461142105419007489743939330577");

	/**
	 * The list of exceptions that are ignored and discarded by the
	 * {@link UpstreamChannelHandler}.
	 */
	public static final ImmutableList<String> IGNORED_EXCEPTIONS = ImmutableList.of(
			"An existing connection was forcibly closed by the remote host",
			"An established connection was aborted by the software in your host machine");

	/**
	 * An array of message opcodes mapped to their respective sizes.
	 */
	public static final int PACKET_SIZES[] = new int[257];

}
