import java.io.*;
import java.net.*;
import java.security.*;
import javax.crypto.*;
import java.util.Base64.*;

public class Server {

	public static void main(String[] args) throws Exception
	{
		// Set my alias and password to make use easier
		String alias="juanca";
        char[] password="password".toCharArray();

        int port = 7999;
		ServerSocket server = new ServerSocket(port);
		Socket s = server.accept();
		ObjectInputStream is = new ObjectInputStream(s.getInputStream());

		//Read the keystore and retrieve the server's private key
        KeyStore ks = KeyStore.getInstance("jks");
        ks.load(new FileInputStream("keystore.jks"), password);
        PrivateKey myPrivateKey = (PrivateKey)ks.getKey(alias, password);

        // Decrypt (using server's private key) and print message sent by client
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        byte[] encipheredMessageBytes = (byte[]) is.readObject();
		cipher.init(Cipher.DECRYPT_MODE, myPrivateKey);
		byte[] messageBytes = cipher.doFinal(encipheredMessageBytes);
		String message = new String(messageBytes);
		System.out.println("Message received: " + message);

		server.close();
		is.close();
		s.close();

		System.exit(0);
	}

}
