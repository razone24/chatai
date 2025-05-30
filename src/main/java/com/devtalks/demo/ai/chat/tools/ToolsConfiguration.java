package com.devtalks.demo.ai.chat.tools;

import java.util.Map;
import java.util.logging.Logger;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class ToolsConfiguration {

    private final Logger logger = Logger.getLogger(ToolsConfiguration.class.getName());

    @Tool(description = "Get game reward details by game name with the following options: Monopoly, Chess, Scrabble")
    public String getWebSearchResult(String gameName) {
        var gameDetails = getAllGameRewardDatils();
        var gameInfo = gameDetails.getOrDefault(gameName, "Game not found or no rewards available.");
        logger.info("Game reward details for " + gameName + ": " + gameInfo);
        return gameInfo;
    }

    private Map<String, String> getAllGameRewardDatils() {
        return Map.of("Monopoly", "Monopoly game can reward with virtual money, properties, and chance cards.",
                      "Chess", "Chess game can reward with points based on the pieces captured and the final outcome of the game.",
                      "Scrabble", "Scrabble game can reward with points based on the words formed and their letter values.");
    }
}
