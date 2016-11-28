package authentication;
import java.io.*;
import java.net.*;
import java.security.*;

public class ProtectedServer
{
	public boolean authenticate(InputStream inStream) throws IOException, NoSuchAlgorithmException
	{
		DataInputStream in = new DataInputStream(inStream);

		// IMPLEMENT THIS FUNCTION.

		String user = in.readUTF();
		double q1 = in.readDouble();
		long t1 = in.readLong();
		double q2 = in.readDouble();
		long t2 = in.readLong();

		int length = in.readInt();

		byte[] receivedDigest = new byte[length];

		in.readFully(receivedDigest);

		byte[] recomputedDigest1 = Protection.makeDigest(user, lookupPassword(user), t1, q1);

		byte[] recomputedDigest2 = Protection.makeDigest(recomputedDigest1, t2, q2);

		return MessageDigest.isEqual(recomputedDigest2, receivedDigest);
	}

	protected String lookupPassword(String user) { return "abc123"; }

	public static void main(String[] args) throws Exception
	{
		int port = 7999;
		ServerSocket s = new ServerSocket(port);
		Socket client = s.accept();

		ProtectedServer server = new ProtectedServer();

		if (server.authenticate(client.getInputStream()))
		  System.out.println("Client logged in.");
		else
		  System.out.println("Client failed to log in.");

		s.close();
	}
}
