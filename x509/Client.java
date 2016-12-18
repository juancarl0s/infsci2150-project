import java.io.*;
import java.net.*;
import javax.crypto.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Scanner;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.InvalidKeyException;
import java.security.SignatureException;

public class Client {

	public static void main(String[] args) throws Exception
	{
		String host = "localhost";
		int port = 7999;
		Socket s = new Socket(host, port);

		// read certificate file
		FileInputStream is = new FileInputStream("server.cer");
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate)cf.generateCertificate(is);
        is.close();

		// Print the string representation of the certificate
        System.out.println("Press enter to print a string representation of the server's certificate");
		System.in.read();
        System.out.println(cert.toString());
		System.out.println("End of certificate string representation\n");

		// check current date is within certificate valid dates
		Date now = new Date();
		try {
			cert.checkValidity(now);
			System.out.println("Certificate is currently valid! " + "(Time of check: " + now.toString() + ")");
		} catch (CertificateExpiredException e) {
			System.out.print("Certificate expired on " + cert.getNotAfter());
			System.exit(0);
		} catch (CertificateNotYetValidException e) {
			System.out.print("Certificate is not valid until " + cert.getNotBefore());
			System.exit(0);
		}

		// verify the public key within the .cert file
		try {
			cert.verify(cert.getPublicKey());
			System.out.println("The public key from the certificate was successfully verified!");
		} catch (InvalidKeyException e) {
			System.out.println("Public key is not valid with respect to the certificate checked");
			System.exit(0);
		} catch (SignatureException e) {
			System.out.println("Signature exception");
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// gather message from user to send to server
        System.out.print("\nMessage to encrypt and send to the server: ");
        Scanner inputMsg = new Scanner(System.in);
        String message = inputMsg.nextLine();
		inputMsg.close();

		// get the server's public key from the certificate
		RSAPublicKey eServer = (RSAPublicKey) cert.getPublicKey();
        // encrypt using the server's public key
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, eServer);
        byte[] cipherText = cipher.doFinal(message.getBytes());

		// write message to stream and close after done
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
        os.writeObject(cipherText);
		os.flush();
		os.close();

	    s.close();
		
		System.exit(0);
	}



}
