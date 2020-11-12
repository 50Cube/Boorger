package pl.lodz.p.it.boorger.utils;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.translate.AmazonTranslate;
import com.amazonaws.services.translate.AmazonTranslateClient;
import com.amazonaws.services.translate.model.TranslateTextRequest;
import com.amazonaws.services.translate.model.TranslateTextResult;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Log
@AllArgsConstructor
@Transactional(propagation = Propagation.NEVER)

public class TranslationService {

    public static String translate(String text, String sourceLanguage, String targetLanguage) {
        try {
            log.info("AWS Translate called");
            AWSCredentialsProvider awsCredentialsProvider = DefaultAWSCredentialsProviderChain.getInstance();
            AmazonTranslate amazonTranslate = AmazonTranslateClient.builder()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentialsProvider.getCredentials()))
                    .withRegion("us-east-1")
                    .build();

            TranslateTextRequest request = new TranslateTextRequest()
                    .withText(text)
                    .withSourceLanguageCode(sourceLanguage)
                    .withTargetLanguageCode(targetLanguage);
            TranslateTextResult result = amazonTranslate.translateText(request);

            return result.getTranslatedText();
        } catch (Exception e) {
            log.warning("An exception occurred during connecting to AWS Translate");
        }
        return text;
    }
}
