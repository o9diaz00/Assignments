import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class hillcipher
{
	public static void main(String[] args) throws IOException
	{
		/*
		 * Since this is compiled from the command line/prompt, we first want to check to see
		 * if the requirements of 2 arguments were fulfilled.  If not, tell the user that he/she
		 * has to provide 2 arguments: args[0] = encryption key | args[1] = plaintext
		 */
		if (args.length < 2)
		{
			System.out.println("You need to input two file arguments in order for this to run");
			System.out.println("For example: [java hillcipher key.txt plaintext.txt]");
		}
		else
		{
			/*
			 * First create our variables to hold text; use a StringBuilder because
			 * it is very easy to manipulate without having to create anew (unlike a string)
			 */
			StringBuilder tempStr = new StringBuilder();
			StringBuilder tempKey = new StringBuilder();
		
			/*
			 * Now read the files and set the above values accordingly
			*/
			ReadFile(new File(args[0]), tempKey);
			ReadFile(new File(args[1]), tempStr);
		
			/*
			 * This StringBuilder will be the product of our encryption
			*/
			StringBuilder ciphertext = new StringBuilder();
		
			/*
			 * Since the first char in the key file tells us the size of our matrix
			 * We can go ahead and create that 
			*/
			int matrixSize = Integer.parseInt(tempKey.substring(0,1));
			String key[][] = new String[matrixSize][matrixSize];
		
			/*
			 * Now since we already have the matrix size
			 * let's delete that first line in the string builder to make things easier
			*/
			tempKey.delete(0, 2);
				
			/*
			 * RemoveChars(StringBuilder str)
			 * ----------------------------------------------------------------------------
			 * Now we have to remove all the special characters (space, punctuation, etc.)
			*/ 
			RemoveChars(tempStr);
		
			/*
			 * KeyToArray(StringBuilder keyString, int size)
			 * ------------------------------------------------------------------
			 * We have to separate our key into a two dimensional array
			 * With dimensions equal to that of the matrixSize
			*/
			key = KeyToArray(tempKey, matrixSize);
		
			/*
			 * StringToNumber(String str[][]) throws NullPointerException
			 * --------------------------------------------------------------------
			 * If our key contains letters, the code will not compile, so we'll 
			 * handle that now (in a crappy way, but who cares)
			 * also, what if professor decides to throw in a non-square matrix to try and crash program
			 * our code is designed to form square matrices, therefore there'd be a null somewhere within
			 * the matrix, catch it, and exit program instead of crashing
			 * (this is unnecessary, the professor responded that we will always have square matrix keys
			 *  that consists of only numbers)
			*/
			key = StringToNumber(key);
		
			/*
			 * PadChars(StringBuilder str, int size)
			 * -------------------------------------------------------------
			 * We want to check and see if we have to 'pad' our string or not
			 * If its length modded by the matrix size does not return 0, pad it
			 * The rules say to first pad with a 'x' and then with a random char
			*/ 
			PadChars(tempStr, matrixSize);
				
			/*
			 * Next, we have to turn the plaintext into an array so we can use
			 * matrix multiplication (also, we are treating 'A' as 'a' so we 
			 * can go ahead and set all the chars to their lowercase counterparts
			*/
			char textArray[] = tempStr.toString().toLowerCase().toCharArray();
		
			/*
			 * MultiplyMatrix(char plaintext[], String k[][], StringBuilder cipher)
			 * ----------------------------------------------------------------
			 * Now we have the necessities to do the matrix multiplication
			 * Java's treats 'a' as 97 whereas we're treating 'a' as 0
			 * Therefore we have to subtract 97 from the values obtained 
			 * when converting the plain text chars to a number and then add 97 back
			 * when converting the numbers to plain text chars
			*/
			ciphertext = MultiplyMatrix(textArray, key, ciphertext);
		
			/*
			 * We're just going to format the output to 80 chars per line right here
			 * Loop through the entire length and insert a new line char after every 80th (index 81) interval
			*/
			StringBuilder formattext = new StringBuilder();
			for (int i=0; i<textArray.length; i+=1)
			{
				formattext.append(textArray[i]);
				if (i%81 == 0)
				{ formattext.insert(i, "\n"); }
			}
			for (int i=0; i<ciphertext.length(); i+=1)
			{
				if (i%81 == 0)
				{ ciphertext.insert(i, "\n"); }
			}
			/*
			 * Every unnecessary variable above can be 'thrown away' now
			 * (pretty sure this isn't necessary, but who cares)
			*/
			tempStr = null;
			key = null;
			
			/*
			 * We're done, so output everything to the screen
			*/
			System.out.println("\n[KEY MATRIX]\n\n"+tempKey);
			System.out.println("\n[PLAIN TEXT]\n"+formattext);
			System.out.println("\n\n[CIPHER TEXT]\n"+ciphertext);
			System.out.println();
		}
	}
	
	public static void ReadFile(File f, StringBuilder output) throws IOException
	{
		/*
		 * Pretty standard coding for reading a file, line by line and storing its contents
		*/
		try
		{
			FileInputStream stream = new FileInputStream(f);
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			String str = null;
			while ((str = reader.readLine()) != null)
			{ output.append(str+"\n"); }
			reader.close();
		}
		catch(Exception e)
		{
			System.out.println("Sorry, seems like you have input an invalid file/file path");
			System.exit(1);
		}
	}
	
	public static String[][] StringToNumber(String str[][]) throws NullPointerException
	{
		/*
		 * Well, arithmetic was failing me so I just decided to loop through
		 * all indices of the key, checking to see if a letter was found
		 * if it was, replace said letter with its number equivalent
		 */
		for (int i=0; i<str.length; i+=1)
		{
			for (int ii=0; ii<str.length; ii+=1)
			{
				try
				{
					/* Edit
					 * This is untested, but I should be able to replace this with:
					 * str[i][ii] = String.parseString((int)(str[i][ii]));
					 *
					*/
					switch (str[i][ii])
					{
					case "a":
						str[i][ii] = "0";
						break;
					case "b":
						str[i][ii] = "1";
						break;
					case "c":
						str[i][ii] = "2";
						break;
					case "d":
						str[i][ii] = "3";
						break;
					case "e":
						str[i][ii] = "4";
						break;
					case "f":
						str[i][ii] = "5";
						break;
					case "g":
						str[i][ii] = "6";
						break;
					case "h":
						str[i][ii] = "7";
						break;
					case "i":
						str[i][ii] = "8";
						break;
					case "j":
						str[i][ii] = "9";
						break;
					case "k":
						str[i][ii] = "10";
						break;
					case "l":
						str[i][ii] = "11";
						break;
					case "m":
						str[i][ii] = "12";
						break;
					case "n":
						str[i][ii] = "13";
						break;
					case "o":
						str[i][ii] = "14";
						break;
					case "p":
						str[i][ii] = "15";
						break;
					case "q":
						str[i][ii] = "16";
						break;
					case "r":
						str[i][ii] = "17";
						break;
					case "s":
						str[i][ii] = "18";
						break;
					case "t":
						str[i][ii] = "19";
						break;
					case "u":
						str[i][ii] = "20";
						break;
					case "v":
						str[i][ii] = "21";
						break;
					case "w":
						str[i][ii] = "22";
						break;
					case "x":
						str[i][ii] = "23";
						break;
					case "y":
						str[i][ii] = "24";
						break;
					case "z":
						str[i][ii] = "25";
						break;
					}
				}
				catch(Exception e)
				{
					System.out.println("Sorry, you do not have a square matrix and this will not work, exiting program...");
					System.exit(1);
					break;
				}
			}
		}
		return str;
	}
	
	public static void PadChars(StringBuilder str, int size)
	{
		while (str.length() % size != 0)
		{
			str.append('x');
			for (int i=0; i<str.length()%size; i+=1)
			{
				int padding = (int)(Math.floor(Math.random()*25)+97);
				/*
				 * The random generator above will return a-y (we pad with 'z' in class but he wants 'x' here)
				 * therefore, we'll check to see if we get 'x', if we do, replace it with 'z'
				*/
				if (padding == 120)
				{ padding = 122; }
				str.append((char)(padding));
			}
		}
	}
	
	public static void RemoveChars(StringBuilder str)
	{
		/*
		 * We use a for-loop and store every char that we DON'T want into an array
		 * The reason I chose not to remove them automatically is because
		 * the charAt(i) offset will change unwarranted and won't be accurate
		 * Therefore, I store it first, and then once I accumulate all of which I do not want
		 * I get rid of them (it's much faster than my earlier method of nesting another for-loop) 
		 */
		char remove[] = new char[str.length()];
		for (int i=0; i<str.length(); i+=1)
		{
			if (str.toString().toLowerCase().charAt(i) < 'a' || str.toString().toLowerCase().charAt(i) > 'z')
			{ remove[i] = str.charAt(i); }
		}
		for (int i=0; i<remove.length; i+=1)
		{
			if (remove[i] != '\0')
			{ str.deleteCharAt(str.toString().indexOf(remove[i])); }
		}
		
		remove = null;
	}
	
	public static String[][] KeyToArray(StringBuilder keyString, int size)
	{
		String s[] = keyString.toString().toLowerCase().split("[ \n]"); //This let's us handle the space and newline chars
		String k[][] = new String[size][size];
		
		int n = -1; //This will be used to set the specific 'row' of array
		for (int i=0; i<s.length; i+=1)
		{
			/*
			 * We know that there are a certain number of chars per row
			 * once we exceed said number (n mod x == 0), then we're on
			 * a new line, or in other words, a new 'row'
			 * we start n at -1, because how I coded it; it technically starts at 0 (n+1 occurs before any storing)
			*/
			if (i%size == 0)
			{ n += 1; }
			k[n][i%size] = s[i];
		}
		
		s = null;
		return k;
	}
	
	public static StringBuilder MultiplyMatrix(char plaintext[], String k[][], StringBuilder cipher)
	{
		int product[][] = new int[plaintext.length][k.length]; //This is the product after multiplication
		int size = (int)(Math.floor(plaintext.length/k.length)); //This gets us the 1st dimension of the following array since we already know the length of the 2nd dimension (k.length)
		char split[][] = new char[size][k.length]; //This array divides the whole plaintext into groups of k.length for easier manipulation
		cipher = new StringBuilder(); //This is our encrypted text output
		
		/*
		 * Given the length l of our key, we must divide our plaintext into groups of l
		 * For example, if our key is a 4x4 matrix (l = 4), then we have to divide our plaintext
		 * into groups of 4's
		 */
		int n = -1;
		for (int i=0; i<plaintext.length; i+=1)
		{
			if (i%k.length == 0)
			{
				n+=1;
				split[n] = String.copyValueOf(plaintext, i, k.length).toCharArray();
			}
		}
		
		/*
		 * This right here is the actual matrix multiplication
		 */
		for (int i=0; i<size; i+=1)
		{
			for (int j=0; j<k.length; j+=1)
			{
				for (int jj=0; jj<k.length; jj+=1)
				{ product[i][j] += (split[i][jj]-97)*(Integer.parseInt(String.valueOf(k[j][jj]))); }
			}
		}
		
		/*
		 * Here is just a little clean up:
		 * the value has to be modded by 26 (it wasn't done above because some errors occurs)
		 * Also, at this point we can convert our numbers back to chars and append it to our StringBuilder
		 */
		for (int i=0; i<size; i+=1)
		{
			for (int ii=0; ii<k.length; ii+=1)
			{
				product[i][ii] %= 26;
				cipher.append((char)(product[i][ii]+97));
			}
		}
		
		product = null;
		split = null;
		return cipher;
	}
}
