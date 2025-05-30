package com.devtalks.demo.ai.chat.core;

import javax.sql.DataSource;

import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

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

    @Bean
    @Primary
    EmbeddingModel embeddingModel(@Qualifier("ollamaEmbeddingModel") EmbeddingModel ollamaEmbeddingModel) {
        return ollamaEmbeddingModel;
    }
}
