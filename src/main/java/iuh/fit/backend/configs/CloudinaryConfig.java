/*
 * @ (#) CloudinaryConfig.java        1.0     11/29/2024
 *
 * Copyright (c) 2024 IUH. All rights reserved.
 */

package iuh.fit.backend.configs;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/*
 * @description
 * @author: Ho Vinh Duy
 * @date:   11/29/2024
 */
@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dfqpclmbc");
        config.put("api_key", "511428582334539");
        config.put("api_secret", "Ic3wp0r8puXKxSFWCCEQqaq3n-Q");
        return new Cloudinary(config);
    }
}