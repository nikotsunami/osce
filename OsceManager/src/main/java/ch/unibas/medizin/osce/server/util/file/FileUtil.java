package ch.unibas.medizin.osce.server.util.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class is used to write file.
 * 
 * @author SPEC-India
 */
public class FileUtil {

	private BufferedWriter out;

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
}
