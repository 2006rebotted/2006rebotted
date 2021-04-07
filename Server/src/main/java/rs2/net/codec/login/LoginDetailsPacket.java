package rs2.net.codec.login;

import rs2.net.codec.IsaacCipher;
import io.netty.channel.ChannelHandlerContext;

/**
 * The upstream packet that contains information about a player.
 * 
 * @author Vult-R
 */
public final class LoginDetailsPacket {

	/**
	 * The context to which this player is going through.
	 */
    private final ChannelHandlerContext context;

    /**
     * The username for this user.
     */
    private final String username;

    /**
     * The password for this user.
     */
    private final String password;

    /**
     * The universal unique identifier for this player.
     */
    private final String uuid;

    /**
     * The encrypting isaac
     */
    private final IsaacCipher encryptor;

    /**
     * The decrypting isaac
     */
    private final IsaacCipher decryptor;

    /**
     * Creates a new {@link LoginDetailsPacket}.
     * 
     * @param context
     * 		The context to which this player is going through
     * 
     * @param username
     * 		The username for this player
     * 
     * @param password
     * 		The password for this player
     * 	
     * @param uuid
     * 		The universal unique identifier for this player
     * 
     * @param encrptor
     * 		The encrypting isaac
     * 
     * @param decrpytor
     * 		The decrypting isaac
     */
    public LoginDetailsPacket(ChannelHandlerContext context, String username, String password, String uuid,
                IsaacCipher encryptor, IsaacCipher decryptor) {
          this.context = context;
          this.username = username;
          this.uuid = uuid;
          this.password = password;
          this.encryptor = encryptor;
          this.decryptor = decryptor;
    }

    public ChannelHandlerContext getContext() {
          return context;
    }

    public String getUsername() {
          return username;
    }

    public String getPassword() {
          return password;
    }

    public String getUUID() {
        return uuid;
    }

    public IsaacCipher getEncryptor() {
          return encryptor;
    }

    public IsaacCipher getDecryptor() {
          return decryptor;
    }

}

