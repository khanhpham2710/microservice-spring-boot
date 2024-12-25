package microservice.chat_service.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        final Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dc441tsnp");
        config.put("api_key", "237729752157616");
        config.put("api_secret", "sq5_yWsCHIJKSoTlR-GSnQTW_Rw");
        return new Cloudinary(config);
    }
}
