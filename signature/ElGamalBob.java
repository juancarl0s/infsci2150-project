import java.io.*;
import java.net.*;
import java.security.*;
import java.math.BigInteger;

public class ElGamalBob
{
	private static BigInteger getLeftSide( BigInteger y, BigInteger a, BigInteger b, BigInteger p )
	{
		return ((y.modPow(a, p)).multiply(a.modPow(b, p))).mod(p);
	}

	private static BigInteger getRightSide( BigInteger g, BigInteger m, BigInteger p )
	{
		return (g.modPow(m, p));
	}

	private static boolean verifySignature(	BigInteger y, BigInteger g, BigInteger p, BigInteger a, BigInteger b, String message)
	{
		// IMPLEMENT THIS FUNCTION;
		BigInteger left = getLeftSide(y, a, b, p);
		BigInteger right = getRightSide(g, new BigInteger(message.getBytes()), p);

		if (left.equals(right) == true)
			return true;
		else
			return false;
	}

	public static void main(String[] args) throws Exception
	{
		int port = 7999;
		ServerSocket s = new ServerSocket(port);
		Socket client = s.accept();
		ObjectInputStream is = new ObjectInputStream(client.getInputStream());

		// read public key
		BigInteger y = (BigInteger)is.readObject();
		BigInteger g = (BigInteger)is.readObject();
		BigInteger p = (BigInteger)is.readObject();

		// read message
		String message = (String)is.readObject();

		// read signature
		BigInteger a = (BigInteger)is.readObject();
		BigInteger b = (BigInteger)is.readObject();

		boolean result = verifySignature(y, g, p, a, b, message);

		System.out.println(message);

		if (result == true)
			System.out.println("Signature verified.");
		else
			System.out.println("Signature verification failed.");

		is.close();
		s.close();
	}
}
