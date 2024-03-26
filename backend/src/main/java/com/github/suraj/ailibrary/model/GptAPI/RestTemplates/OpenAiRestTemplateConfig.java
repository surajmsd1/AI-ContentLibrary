package com.github.suraj.ailibrary.model.GptAPI.RestTemplates;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OpenAiRestTemplateConfig {

    @Value("${openai.api.key}")
    private String openaiApiKey;

//    @Value("${openai.api.org}")
//    private String openaiApiOrgKey;

    @Bean
    @Qualifier("openaiRestTemplate")
    public RestTemplate openAiTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + openaiApiKey);
//            request.getHeaders().add("OpenAI-Organization", openaiApiOrgKey);
            return execution.execute(request, body);
        });
        return restTemplate;
    }
}
