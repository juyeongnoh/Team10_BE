package bdbe.bdbd._core.errors.utils;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;

@Configuration
public class AWSConfig {

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${proxy.host:}")
    private String proxyHost;

    @Value("${proxy.port:0}")
    private int proxyPort;


    @Bean
    @Profile("!prod")
    public AmazonS3 amazonS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }

    @Bean
    @Profile("prod")
    public AmazonS3 amazonS3ClientProd() {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        if (!StringUtils.isEmpty(proxyHost) && proxyPort > 0) {
            clientConfiguration.setProxyHost(proxyHost);
            clientConfiguration.setProxyPort(proxyPort);
        }
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withClientConfiguration(clientConfiguration)
                .withRegion(region)
                .build();
    }
}
