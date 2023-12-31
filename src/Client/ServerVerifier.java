import java.security.*;
import java.security.spec.*;
import java.io.*;
import javax.crypto.*;
import java.util.*;

// nice link: https://static.javatpoint.com/core/images/java-digital-signature2.png
class ServerVerifier {
	private byte[] signatureData;
	private byte[] msgData;
	private PublicKey pubKey;

	public ServerVerifier() throws Exception {
		try {
			signatureData = readFile("../signature/signature");
			msgData = readFile("../signature/msg");
			pubKey = readPublicKey();
		} catch (Exception error) {
			throw error;
		}
	}

	public void verifyServerSignature() throws Exception {
		Signature sig = Signature.getInstance("SHA256withRSA");
		sig.initVerify(pubKey);
		sig.update(msgData);

		if (!sig.verify(signatureData))
			throw new Exception("Server Authentication Failed");

		System.out.println("Server Identity Authenticated");
	}

	public void displayHashValues() throws Exception {
		try {
			// Convert Server Msg to a Digest using SHA-256 Hashing Algorithm
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] digest1 = md.digest(msgData);
			
			// Decrypting Digital Signature to produce Original Digest
			// Here we needed to just take the last 32 bytest from the decryptedSignature as RSA includes Padding, and we know that SHA-256 uses only 256bits = 32bytes
			Cipher rsa = Cipher.getInstance("RSA");
			rsa.init(Cipher.DECRYPT_MODE, pubKey);
			byte[] decryptedSignature = rsa.doFinal(signatureData);
			byte[] digest2 = Arrays.copyOfRange(decryptedSignature, decryptedSignature.length - 32, decryptedSignature.length);

			System.out.println("Digest 1: " + Arrays.toString(digest1));
			System.out.println("Digest 2: " + Arrays.toString(digest2));
			System.out.println();
		} catch (Exception error) {
			throw error;
		}
	}

	private byte[] readFile(String path) throws Exception {
		try {
			FileInputStream fin = new FileInputStream(path);
			byte[] data = new byte[fin.available()];
			fin.read(data);
			fin.close();

			return data;
		} catch (Exception error) {
			throw error;
		}
	}

	private PublicKey readPublicKey() throws Exception {
		try {
			FileInputStream fin = new FileInputStream("../signature/pubKey");
			byte[] key = new byte[fin.available()];
			fin.read(key);
			fin.close();

			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey pubKey = keyFactory.generatePublic(keySpec);

			return pubKey;
		} catch (Exception error) {
			throw error;
		}
	}

	public static void main(String[] args) {
		try {
			ServerVerifier verifier = new ServerVerifier();
			verifier.verifyServerSignature();
			verifier.displayHashValues();
		} catch (Exception e) {
			System.out.println(e.getMessage());	
		}
	}
}