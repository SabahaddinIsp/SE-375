import java.util.*;
import java.net.*;
import java.io.*;
import java.nio.charset.*;
import java.security.*;
import javax.crypto.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.spec.*;
public class Client{

    public static void main(String[] args) throws Exception{

        String ulcase;

        String times;

        String color;

        String cipherUrl;

        String urlData="";

        String modifiedSentence;

        Cipher cipher;

        byte[] decryptedText;

        byte[] newDecryptedText;

        byte[] encryptedText;

        MessageDigest md=MessageDigest.getInstance("SHA-256");

        BufferedReader inFromUser=new BufferedReader(new InputStreamReader(System.in));

        Socket clientSocket=new Socket("localhost",6789);

        DataOutputStream outToServer=new DataOutputStream(clientSocket.getOutputStream());

        DataInputStream inFromServer=new DataInputStream(clientSocket.getInputStream());

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

        keyPairGenerator.initialize(1024);

        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PrivateKey privateKey = keyPair.getPrivate();

        PublicKey publicKey = keyPair.getPublic();

        Signature signature = Signature.getInstance("SHA1withRSA");

        signature.initSign(privateKey);

        byte[] encodedPublic = publicKey.getEncoded();

        String toSendPublicKey = Base64.getEncoder().encodeToString(encodedPublic);

        outToServer.writeUTF(toSendPublicKey);

        System.out.println("Public key has sent from Client...");

        String retrievedPublic = inFromServer.readUTF();

        byte[] spec = Base64.getDecoder().decode(retrievedPublic);

        X509EncodedKeySpec decodedServerPublic = new X509EncodedKeySpec(spec);

        KeyFactory kf = KeyFactory.getInstance("RSA");

        PublicKey serVerPublicKey = kf.generatePublic(decodedServerPublic);

        signature.update(encodedPublic);

        byte[] publicSig = signature.sign();

        String toSendSig = Base64.getEncoder().encodeToString(publicSig);

        outToServer.writeUTF(toSendSig);

        String retrievedPublicSign = inFromServer.readUTF();

        byte[] retrievedSing = Base64.getDecoder().decode(retrievedPublicSign);

        signature.initVerify(serVerPublicKey);

        signature.update(spec);

        boolean verified = signature.verify(retrievedSing);

        System.out.println(verified ? "Legit": "Forged");

        cipher=Cipher.getInstance("RSA");

        cipher.init(Cipher.DECRYPT_MODE,privateKey);

        cipherUrl=inFromServer.readUTF();

        decryptedText=Base64.getDecoder().decode(cipherUrl);

        newDecryptedText=cipher.doFinal(decryptedText);

        String U=new String(newDecryptedText);

        System.out.printf("Received String URL: %s \n",U);

        BufferedInputStream in=new BufferedInputStream(new URL(new String(newDecryptedText)).openStream());

        for(int i=in.available(); i>0; i--)

            urlData+=(char)in.read();

        in.close();

        cipher.init(Cipher.ENCRYPT_MODE,serVerPublicKey);

        encryptedText = cipher.doFinal(urlData.getBytes(StandardCharsets.UTF_8));

        System.out.println("Sending Encrypted Data");

        outToServer.writeUTF(Base64.getEncoder().encodeToString(encryptedText));

    }
}