package ch.unibas.medizin.osce.server.util.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ch.unibas.medizin.osce.server.OsMaFilePathConstant;



/**
 * This class is used to write file.
 * 
 * @author SPEC-India
 */
public class FileUtil {

	private BufferedWriter out;

        	//SPEC[ Static Path needs to change when run on another PC
	//private static String imagelocalPath="d://sp//images//";
	//private static String imageappPath="osMaEntry\\gwt\\unibas\\sp\\images\\";
	//private static String videolocalPath="d://sp//videos//";
	//private static String videoappPath="osMaEntry\\gwt\\unibas\\sp\\videos\\";
	//SPEC]

	/**
	 * Open the specified file.
	 * 
	 * @param fullFileName
	 * @param appendMode
	 * @throws IOException
	 */
	public void open(String fullFileName, boolean appendMode)
			throws IOException {
		out = new BufferedWriter(new FileWriter(new File(fullFileName),
				appendMode));
	}

	/**
	 * write string into the file.
	 * 
	 * @param content
	 * @throws IOException
	 */
	public void write(String content) throws IOException {
		out.write(content);
	}

	/**
	 * close the file.
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		out.close();
	}
	
	public static boolean copyImageFile(String imagePath)
	{
		String name=getName(imagePath);
		
		return copyFile(OsMaFilePathConstant.localImageUploadDirectory+name,OsMaFilePathConstant.realImagePath,name);
	}
	public static boolean copyvideoFile(String videoPath)
	{
		String name=getName(videoPath);
		
		return copyFile(OsMaFilePathConstant.localVideoUploadDirectory+name,OsMaFilePathConstant.realVideoPath,name);
	}
	public static String  getName(String path)
	{
		String s[]=path.split("/");
		return s[s.length-1];
	}
	
	  private static boolean copyFile(String srFile, String dtFile,String fileName){
		  try{
		
		  File f1 = new File(srFile);
		  File f2 = new File(dtFile,fileName);
		  
		  if(!f2.createNewFile())
		  {
			  return false;
		  }
		  InputStream in = new FileInputStream(f1);
		  
		  //For Append the file.
		//  OutputStream out = new FileOutputStream(f2,true);

		  //For Overwrite the file.
		  OutputStream out = new FileOutputStream(f2);

		  byte[] buf = new byte[1024];
		  int len;
		  while ((len = in.read(buf)) > 0){
		  out.write(buf, 0, len);
		  }
		  in.close();
		  out.close();
		  System.out.println("File copied.");
		  return true;
		  }
		  catch(FileNotFoundException ex){
		  System.out.println(ex.getMessage() + " in the specified directory.");
		  return false;
		  
		  }
		  catch(IOException e){
		  System.out.println(e.getMessage());  
		  return false;
		  }
		  }
}
