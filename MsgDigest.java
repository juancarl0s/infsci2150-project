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

        String str = args[0];

        byte[] strToHashInBytes = str.getBytes();

        StringBuilder resultMD5 = new StringBuilder();
        StringBuilder resultSHA = new StringBuilder();

        Formatter fMD5 = new Formatter(resultMD5);
        Formatter fSHA = new Formatter(resultSHA);

        try{
            MessageDigest mdMD5 = MessageDigest.getInstance("MD5");
            MessageDigest mdSHA = MessageDigest.getInstance("SHA");

            mdMD5.update(strToHashInBytes);
            mdSHA.update(strToHashInBytes);

            byte[] strDigest;

            strDigest = mdMD5.digest();
            for (int i = 0; i < strDigest.length; i++)
            {
                fMD5.format("%02x", new Object[] { new Byte(strDigest[i]) });
            }

            strDigest = mdSHA.digest();
            for (int i = 0; i < strDigest.length; i++)
            {
                fSHA.format("%02x", new Object[] { new Byte(strDigest[i]) });
            }
        } catch (Exception e)
    	{
    		e.printStackTrace();
    	}

    System.out.println("MD5 hash: " + resultMD5.toString());

    System.out.println("SHA hash: " + resultSHA.toString());

    System.exit(0);
    }

}
