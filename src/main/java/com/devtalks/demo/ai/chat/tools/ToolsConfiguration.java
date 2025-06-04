package com.devtalks.demo.ai.chat.tools;

import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class ToolsConfiguration {

    private final Logger logger = Logger.getLogger(ToolsConfiguration.class.getName());

    @Tool(description = "Retrieve information about a game or a feature")
    public String getGameInfo(String game) {
        var gameDetails = getAllGameDetails();
        var gameInfo = gameDetails.getOrDefault(game.toLowerCase(), "Game not found or no rewards available.");
        logger.info("Game reward details for " + game + ": " + gameInfo);
        return gameInfo;
    }

    @Tool(description = "Retrieve the owner of a specific game or feature")
    public String getGameOwner(String gameName) {
        var ownerDetails = getAllOwnerDetails();
        var ownerInfo = ownerDetails.getOrDefault(gameName.toLowerCase(), "Owner information not found.");
        logger.info("Game owner details for " + gameName + ": " + ownerInfo);
        return ownerInfo;
    }

    @Tool(description = "Retrieve a list of all available games / features that have information")
    public Set<String> getAllGamesInformation() {
        Set<String> games = getAllGameDetails().keySet();
        logger.info("All games found: " + games);
        return games;
    }

    private Map<String, String> getAllGameDetails() {
        return Map.of(
            // Casino games from wiki database
            "poker", "Poker feature rewards 70% chips, 10% coins, 10% gems and 10% experience points",
            "blackjack", "Blackjack feature rewards 60% chips, 20% coins, 10% gems and 10% experience points", 
            "roulette", "Roulette feature rewards 50% chips, 30% coins, 10% gems and 10% experience points",
            "slots", "Slots feature rewards 40% chips, 40% coins, 10% gems and 10% experience points",
            "bingo", "Bingo feature rewards 30% chips, 50% coins, 10% gems and 10% experience points",
            // Board games
            "chess", "Chess game can reward with points based on the pieces captured and the final outcome of the game.",
            "monopoly", "Monopoly game can reward with virtual money, properties, and chance cards.",
            "scrabble", "Scrabble game can reward with points based on the words formed and their letter values."
        );
    }

    private Map<String, String> getAllOwnerDetails() {
        return Map.of(
            "poker", "Poker Team",
            "blackjack", "Blackjack Team",
            "roulette", "Roulette Team", 
            "slots", "Slots Team",
            "bingo", "Bingo Team",
            "chess", "Chess Team"
        );
    }
}
