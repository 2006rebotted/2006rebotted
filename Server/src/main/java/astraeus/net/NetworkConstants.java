package astraeus.net;

import com.google.common.collect.ImmutableList;
import com.moandjiezana.toml.Toml;

import astraeus.net.channel.PlayerChannel;
import astraeus.net.channel.UpstreamChannelHandler;
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

	public static final BigInteger RSA_EXPONENT = new BigInteger("197151575875955611272726317568769064495393697896313589941263639293236835129372473560213305706927262925184529786090502951801847937328657100826220830431883545171850964877658940891431446133788830043561329303263101497410652427434732434750586358031079873919944718997397873612769441445383091768003419290359047217");
	public static final BigInteger RSA_MODULUS = new BigInteger("90354705092185334936927724996534392865976341112102823384479686212313723523592194403606289623181063149145584116021071971693969987893064338579356885063037445254360260312444388873521580091350067744677146493334468325125539182479537309035822624862383200035037173565499544436042438525160022053390578519461635813819");

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
