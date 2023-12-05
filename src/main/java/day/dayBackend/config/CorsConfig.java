package day.dayBackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private final String serverUrl;
    private final String clientUrl;

    public CorsConfig(
            @Value("${deploy.client.url}") String serverUrl,
            @Value("${deploy.server.url}") String clientUrl) {
        this.serverUrl = serverUrl;
        this.clientUrl = clientUrl;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3000);

    }

}
