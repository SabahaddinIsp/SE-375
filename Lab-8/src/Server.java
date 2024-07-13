import java.util.*;
import java.net.*;
import java.io.*;
import java.nio.charset.*;
import java.security.*;
import javax.crypto.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
public class Server{

    public static void main(String[] args) throws Exception{

        String clientSentence;

        String dataL;

        Cipher cipher;

        byte[] encryptedText;

        byte[] decryptedText;

        byte[] newDecryptedText;

        String url="https://homes.izmirekonomi.edu.tr/eokur/sample0.txt";

        MessageDigest md=MessageDigest.getInstance("SHA-256");

        ServerSocket welcomeSocket=new ServerSocket(6789);

        Socket connectionSocket=welcomeSocket.accept();

        DataInputStream inFromClient=new DataInputStream(connectionSocket.getInputStream());

        DataOutputStream outToClient=new DataOutputStream(connectionSocket.getOutputStream());

        System.out.println("Waiting for connection...");

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

        keyPairGenerator.initialize(1024);

        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PrivateKey privateKey = keyPair.getPrivate();

        PublicKey publicKey = keyPair.getPublic();

        Signature signature = Signature.getInstance("SHA1withRSA");

        signature.initSign(privateKey);

        byte[] encodedPublic=publicKey.getEncoded();

        String toSendPublic =Base64.getEncoder().encodeToString(encodedPublic);

        outToClient.writeUTF(toSendPublic);

        System.out.println("Public key has sent from Server...");

        String retrievedPublic = inFromClient.readUTF();

        byte[] spec = Base64.getDecoder().decode(retrievedPublic);

        X509EncodedKeySpec decodedServerPublic = new X509EncodedKeySpec(spec);

        KeyFactory kf = KeyFactory.getInstance("RSA");

        PublicKey clientPublicKey = kf.generatePublic(decodedServerPublic);

        signature.update(encodedPublic);

        byte[] publicSig = signature.sign();

        String toSendSig = Base64.getEncoder().encodeToString(publicSig);

        outToClient.writeUTF(toSendSig);

        String receivedSign = inFromClient.readUTF();

        byte[] RetSig = Base64.getDecoder().decode(receivedSign);

        signature.initVerify(clientPublicKey);

        signature.update(spec);

        boolean verified = signature.verify(RetSig);

        System.out.println(verified ? "Legit": "Forged");

        cipher=Cipher.getInstance("RSA");

        cipher.init(Cipher.ENCRYPT_MODE,clientPublicKey);

        encryptedText=cipher.doFinal(url.getBytes(StandardCharsets.UTF_8));

        System.out.println("Sending the URL...");

        outToClient.writeUTF(Base64.getEncoder().encodeToString(encryptedText));

        clientSentence = inFromClient.readUTF();

        cipher.init(Cipher.DECRYPT_MODE,privateKey);

        decryptedText = Base64.getDecoder().decode(clientSentence);

        newDecryptedText = cipher.doFinal(decryptedText);

        String U = new String(newDecryptedText);

        System.out.println(U);

    }
}