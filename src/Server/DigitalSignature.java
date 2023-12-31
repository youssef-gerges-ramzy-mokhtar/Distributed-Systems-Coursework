import java.security.*;
import java.io.*;
import javax.crypto.*;
import java.util.*;

class DigitalSignature {
	private String msg;

	public DigitalSignature() {
		this.msg = "Hello That is the Auction Server";
	}

	public void createDigitalSignature() throws Exception {
		try {
			Pair<PrivateKey, PublicKey> keys = generateKeys();
			byte[] digitalSignature = generateDigitalSignature(keys.first);			

			saveDigitalSignature(digitalSignature, keys.second, keys.first);
			saveServerPublicKey(keys.second);
			saveServerMsg();
		} catch (Exception error) {
			throw error;
		}
	}

	private Pair<PrivateKey, PublicKey> generateKeys() throws Exception {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			keyGen.initialize(1024, random);

			KeyPair pair = keyGen.generateKeyPair();
			PrivateKey privateKey = pair.getPrivate();
			PublicKey publicKey = pair.getPublic();

			Pair<PrivateKey, PublicKey> keys = new Pair<PrivateKey, PublicKey>(privateKey, publicKey);
			return keys;
		} catch (Exception error) {
			throw error;
		}
	}

	private byte[] generateDigitalSignature(PrivateKey privateKey) throws Exception {
		try {
			Signature dsa = Signature.getInstance("SHA256withRSA");
			dsa.initSign(privateKey);

			byte[] byteData = msg.getBytes();
			dsa.update(byteData);
			
			byte[] realSignature = dsa.sign(); // This line will hash the byteData using SHA256, and encrypt the hash value using RSA and the Server's Private Key
			return realSignature;
		} catch (Exception error) {
			throw error;
		}
	}

	private void saveDigitalSignature(byte[] digitalSignature, PublicKey pubKey, PrivateKey privKey) throws Exception {
		try {
			FileOutputStream fout = new FileOutputStream("../signature/signature");
			fout.write(digitalSignature);
			fout.close();
		} catch (Exception error) {
			throw error;
		}
	}

	private void saveServerPublicKey(PublicKey publicKey) throws Exception {
		try {
			FileOutputStream fout = new FileOutputStream("../signature/pubKey");
			byte[] pubKey = publicKey.getEncoded();
			fout.write(pubKey);
			fout.close();
		} catch (Exception error) {
			throw error;
		}
	}

	private void saveServerMsg() throws Exception {
		try {
			FileOutputStream fout = new FileOutputStream("../signature/msg");
			byte[] msgData = msg.getBytes();
			fout.write(msgData);
			fout.close();
		} catch (Exception error) {
			throw error;
		}
	}
}