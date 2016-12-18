import javax.crypto.Cipher;
import java.io.*;
import java.net.*;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.interfaces.RSAPrivateKey;

public class Receiver {

	public static void main(String[] args) throws Exception {

		// generate my public and private key
		int mStrength = 1024;
		SecureRandom mSecureRandom = new SecureRandom();
		KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
		keyGenerator.initialize(mStrength, mSecureRandom);
		KeyPair keys = keyGenerator.generateKeyPair();
		RSAPublicKey myPublicKey = (RSAPublicKey) keys.getPublic();
		RSAPrivateKey myPrivateKey = (RSAPrivateKey) keys.getPrivate();

		// save my public key into a file
		ObjectOutputStream outFile = new ObjectOutputStream(new FileOutputStream("ReceiverPublicKey.xx"));
		outFile.writeObject(myPublicKey);
		outFile.close();

		System.out.println("Hello, I am the receiver. Waiting for the sender...");

		// set up my socket to communicate
	    int port = 7999;
		ServerSocket s = new ServerSocket(port);
		Socket client = s.accept();

		// get sender's selection to know which key to use for dencryption
		ObjectInputStream is = new ObjectInputStream(client.getInputStream());
		int selectionValue = is.readInt();
		// System.out.println("Received: " + selectionValue);

		// get the sender's public key
		ObjectInputStream in = new ObjectInputStream(new FileInputStream("SenderPublicKey.xx"));
		RSAPublicKey senderPublicKey = (RSAPublicKey) in.readObject();
		in.close();

		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

		if (selectionValue == 1)
		{
			// Confidentiality: decrypt with my (receiver's) private key
		  	byte[] cipherText = (byte[]) is.readObject();
		  	cipher.init(Cipher.DECRYPT_MODE, myPrivateKey);
			byte[] messageBytes = cipher.doFinal(cipherText);
			String message = new String(messageBytes);
			System.out.println("Confidential message received: " + message);
		}
		else if (selectionValue == 2)
		{
			// Confidentiality: decrypt with the sender's public key
			byte[] cipherText = (byte[]) is.readObject();
		  	cipher.init(Cipher.DECRYPT_MODE, senderPublicKey);
			byte[] messageBytes = cipher.doFinal(cipherText);
			String message = new String(messageBytes);
			System.out.println("Message received from authenticated sender: " + message);
		}
		else
		{
			System.out.println("Invalid selection from the sender. Bye!");
		}

		is.close();
		System.exit(0);
		}
	}
