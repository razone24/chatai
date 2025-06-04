package com.devtalks.demo.ai.chat.tools;

import java.util.Map;
import java.util.logging.Logger;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class ToolsConfiguration {

    private final Logger logger = Logger.getLogger(ToolsConfiguration.class.getName());

    @Tool(description = "Get reward information for games including Monopoly, Chess, and Scrabble. Call this when users ask about game rewards or game information for these specific games.")
    public String getGameRewards(String gameName) {
        var gameDetails = getAllGameRewardDetails();
        var gameInfo = gameDetails.getOrDefault(gameName.toLowerCase(), "Game not found or no rewards available.");
        logger.info("Game reward details for " + gameName + ": " + gameInfo);
        return gameInfo;
    }

    private Map<String, String> getAllGameRewardDetails() {
        return Map.of("monopoly", "Monopoly game can reward with virtual money, properties, and chance cards.",
                      "chess", "Chess game can reward with points based on the pieces captured and the final outcome of the game.",
                      "scrabble", "Scrabble game can reward with points based on the words formed and their letter values.");
    }
}
