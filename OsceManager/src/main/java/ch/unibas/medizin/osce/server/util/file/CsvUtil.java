
package ch.unibas.medizin.osce.server.util.file;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import ch.unibas.medizin.osce.domain.Nationality;
import ch.unibas.medizin.osce.domain.StandardizedPatient;
import ch.unibas.medizin.osce.shared.i18n.OsceConstants;

import com.mattbertolini.hermes.Hermes;

/**
 * This class is used to write the csv file from the ResultSet. It will write
 * the all row from the resultset.
 * 
 * @author SPEC-India
 * 
 */
public class CsvUtil extends FileUtil {

	private String separater = "\t";
	private List<String> listOfSelectedColumns;
	private String currentLocale;
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
		
		//initiating osce constants on server side to use it here. Added for OMS-156.
		OsceConstants constants =Hermes.get(OsceConstants.class,currentLocale);
		
		StringBuffer rowData = new StringBuffer();
		int standardizedPatientsSize = standardizedPatients.size();
		//changed for OMS-156.
		if (headerInclude) {
			// header

			rowData.append(constants.name());
			rowData.append(getSeparater());
			rowData.append(constants.preName());
			
			if(listOfSelectedColumns.contains(constants.email())){
				rowData.append(getSeparater());
				rowData.append(constants.email());
			}
			if (listOfSelectedColumns.contains(constants.street())) {
				rowData.append(getSeparater());
				rowData.append(constants.street());
				
			}
			if (listOfSelectedColumns.contains(constants.plz())) {
				
				rowData.append(getSeparater());
				rowData.append(constants.plz());
				
			}
			if (listOfSelectedColumns.contains(constants.city())) {
				rowData.append(getSeparater());
				rowData.append(constants.city());				
				
			}
			
			if (listOfSelectedColumns.contains(constants.country())) {
				rowData.append(getSeparater());
				rowData.append(constants.country());				
				
			}
			if (listOfSelectedColumns.contains(constants.telephone())) {
				
				rowData.append(getSeparater());
				rowData.append(constants.telephone());

			}
			if (listOfSelectedColumns.contains(constants.height())) {
				
				rowData.append(getSeparater());
				rowData.append(constants.height());

			}
			if (listOfSelectedColumns.contains(constants.weight())) {
				
				rowData.append(getSeparater());
				rowData.append(constants.weight());

			}

		}
		if (blankLine)
			rowData.append("\n");

		// row data
		//changed for OMS-156.
		for (int index = 0; index < standardizedPatientsSize; index++) {

			rowData.append(escape(standardizedPatients.get(index).getName()));
			rowData.append(getSeparater());
			rowData.append(escape(standardizedPatients.get(index).getPreName()));
			
			if(listOfSelectedColumns.contains(constants.email())){
				rowData.append(getSeparater());
				rowData.append(escape(standardizedPatients.get(index).getEmail()));
			}
			if (listOfSelectedColumns.contains(constants.street())) {
				rowData.append(getSeparater());
				rowData.append(escape(standardizedPatients.get(index).getStreet()));
				
			}
			if (listOfSelectedColumns.contains(constants.plz())) {
				rowData.append(getSeparater());
				rowData.append(escape(standardizedPatients.get(index).getPostalCode()));
				
			}  
			if (listOfSelectedColumns.contains(constants.city())) {
				rowData.append(getSeparater());
				rowData.append(escape(standardizedPatients.get(index).getCity()));
			}
			if (listOfSelectedColumns.contains(constants.country())) {
				rowData.append(getSeparater());
				Nationality country = standardizedPatients.get(index).getCountry();
				rowData.append(escape(country!=null&&country.getNationality()!=null?country.getNationality():""));
			}
			if (listOfSelectedColumns.contains(constants.telephone())) {
				
				rowData.append(getSeparater());
				rowData.append(escape(standardizedPatients.get(index).getTelephone()));

			}
			if (listOfSelectedColumns.contains(constants.height())) {
				rowData.append(getSeparater());
				rowData.append(escape(String.valueOf(standardizedPatients.get(index).getHeight())));

			}
			if (listOfSelectedColumns.contains(constants.weight())) {
				
				rowData.append(getSeparater());
				rowData.append(escape(String.valueOf(standardizedPatients.get(index).getWeight())));

			}

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

	//Added for OMS-156.
	public void setListOfSelectedColumns(List<String> listOfSelectedColumns) {
		this.listOfSelectedColumns=listOfSelectedColumns;
		
	}

	public void setCurrentLocale(String currentLocale) {
		this.currentLocale=currentLocale;
		
	}

}
