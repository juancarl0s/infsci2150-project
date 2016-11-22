import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 *
 * @author juan
 */
public class MsgDigest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        String str = args[0];

        byte[] strToHashInBytes = str.getBytes();

        StringBuffer result = new StringBuffer(32);

        Formatter f = new Formatter(result);

        try{
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(strToHashInBytes);
        byte[] strDigest;
        strDigest = md.digest();
        for (int i = 0; i < strDigest.length; i++)
        {
            f.format("%02x", new Object[] { new Byte(strDigest[i]) });
        }


        }
        catch (NoSuchAlgorithmException ex)
	{
		ex.printStackTrace();
	}


    System.out.println(result.toString());
    System.exit(0);
    }

}
