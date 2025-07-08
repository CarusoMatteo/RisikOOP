package it.unibo.risikoop.model.implementations;

import java.util.Random;

import it.unibo.risikoop.model.implementations.objectivecards.ConquerNContinetsBuilder;
import it.unibo.risikoop.model.implementations.objectivecards.ConquerNTerritoriesWithXArmiesBuilder;
import it.unibo.risikoop.model.implementations.objectivecards.KillPlayerOrConquer24Builder;
import it.unibo.risikoop.model.implementations.specification.ConquerTerritoriesWithMinArmiesSpec;
import it.unibo.risikoop.model.implementations.specification.KillPlayerOrConquer24TerritoriesSpec;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.ObjectiveCard;
import it.unibo.risikoop.model.interfaces.ObjectiveCardFactory;
import it.unibo.risikoop.model.interfaces.Player;

public class ObjectiveCardFactoryImpl implements ObjectiveCardFactory {

    enum ObjectiveType {
        KILL_PLAYER_OR_CONQUER_24_TERRITORIES,
        CONQUER_TERRITORIES,
        CONQUER_CONTINENTS
    }

    private final Random random;
    private final GameManager gameManager;

    public ObjectiveCardFactoryImpl(GameManager gameManager) {
        this.random = new Random();
        this.gameManager = gameManager;
    }

    @Override
    public ObjectiveCard createObjectiveCard(Player owner) {
        ObjectiveType type = getRandomObjectiveType();

        switch (type) {
            case KILL_PLAYER_OR_CONQUER_24_TERRITORIES:
                return new KillPlayerOrConquer24Builder(gameManager, owner).createCard();
            case CONQUER_TERRITORIES:
                return new ConquerNTerritoriesWithXArmiesBuilder(
                        gameManager,
                        owner,
                        18,
                        2).createCard();
            case CONQUER_CONTINENTS:
                return new ConquerNContinetsBuilder(gameManager, owner).createCard();
            default:
                throw new IllegalArgumentException("Unknown objective type: " + type);
        }
    }

    private ObjectiveType getRandomObjectiveType() {
        ObjectiveType[] types = ObjectiveType.values();
        return types[random.nextInt(types.length)];
    }

}
