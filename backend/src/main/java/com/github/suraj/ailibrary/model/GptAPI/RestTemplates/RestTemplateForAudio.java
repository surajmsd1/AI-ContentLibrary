package com.github.suraj.ailibrary.model.GptAPI.RestTemplates;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateForAudio {

    @Value("${openai.api.key}")
    private String openaiApiKey;

    @Bean
    @Qualifier("openaiAudioRestTemplate")
    public RestTemplate openaiAudioTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // Create a list of message converters
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(new StringHttpMessageConverter());
        converters.add(new ResourceHttpMessageConverter());
        converters.add(new FormHttpMessageConverter()); // For multipart
        converters.add(new MappingJackson2HttpMessageConverter()); // For JSON

        restTemplate.setMessageConverters(converters);
        return restTemplate;

    }
}
