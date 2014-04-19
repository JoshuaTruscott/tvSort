/* tvSort - A Java utility for sorting downloaded TV Shows
 * Only working if file has the standard format
 * e.g. Family.Guy.S01E11.XX.XX.XX.xxx
 * Created by Joshua Truscott (tru_sk0tt)
 * Free to modify, recreate and/or redistribute
 * 18/04/2014 - First project using GitHub
 */

package tvSort;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Main {

	public static void main(String[] args) throws IOException {
		System.out.println("tvSort - Created by tru_sk0tt");
		String[] lines = readFile("config.txt").split("\n"); //Reads configuration
		lines[1] = lines[1].replace("\\","/"); //Makes file name readable for Java
		File downloadLocation = new File(lines[1].substring(21, lines[1].length()-1)); //Gets download location from config file (file origin)
		File destLocation = new File(lines[2].substring(22, lines[2].length()-1)) ; //Gets user's TV Shows folder (file destination)
		int sortCount = 0;
		System.out.println("Download Location: " + downloadLocation + "\nDestination Location: "+ destLocation + "\n");
		if(!downloadLocation.exists() || !destLocation.exists()){
			System.out.println("Incorrect file paths given by config.txt\nMake sure the locations entered exist\nand are accessible by this process.\nTerminating...");
			return;
		}
		File[] listOfFiles = downloadLocation.listFiles(); //Makes an array of the files in download folder
		if(listOfFiles.length == 0){
			System.out.println("No files in download folder.\nTerminating...");
			return;
		}
		String showString; //Initializes string that will be used to match show title with filename 

		for (int i = 0; i < listOfFiles.length; i++) { //Searches folder

			if (listOfFiles[i].isFile()) { //If item is a file
				String label = listOfFiles[i].getName(); //Name of file
				label = label.replaceAll("\\s+","").replace(".","").toLowerCase(); //Gets rid of special characters(!, %, & etc.) and also removes all ".", and makes lower case

				for(int k = 8;k<lines.length;k++){ //Starts from line 8(beginning of Show list)
					showString = lines[k].replaceAll("[^\\w\\s]","").replaceAll(" ","").toLowerCase(); //Gets rid of special characters(!, %, & etc.) and also removes all ".", and makes lower case
					if(label.length()>showString.length())
						if(showString.equals(label.substring(0, showString.length()))){ //If showString from config file matches with string 'label' derived from file name
							int season = Integer.parseInt(label.substring(showString.length()+1,showString.length()+3)); //Converts season number (was String) to integer
							System.out.println("Found episode of: " + lines[k] + ", Season: " + season); //Notifies via console that match has been found
							File newDir = new File(destLocation.getAbsolutePath().replace("\\","/")+"/" + lines[k] + "/" +"Season "+ season + "/" + listOfFiles[i].getName()); 
							//New Directory format: X:\[Users TV Shows folder]\[TV Show Name]\[Season #]
							sortCount += 1;

							if(!newDir.exists()) //If directory the file is being sent to doesn't exist:
								newDir.mkdirs(); //Create it

							Files.move(listOfFiles[i].toPath(), newDir.toPath(), StandardCopyOption.REPLACE_EXISTING); //Movement operation
						}
				}
			}
		}
		System.out.println(sortCount + " shows have been sorted\nTerminating...");
	}
	//
	public static String readFile(String fileName) throws IOException { //Method used to read text file
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) { //Reads line by line
				sb.append(line); //Appends StringBuilder with text from line
				sb.append("\n"); 
				line = br.readLine(); //Goes to next line
			}
			return sb.toString(); //Converts StringBuilder to string and returns it
		} finally {
			br.close();
		}
	}
}
