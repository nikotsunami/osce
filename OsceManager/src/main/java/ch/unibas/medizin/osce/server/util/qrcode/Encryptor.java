package ch.unibas.medizin.osce.server.util.qrcode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

public class Encryptor {
	private static final int KEY_STRING_LENGTH = 32;

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
		return Encryptor.hexStringToByteArray(paddedString);
	}

	/**
	 * Decrypts a .crumble file and writes it to the specified path.
	 * @param symmetricKey The symmetric key to use for decryption
	 * @param outputFilePath The path to the target decrypted file
	 * @param inputFilePath The source path of the encrypted file
	 */
	public static ByteArrayOutputStream decryptFile(String symmetricKey, byte[] encryptedBytes) throws Exception {
		
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		Cipher dcipher = getAESCipherWithEncryptionKey(symmetricKey);
		ByteArrayInputStream input = new ByteArrayInputStream(encryptedBytes);
				
		byte[] buffer = new byte[1024];
		int byteCount = input.read(buffer);

		while (byteCount >= 0) {
			byte[] cipherData = dcipher.update(buffer, 0, byteCount); 
			//output.write(cipherData);
			bout.write(cipherData);
			byteCount = input.read(buffer);
		}
		
		return bout;
		
	}
	
	public static ByteArrayOutputStream encryptFile(String symmetricKey, byte[] encryptedBytes) throws Exception {
		
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		Cipher dcipher = getAESCipherWithEncryptionKeyEncryptMode(symmetricKey);
		ByteArrayInputStream input = new ByteArrayInputStream(encryptedBytes);
				
		byte[] buffer = new byte[1024];
		int byteCount = input.read(buffer);

		while (byteCount >= 0) {
			byte[] cipherData = dcipher.update(buffer, 0, byteCount); 
			//output.write(cipherData);
			bout.write(cipherData);
			byteCount = input.read(buffer);
		}
		
		return bout;
		
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
		
		byte[] symmetricKeyAsByteArray = Encryptor.paddedKeyFromString(secString);
		SecretKey skey = new SecretKeySpec(symmetricKeyAsByteArray, "AES");
		byte[] iv = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);

		Cipher dcipher = Cipher.getInstance("AES/CBC/NoPadding");
		dcipher.init(Cipher.DECRYPT_MODE, skey, paramSpec);
		return dcipher;		
	}
	
	private static Cipher getAESCipherWithEncryptionKeyEncryptMode(String secString)
			throws UnsupportedEncodingException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException {
		
		byte[] symmetricKeyAsByteArray = Encryptor.paddedKeyFromString(secString);
		SecretKey skey = new SecretKeySpec(symmetricKeyAsByteArray, "AES");
		byte[] iv = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);

		Cipher dcipher = Cipher.getInstance("AES/CBC/NoPadding");
		dcipher.init(Cipher.ENCRYPT_MODE, skey, paramSpec);
		return dcipher;		
	}
}