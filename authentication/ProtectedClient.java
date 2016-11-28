package authentication;
// import Protection;
import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Date;
import java.util.Random;

public class ProtectedClient
{
	public void sendAuthentication(String user, String password, OutputStream outStream) throws IOException, NoSuchAlgorithmException
	{
		DataOutputStream out = new DataOutputStream(outStream);

		// IMPLEMENT THIS FUNCTION.

		Random r = new Random();

		double q1 = r.nextDouble();
		long t1 = System.currentTimeMillis(); //date.getTime();

		byte[] digest_1 = Protection.makeDigest(user, password, t1, q1);

		double q2 = r.nextDouble();
		long t2 = System.currentTimeMillis(); //date.getTime();

		byte[] digest_2 = Protection.makeDigest(digest_1, t2, q2);


		out.writeUTF(user);
		out.writeDouble(q1);
		out.writeLong(t1);
		out.writeDouble(q2);
		out.writeLong(t2);


		out.writeInt(digest_2.length);
		out.write(digest_2);

		out.flush();
	}

	public static void main(String[] args) throws Exception
	{
		String host = "localhost";
		int port = 7999;
		String user = "George";
		String password = "abc123";
		Socket s = new Socket(host, port);

		ProtectedClient client = new ProtectedClient();
		client.sendAuthentication(user, password, s.getOutputStream());

		s.close();
	}
}
