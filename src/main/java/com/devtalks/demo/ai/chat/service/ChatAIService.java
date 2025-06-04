package com.devtalks.demo.ai.chat.service;

import com.devtalks.demo.ai.chat.tools.ToolsConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ChatAIService {

	private static final Logger log = LoggerFactory.getLogger(ChatAIService.class);
	private final ChatClient ai;

	public ChatAIService(ChatClient.Builder ai,
	                     ToolsConfiguration toolsConfiguration) {
		
		var systemPrompt = """
				You are a game information assistant with access to functions.
				
				When users ask about games:
				- Extract the game name (poker, chess, etc.) from the question
				- Call getGameInfo function with just the game name
				- Call getGameOwner function for ownership questions
				- Call getAllGamesInformation function when asked about available games
				
				IMPORTANT: Execute the functions and return their actual results, not function call descriptions.
				
				Examples:
				- "Tell me about chess" → Call getGameInfo("chess") → Return the game details
				- "Who owns poker?" → Call getGameOwner("poker") → Return the owner
				- "What games are available?" → Call getAllGamesInformation() → Return the list
				""";
		
		this.ai = ai
				.defaultTools(toolsConfiguration)
				.defaultSystem(systemPrompt)
				.build();
	}

	public String ask(String user, String message) {
		log.info("Asking user {} to {}", user, message);
		var response = ai.prompt()
				.user(message)
				.call();
		return response.content();
	}
}
