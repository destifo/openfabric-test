package ai.openfabric.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;

@Configuration
public class DockerConfig {
    
    @Bean
    public DockerClient dockerClient() {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerHost("tcp://localhost:2375").withDockerTlsVerify(false).build();

        // DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
        // .dockerHost(config.getDockerHost())
        // .sslConfig(config.getSSLConfig())
        // .build();

        DockerClient client = DockerClientBuilder.getInstance(config).build();
        return client;
    }

}
