package ddc.support.pki;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class PkiCrypto {

	private static final String ALGORITHM = "RSA";

	public static byte[] encrypt(byte[] publicKey, byte[] inputData) throws Exception {

		PublicKey key = KeyFactory.getInstance(ALGORITHM).generatePublic(new X509EncodedKeySpec(publicKey));

		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key);

		byte[] encryptedBytes = cipher.doFinal(inputData);

		return encryptedBytes;
	}

	public static byte[] decrypt(byte[] privateKey, byte[] inputData) throws Exception {

		PrivateKey key = KeyFactory.getInstance(ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(privateKey));

		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key);

		byte[] decryptedBytes = cipher.doFinal(inputData);

		return decryptedBytes;
	}

	public static void main(String[] args) throws Exception {
		PkiGenerateKeys keys = new PkiGenerateKeys();
		keys.createKeys();
		byte[] publicKey = keys.getPublicKey().getEncoded();
		byte[] privateKey = keys.getPrivateKey().getEncoded();

		byte[] encryptedData = encrypt(publicKey, "hi this is Visruth here".getBytes());

		byte[] decryptedData = decrypt(privateKey, encryptedData);

		System.out.println(new String(decryptedData));

	}

}
