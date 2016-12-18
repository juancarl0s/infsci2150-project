import javax.crypto.Cipher;
import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Scanner;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class Sender {

	public static void main(String[] args) throws Exception {

		// message to send
		String message = "The quick brown fox jumps over the lazy dog.";

		// generate my public and private key
		int mStrength = 1024;
		SecureRandom mSecureRandom = new SecureRandom();
		KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
		keyGenerator.initialize(mStrength, mSecureRandom);
		KeyPair keys = keyGenerator.generateKeyPair();
		RSAPublicKey myPublicKey = (RSAPublicKey) keys.getPublic();
		RSAPrivateKey myPrivateKey = (RSAPrivateKey) keys.getPrivate();

		// save my public key into a file
		ObjectOutputStream outFile = new ObjectOutputStream(new FileOutputStream("SenderPublicKey.xx"));
		outFile.writeObject(myPublicKey);
		outFile.close();

		// get the receiver's public key
		ObjectInputStream in = new ObjectInputStream(new FileInputStream("ReceiverPublicKey.xx"));
		RSAPublicKey receiverPublicKey = (RSAPublicKey) in.readObject();

		// set up my socket to communicate
		String host = "localhost";
		int port = 7999;
		Socket s = new Socket(host, port);

		// get user input to know which key to use for encryption
		System.out.println("Hello, I am the sender, please make a selection:");
		System.out.println("1) Confidentiality");
	    System.out.println("2) Integrity/Authentication");
		Scanner selection = new Scanner(System.in);

		// send the selection to the receiver so he knows what decryption key to use
		int selectionValue = selection.nextInt();
		selection.close();
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
		os.writeInt(selectionValue);

		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

		if (selectionValue == 1)
		{
			// Confidentiality: encrypt with the receiver's public key
			cipher.init(Cipher.ENCRYPT_MODE, receiverPublicKey);
			byte[] cipherText = cipher.doFinal(message.getBytes());
			os.writeObject(cipherText);
		}
		else if (selectionValue == 2)
		{
			// Integrity/Authentication: encrypt with my (sender's) private key
			cipher.init(Cipher.ENCRYPT_MODE, myPrivateKey);
			byte[] cipherText = cipher.doFinal(message.getBytes());
			os.writeObject(cipherText);
		}
		else
		{
			System.out.println("Invalid selection. Bye!");
		}

		os.flush();
		os.close();
		s.close();
		System.exit(0);
		}
	}
