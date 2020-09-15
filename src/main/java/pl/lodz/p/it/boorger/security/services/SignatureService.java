package pl.lodz.p.it.boorger.security.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.util.UUID;

@Log
@Component
public class SignatureService {

    private static String keystorePassword;
    private static String keyPassword;

    @Autowired
    public SignatureService(@Value("${boorger.keystorePassword}") String keystorePassword, @Value("${boorger.keyPassword}") String keyPassword) {
        SignatureService.keystorePassword = keystorePassword;
        SignatureService.keyPassword = keyPassword;
    }

    private static KeyPair getKeyPairFromKeystore() throws Exception {
        InputStream inputStream = SignatureService.class.getResourceAsStream("/keystore.jks");
        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        keyStore.load(inputStream, keystorePassword.toCharArray());
        KeyStore.PasswordProtection keyPasswordProtection = new KeyStore.PasswordProtection(keyPassword.toCharArray());
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry("boorger", keyPasswordProtection);
        Certificate certificate = keyStore.getCertificate("boorger");
        PublicKey publicKey = certificate.getPublicKey();
        PrivateKey privateKey = privateKeyEntry.getPrivateKey();
        return new KeyPair(publicKey, privateKey);
    }

    public static String createSignature(String string) {
        try {
            KeyPair keyPair = getKeyPairFromKeystore();
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(keyPair.getPrivate());
            signature.update(string.getBytes());
            return Base64Utils.encodeToString(signature.sign());
        } catch (Exception e) {
            log.warning("An error occurred during creating signature");
        }
        return UUID.randomUUID().toString();
    }

    public static boolean verify(String signature, String signatureDTO) {
        try {
            KeyPair keyPair = getKeyPairFromKeystore();
            Signature publicSignature = Signature.getInstance("SHA256withRSA");
            publicSignature.initVerify(keyPair.getPublic());
            publicSignature.update(signature.getBytes());
            return publicSignature.verify(Base64Utils.decode(signatureDTO.getBytes()));
        } catch (Exception e) {
            log.warning("An error occurred during verifying signature");
        }
        return false;
    }
}
