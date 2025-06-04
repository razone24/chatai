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

	public ChatAIService(JdbcClient db,
	                     ChatClient.Builder ai,
	                     WikiRepository wikiRepository,
	                     VectorStore vectorStore,
	                     ToolsConfiguration toolsConfiguration, PromptChatMemoryAdvisor promptChatMemoryAdvisor) {
		int count = db.sql("SELECT count(*) FROM vector_store")
				.query(Integer.class)
				.single();
		if (count == 0) {
			wikiRepository.findAll()
					.forEach(wiki -> createVectorStoreData(vectorStore, wiki));
		}

		var systemPrompt = """
				You provide game information from a wiki database with this exact structure:
				- id: unique identifier
				- feature: game name (like "Poker Game", "Blackjack Game", "Roulette Game", "Slots Game", "Bingo Game")
				- owner: team responsible (like "Poker Team", "Blackjack Team", etc.)
				- description: detailed reward information
				
				When users ask about games, search the wiki database for matching entries and respond based on what they're asking for:
				
				When users ask about games:
				1. First check if the requested game exists in the wiki database
				2. If the game exists, search for the matching entry and respond based on what they're asking for:
				   - For REWARD/DESCRIPTION questions: Return ONLY the description field content
				   - For OWNER questions: Return ONLY the owner field content
				   - For FEATURE NAME questions: Return ONLY the feature field content
				3. If the game does NOT exist in the wiki database, say "I don't have information about [game name] in the wiki database."
				
				FORBIDDEN: Never return the ID field. Never return information about the wrong game.
				
				Never include field names, formatting, additional explanation, or the ID field. Just the pure field content that answers their question.
				""";
		this.ai = ai.defaultAdvisors(
						new QuestionAnswerAdvisor(vectorStore),
						promptChatMemoryAdvisor

				)
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
