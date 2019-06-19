package ddc.support.pki;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.apache.commons.net.util.Base64;

public class PkiGenerateKeys {
	private static final int KEYLENGTH_DEFAULT=1024;
	private static final String ALGORITHM = "RSA";
	
	private KeyPairGenerator keyGen;
	private KeyPair pair;
	private PrivateKey privateKey;
	private PublicKey publicKey;

	public PkiGenerateKeys() throws NoSuchAlgorithmException, NoSuchProviderException {
		this.keyGen = KeyPairGenerator.getInstance(ALGORITHM);
		this.keyGen.initialize(KEYLENGTH_DEFAULT);
	}
	
	public PkiGenerateKeys(int keylength) throws NoSuchAlgorithmException, NoSuchProviderException {
		this.keyGen = KeyPairGenerator.getInstance(ALGORITHM);
		this.keyGen.initialize(keylength);
	}

	public void createKeys() {
		this.pair = this.keyGen.generateKeyPair();
		this.privateKey = pair.getPrivate();
		this.publicKey = pair.getPublic();
	}

	public PrivateKey getPrivateKey() {
		return this.privateKey;
	}

	public PublicKey getPublicKey() {
		return this.publicKey;
	}

	public void writeToFile(String path, byte[] key) throws IOException {
		File f = new File(path);
		f.getParentFile().mkdirs();

		FileOutputStream fos = new FileOutputStream(f);
		fos.write(key);
		fos.flush();
		fos.close();
	}

	public static void main(String[] args) {
		PkiGenerateKeys gk;
		try {
			gk = new PkiGenerateKeys(1024);
			gk.createKeys();
			gk.writeToFile("KeyPair/publicKey", gk.getPublicKey().getEncoded());
			gk.writeToFile("KeyPair/privateKey", gk.getPrivateKey().getEncoded());
			System.out.println("public base64:\n"+  Base64.encodeBase64String(gk.getPublicKey().getEncoded()));
			System.out.println("private base64:\n"+  Base64.encodeBase64String(gk.getPrivateKey().getEncoded()));
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

	}

}
