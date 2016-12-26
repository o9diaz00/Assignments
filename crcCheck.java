import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class crccheck
{
	/*
	 * These values are constant
	 * {POLYNOMIAL} is the polynomial we are using (hex format)
	 * {POLYSTRING} is the aforementioned converted to a binary string
	 */
	static final int POLYNOMIAL = 0xA053;
	static final String POLYSTRING = Integer.toBinaryString(POLYNOMIAL);
	
	public static void main(String[] args) throws IOException
	{
		/*
		 * Exception handling if anything else but "calc" or "verify" is entered as the second argument
		 */
		if (args[1].toLowerCase().equals("calc") || args[1].toLowerCase().equals("verify"))
		{ Print(args[0],args[1].toLowerCase()); }
		else
		{
			System.out.println("You have entered an invalid argument\nPlease enter the name of the file for the first argument, and either 'calc' or 'verify' for the second argument");
			System.exit(0);
		}
	}
	
	public static void Print(String f, String type) throws IOException
	{
		/*
		 * {input} is the data returned from the file
		 * {bytes[]} is the aforementioned converted to a byte array
		 * {test} will store the bytes as a binary string
		 * {crc} is the crc appended to the file (only for verification)
		 * {verify} is just a string to tell us if what we calculated matches what is stored
		 * {hex} is the CRC that we calculate ourselves
		 */
		String input = new String(ReadFile(new File(f), type));
	    byte bytes[] = input.getBytes();
	    StringBuilder test = new StringBuilder();
	    StringBuilder crc = new StringBuilder();
	    String verify = "";
	    
	    //The last 8 chars are that of the CRC, so let's grab it
	    for (int i=bytes.length-8; i<bytes.length; i+=1)
	    { crc.append((char)(bytes[i])); }
	    
	    String hex;
		
	    if (type.equals("calc"))
		{
	    	System.out.println("Calculating CRC...\n");
	    	for (int i=0; i<bytes.length; i+=1)
		    {
		    	System.out.print((char)(bytes[i]));
		    	test.append(Integer.toBinaryString(bytes[i]));
		    	if (i+1 < bytes.length)
				{
					if (bytes[i+1] == (int) ('\n'))
					{
						hex = Calc(test.toString());
						System.out.print(" - "+hex);
					}
				}
		    }
	    	hex = Calc(test.toString());
			System.out.print(hex+" - "+hex);
			System.out.println("\n\nCRC16 Result: "+hex);
		}
	    else
	    {
	    	System.out.println("Verifying CRC...\n");
	    	for (int i=0; i<bytes.length-8; i+=1)
	    	{
	    		System.out.print((char)(bytes[i]));
	    		test.append(Integer.toBinaryString(bytes[i]));
	    	}
	    	for (int i=bytes.length-8; i<bytes.length; i+=1)
	    	{ System.out.print((char)(bytes[i])); }
	    	
	    	hex = Calc(test.toString());
	    	System.out.println("\n\nCRC16 Result: "+hex);
	    	for (int i=0; i<8; i+=1)
	    	{ verify = (hex.charAt(i) == (char)(bytes[i+(bytes.length-8)])) ? "passed" : "failed"; }
	    	System.out.println("Verification: "+verify);
	    }
	}
	
	public static String ReadFile(File f, String type) throws IOException
	{
		String str = new String();
		StringBuilder input = new StringBuilder();
		String temp = "";
		
		 //Pretty standard coding for reading a file, line by line and storing its contents
		try
		{
			FileInputStream stream = new FileInputStream(f);
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			while ((str = reader.readLine()) != null)
			{
				if (str != "\n")
				{ input.append(str); }
			}
			reader.close();
		}
		catch(Exception e)
		{
			System.out.println("Sorry, seems like you have input an invalid file/file path");
			System.exit(1);
		}
				
		for (int i=0; i<input.length(); i+=1)
		{
			if (input.charAt(i) < 32 || input.charAt(i) > 126)
			{ input.deleteCharAt(i); }
		}
		
		if (type.equals("verify"))
		{
			//If we are verifying a CRC then the last 8 characters should be the CRC
			temp = input.substring(input.length()-8, input.length());
			if (temp.contains("0000"))
			{ input.delete(input.length()-8, input.length()); }
			else
			{
				System.out.println("This has no CRC to verify");
				System.exit(0);
			}
		}		
		
		while (input.length() < 504)
		{ input.append('.'); }
		
		//I am not sure why, but I had to break up the coding like this to get the 64per line standard correctly
		input.insert(64, '\n');
		for (int i=64; i<input.length(); i+=1)
		{
			if (i%65 == 0 && i > 64)
			{ input.insert(i, '\n'); }
		}
		input.delete(65, 66);
		input.append(temp);
						
		return input.toString();
	}
	
	public static String Calc(String str)
	{
		/*
		 * {string} is the dividend with the appended 0s
		 * {result} is the XOR'd result between the dividend and divisor (16 bits at a time)
		 */
		StringBuilder string = new StringBuilder(str.toString()+"000000000000000");
		StringBuilder result = new StringBuilder();
		
		//We want this operation to keep occurring until we're left with 15 'bits'
		while (string.length() >= 16)
		{
			//The first 0 is always negligible, so delete it
			while (string.charAt(0) == '0')
			{ string.deleteCharAt(0); }
			
			//Once we find a '1', start our XOR operation
			for (int i=0; i<POLYSTRING.length(); i+=1)
			{ result.append(XOR(string.charAt(i), POLYSTRING.charAt(i))); }
			
			/*
			 * Instead of creating anew, we will just reuse the first stringbuilder we created
			 * its first indices we replace with the values we received from our XOR operations performed
			 * Also, we cannot forget to reset our {result} stringbuilder
			 */
			string.delete(0, 16);
			string.insert(0, result.toString());
			result = new StringBuilder();
			while (string.charAt(0) == '0')
			{ string.deleteCharAt(0); }
		}
		
		//After all is said and done, let's get the hex value of our final number
		String l = Long.toHexString(Long.parseLong(string.toString(), 2));
		String zero = "";
		for (int i=l.length(); i<8; i+=1)
		{ zero += "0"; }
		String hex = new String(zero+Long.toHexString(Long.parseLong(string.toString(), 2)));
		
		//And now let's return it
		return hex;
	}
	
	public static int XOR(int a, int b)
	{
		//Exclusive OR: return true if inputs differ, else false
		return Boolean.compare(true, (a==b));
	}
}
