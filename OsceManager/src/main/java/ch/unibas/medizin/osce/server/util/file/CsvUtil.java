package ch.unibas.medizin.osce.server.util.file;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import ch.unibas.medizin.osce.domain.StandardizedPatient;

/**
 * This class is used to write the csv file from the ResultSet. It will write
 * the all row from the resultset.
 * 
 * @author SPEC-India
 * 
 */
public class CsvUtil extends FileUtil {

	private String separater = "\t";

	/**
	 * Write the csv format from the result into the file
	 * 
	 * @param resultSet
	 * @param blankLine
	 * @param headerInclude
	 * @throws SQLException
	 * @throws IOException
	 */
	public void writeCsv(List<StandardizedPatient> standardizedPatients,
			boolean blankLine, boolean headerInclude) throws SQLException,
			IOException {

		StringBuffer rowData = new StringBuffer();
		int standardizedPatientsSize = standardizedPatients.size();

		if (headerInclude) {
			// header

			rowData.append("Name");
			rowData.append(getSeparater());
			rowData.append("PreName");
			rowData.append(getSeparater());
			rowData.append("Email");
		}
		if (blankLine)
			rowData.append("\n");

		// row data

		for (int index = 0; index < standardizedPatientsSize; index++) {

			rowData.append(escape(standardizedPatients.get(index).getName()));
			rowData.append(getSeparater());
			rowData.append(escape(standardizedPatients.get(index).getPreName()));
			rowData.append(getSeparater());
			rowData.append(escape(standardizedPatients.get(index).getEmail()));

			if (index + 1 < standardizedPatientsSize) {
				rowData.append(getSeparater());
			}
			if (blankLine) {
				rowData.append("\n");
			}
		}
		// if (blankLine)
		// rowData.append("\n");

		write(rowData.toString());
	}

	/**
	 * remove the escape from the string before write to the file
	 * 
	 * @param resultString
	 * @return
	 */
	private String escape(String resultString) {
		if (resultString == null)
			return "";
		resultString = resultString.replaceAll("\"", "\"\"");
		resultString = resultString.replaceAll("\n\r", "");
		resultString = resultString.replaceAll("\n", "");
		resultString = resultString.replaceAll("\r", "");
		resultString = resultString.replaceAll("\t", "");
		resultString = "\"" + resultString + "\"";
		return resultString;
	}

	/**
	 * @return the separater
	 */
	public String getSeparater() {
		return separater;
	}

	/**
	 * @param separater
	 *            the separater to set
	 */
	public void setSeparater(String separater) {
		this.separater = separater;
	}

}
