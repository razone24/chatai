package com.devtalks.demo.ai.chat.core;

import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class CoreConfiguration {

    @Bean
    PromptChatMemoryAdvisor promptChatMemoryAdvisor(DataSource dataSource) {
        var jdbc = JdbcChatMemoryRepository.builder()
                                           .dataSource(dataSource)
                                           .build();
        var chatMessageWindow = MessageWindowChatMemory.builder()
                                                       .chatMemoryRepository(jdbc)
                                                       .build();
        return PromptChatMemoryAdvisor.builder(chatMessageWindow)
                                      .build();
    }
}
