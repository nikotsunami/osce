package ch.unibas.medizin.osce.server;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import com.dd.plist.NSData;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSObject;
import com.dd.plist.PropertyListParser;

public class S3Decryptor {
	private static final Logger log = Logger.getLogger(S3Decryptor.class);
	private static final String EXAM_DATA_PLIST_KEY = "body";
	private static final int KEY_STRING_LENGTH = 32;

	public static String decrypt(String symmetricKey, String fileName, String filePath) {
		
		if (FilenameUtils.getExtension(fileName).equalsIgnoreCase("crumble"))
		{
			String outputPath = filePath + FilenameUtils.getBaseName(fileName) + "_dec.oscexam";
			File outputFilePath = new File(outputPath);
			File inputFilePath = new File((filePath + fileName));
			
			try {
				FileUtils.touch(outputFilePath);
			} catch (IOException e) {
				log.error("Error in creating output file");
				return "";
			}
			
			boolean flag = decryptFile(symmetricKey, outputFilePath, inputFilePath);
			
			if (flag == true)
				return outputPath;
			else
				return "";
		}
		else
		{
			return "";
		}		
	}

	/**
	 * Converts a string in hexadecimal format to a byte array
	 * @param hexString The string to be converted
	 * @return A byte array containing the extracted hex bytes
	 */
	private static byte[] hexStringToByteArray(String hexString) {
		int len = hexString.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
		}
		return data;
	}

	/**
	 * Transforms a string into its hexadecimal representation and adds padding.
	 * @param symmetricKeyAsString The symmetric key in plain text format
	 * @return A byte array containing the padded decryption key
	 */
	private static byte[] paddedKeyFromString(String symmetricKeyAsString) throws UnsupportedEncodingException {
		String encodedHexString = Hex.encodeHexString(new String(symmetricKeyAsString).getBytes());
		String paddedString = String.format("%1$-" + KEY_STRING_LENGTH + "s", encodedHexString).replace(' ', '0');
		return S3Decryptor.hexStringToByteArray(paddedString);
	}

	/**
	 * Decrypts a .crumble file and writes it to the specified path.
	 * @param symmetricKey The symmetric key to use for decryption
	 * @param outputFilePath The path to the target decrypted file
	 * @param inputFilePath The source path of the encrypted file
	 */
	private static boolean decryptFile(String symmetricKey, File outputFilePath,
			File inputFilePath) {
		try {
			byte[] encryptedBytes = bytesFromCrumbleFile(inputFilePath);
			Cipher dcipher = getAESCipherWithEncryptionKey(symmetricKey);

			ByteArrayInputStream input = new ByteArrayInputStream(encryptedBytes);
			FileOutputStream output = new FileOutputStream(outputFilePath);
			byte[] buffer = new byte[1024];
			int byteCount = input.read(buffer);

			while (byteCount >= 0) {
				output.write(dcipher.update(buffer, 0, byteCount));
				byteCount = input.read(buffer);
			}
			output.write(dcipher.doFinal());
			output.flush();
			output.close();
			return true;
		} catch (Exception ex) {
			log.error("Error in decryptfile", ex);			
			return false;
		}
	}

	/**
	 * Extracts the data for the SQLite file from the property list
	 * @param file The source .crumble file to extract data from
	 * @return A byte array of encrypted data
	 */
	private static byte[] bytesFromCrumbleFile(File file) throws Exception {
		NSDictionary rootDict = (NSDictionary) PropertyListParser.parse(file);
		NSObject body = rootDict.objectForKey(EXAM_DATA_PLIST_KEY);
		NSData bodyData = (NSData) body;
		return bodyData.bytes();
	}

	/**
	 * Configures a Cipher instance that can be used to perform a AES decryption.
	 * @param secString The encryption key in plain text format
	 * @return A Cipher instance that can be used for decryption
	 */
	private static Cipher getAESCipherWithEncryptionKey(String secString)
			throws UnsupportedEncodingException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException {
		byte[] symmetricKeyAsByteArray = S3Decryptor.paddedKeyFromString(secString);
		SecretKey skey = new SecretKeySpec(symmetricKeyAsByteArray, "AES");
		byte[] iv = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);

		Cipher dcipher = Cipher.getInstance("AES/CBC/NoPadding");
		dcipher.init(Cipher.DECRYPT_MODE, skey, paramSpec);
		return dcipher;
	}
}