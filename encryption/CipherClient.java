import java.io.*;
import java.net.*;
import java.security.*;
import javax.crypto.*;
// import

public class CipherClient
{
	public static void main(String[] args) throws Exception
	{
		String message = "The quick brown fox jumps over the lazy dog.";
		String host = "localhost";
		int port = 7999;
		Socket s = new Socket(host, port);

		// YOU NEED TO DO THESE STEPS:
		// -Generate a DES key.

		KeyGenerator keyGenerator = null;
		try
		{
			keyGenerator = KeyGenerator.getInstance("DES");
		}
		catch (NoSuchAlgorithmException e)
		{
			// _log.error("Unsupported algorithm: " + algorithm + ", size: " + keySize, e);
			// return null;
			System.exit(0);
		}

		keyGenerator.init(56, new SecureRandom());

		Key key = keyGenerator.generateKey();

		System.out.println(key.getFormat());
		System.out.println(key.getEncoded());

		// byte[] encoded = key.getEncoded();

		// -Store it in a file. (see http://stackoverflow.com/questions/6403662/how-to-use-a-key-generated-by-keygenerator-at-a-later-time)
				// http://stackoverflow.com/questions/27064383/difference-between-key-material-and-actual-key
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("KeyFile.xx", false));

		out.writeObject(key);

		// out.close();

		// -Use the key to encrypt the message above and send it over socket s to the server.
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

		cipher.init(Cipher.ENCRYPT_MODE, key);

		byte[] messageBytes = message.getBytes("UTF-8");

		// byte[] encryptedMessage = new byte[cipher.getOutputSize(messageBytes.length)];

		// int enc_len = cipher.doFinal(message, 0, message.length, encryptedMessage, 0);
		// encryptedMessage = cipher.doFinal(messageBytes, 0, messageBytes.length);

		// enc_len += cipher.doFinal(encrypted, enc_len);
		CipherOutputStream cipherOut = new CipherOutputStream(s.getOutputStream(), cipher);

		// cipherOut.write(encryptedMessage);
		cipherOut.write(messageBytes, 0, messageBytes.length);

		out.close();
		cipherOut.close();

		// System.out.println("messageBytes length:" + messageBytes.length);

		System.exit(0);

	}
}
