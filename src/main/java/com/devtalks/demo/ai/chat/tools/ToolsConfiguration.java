package com.devtalks.demo.ai.chat.tools;

import java.util.Map;
import java.util.logging.Logger;
import java.time.Duration;

import com.devtalks.demo.ai.chat.config.BraveSearchConfig;
import com.devtalks.demo.ai.chat.dto.BraveSearchResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class ToolsConfiguration {

    private final Logger logger = Logger.getLogger(ToolsConfiguration.class.getName());
    private final WebClient webClient;
    private final BraveSearchConfig braveSearchConfig;

    @Autowired
    public ToolsConfiguration(BraveSearchConfig braveSearchConfig) {
        this.braveSearchConfig = braveSearchConfig;
        
        this.webClient = WebClient.builder()
                .defaultHeader("Accept", "application/json")
                .defaultHeader("Accept-Encoding", "gzip")
                .defaultHeader("User-Agent", "ChatAI/1.0")
                .build();
    }

    @Tool(description = "Get game details by game name")
    public String getGameDetails(String gameName) {
        logger.info("Looking for game reward details for " + gameName + " in external sources");
        
        Map<String, String> gameDetails = getAllGameRewardDatils();
        String searchTerm = gameName.toLowerCase().trim();
        
        // First try exact match (case-insensitive)
        for (Map.Entry<String, String> entry : gameDetails.entrySet()) {
            if (entry.getKey().toLowerCase().equals(searchTerm)) {
                logger.info("Found game reward details for " + gameName + " in external sources. Found: " + entry.getValue());
                return entry.getValue();
            }
        }
        
        // Then try partial match - check if search term contains any game name
        for (Map.Entry<String, String> entry : gameDetails.entrySet()) {
            String key = entry.getKey().toLowerCase();
            if (searchTerm.contains(key) || key.contains(searchTerm)) {
                logger.info("Found game reward details for " + gameName + " in external sources. Found: " + entry.getValue());
                return entry.getValue();
            }
        }
        
        // If no match found, return available options
        return "Game not found. Available games: " + String.join(", ", gameDetails.keySet()) + 
               ". Please try searching for one of these games.";
    }

//    @Tool(description = "Search the internet for current information using Brave Search API. Use this for finding recent news, current events, or any information not in the knowledge base.")
    public String searchInternet(String query) {
        try {
            logger.info("Searching internet for: " + query);
            
            // Check if API key is configured
            if (braveSearchConfig.getKey() == null || braveSearchConfig.getKey().equals("your-brave-api-key-here")) {
                logger.warning("Brave API key not configured properly");
                return "Internet search is not available. Please configure your Brave API key in the .env file.";
            }
            
            BraveSearchResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .host("api.search.brave.com")
                            .path("/res/v1/web/search")
                            .queryParam("q", query)
                            .queryParam("count", "5") // Limit to top 5 results
                            .build())
                    .header("X-Subscription-Token", braveSearchConfig.getKey())
                    .retrieve()
                    .bodyToMono(BraveSearchResponse.class)
                    .timeout(Duration.ofSeconds(15))
                    .block();

            if (response != null && response.getWeb() != null && response.getWeb().getResults() != null) {
                StringBuilder results = new StringBuilder();
                results.append("Search results for '").append(query).append("':\n\n");
                
                response.getWeb().getResults().stream()
                        .limit(3) // Limit to top 3 results to keep response manageable
                        .forEach(result -> {
                            results.append("• ").append(result.getTitle()).append("\n");
                            if (result.getDescription() != null && !result.getDescription().isEmpty()) {
                                results.append("  ").append(result.getDescription()).append("\n");
                            }
                            results.append("  URL: ").append(result.getUrl()).append("\n\n");
                        });
                
                String searchResults = results.toString();
                logger.info("Search completed successfully for: " + query);
                return searchResults;
            } else {
                logger.warning("No results found for query: " + query);
                return "No search results found for: " + query;
            }
            
        } catch (WebClientResponseException e) {
            logger.severe("HTTP error searching internet for query '" + query + "': " + e.getStatusCode() + " - " + e.getMessage());
            if (e.getStatusCode().value() == 401) {
                return "Authentication failed. Please check your Brave API key configuration.";
            } else if (e.getStatusCode().value() == 429) {
                return "Rate limit exceeded. Please try again later.";
            } else {
                return "HTTP error occurred while searching the internet: " + e.getStatusCode();
            }
        } catch (Exception e) {
            logger.severe("Error searching internet for query '" + query + "': " + e.getMessage());
            return "Sorry, I encountered an error while searching the internet. Please try again later.";
        }
    }

    private String externalGameRewardDetails() {
        return """
                Monopoly game can reward with virtual money, properties, and chance cards.
                
                Chess game can reward with points based on the pieces captured and the final outcome of the game.
                
                Scrabble game can reward with points based on the words formed and their letter values.
                """;
    }

    private Map<String, String> getAllGameRewardDatils() {
        return Map.of("monopoly", "Monopoly game can reward with virtual money, properties, and chance cards.",
                "chess", "Chess game can reward with points based on the pieces captured and the final outcome of the game.",
                "scrabble", "Scrabble game can reward with points based on the words formed and their letter values.");
    }
}
