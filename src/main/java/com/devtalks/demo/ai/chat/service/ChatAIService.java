package com.devtalks.demo.ai.chat.service;

import java.util.List;

import com.devtalks.demo.ai.chat.repository.Wiki;
import com.devtalks.demo.ai.chat.repository.WikiRepository;
import com.devtalks.demo.ai.chat.tools.ToolsConfiguration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

@Service
public class ChatAIService {

    private final ChatClient ai;
	private final ToolsConfiguration toolsConfiguration;

    public ChatAIService(JdbcClient db,
                         PromptChatMemoryAdvisor promptChatMemoryAdvisor,
                         ChatClient.Builder ai,
                         WikiRepository wikiRepository,
                         VectorStore vectorStore,
                         ToolsConfiguration toolsConfiguration) {
	    int count = db.sql("SELECT count(*) FROM vector_store")
                      .query(Integer.class)
                      .single();
        if (count == 0) {
            wikiRepository.findAll()
                          .forEach(wiki -> createVectorStoreData(vectorStore, wiki));
        }
        var systemPrompt = """
            You are a helpful AI assistant that provides information about game features such as poker game, blackjack game, roulette game, slots game and bingo game.
            You have access to tools to help answer questions:
            1. Game reward details for Monopoly, Chess, and Scrabble
            
            The user will ask questions about features, and you will provide relevant information based on the available data.
            When you need current information or recent updates, use the internet search tool.
            If you don't know the answer and can't find it through available tools, you will say "I don't know".
            """;
	    this.toolsConfiguration = toolsConfiguration;
		this.ai = ai.defaultAdvisors(
                        new QuestionAnswerAdvisor(vectorStore),
                        promptChatMemoryAdvisor
                    )
                    .defaultTools(toolsConfiguration)
                    .defaultSystem(systemPrompt)
                    .build();
    }

	public String ask(String user, String message) {
		var response = ai.prompt()
				.user(message)
				.advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, user))
				.call();
		return response.content();
	}

	private static void createVectorStoreData(VectorStore vectorStore, Wiki wiki) {
		var dataFormat = "id: %s, feature: %s, owner: %s, description: %s";
		var wikiDocument = new Document(dataFormat.formatted(wiki.id(), wiki.feature(), wiki.owner(), wiki.description()));
		vectorStore.add(List.of(wikiDocument));
	}
}
