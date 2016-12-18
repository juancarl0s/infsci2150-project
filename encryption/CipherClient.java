import java.io.*;
import java.net.*;
import java.security.*;
import javax.crypto.*;

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
			e.printStackTrace();
			System.exit(0);
		}

		// generate key and save it to a file
		keyGenerator.init(56, new SecureRandom());
		Key key = keyGenerator.generateKey();
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("KeyFile.xx", false));

		out.writeObject(key);


		// -Use the key to encrypt the message above and send it over socket s to the server.
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

		cipher.init(Cipher.ENCRYPT_MODE, key);

		byte[] messageBytes = message.getBytes("UTF-8");

		CipherOutputStream cipherOut = new CipherOutputStream(s.getOutputStream(), cipher);

		cipherOut.write(messageBytes, 0, messageBytes.length);

		out.close();
		cipherOut.close();

		s.close();

		System.exit(0);

	}
}
