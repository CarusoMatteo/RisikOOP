package it.unibo.risikoop.model.implementations;

import java.util.Random;

import it.unibo.risikoop.model.implementations.gamecards.objectivecards.ConquerNContinetsBuilder;
import it.unibo.risikoop.model.implementations.gamecards.objectivecards.ConquerNTerritoriesWithXArmiesBuilder;
import it.unibo.risikoop.model.implementations.gamecards.objectivecards.KillPlayerOrConquer24Builder;
import it.unibo.risikoop.model.interfaces.GameManager;
import it.unibo.risikoop.model.interfaces.ObjectiveCard;
import it.unibo.risikoop.model.interfaces.ObjectiveCardFactory;
import it.unibo.risikoop.model.interfaces.Player;

/**
 * Implementation of the ObjectiveCardFactory interface.
 * This factory creates objective cards for players based on random selection of
 * objective types.
 */
public final class ObjectiveCardFactoryImpl implements ObjectiveCardFactory {

    enum ObjectiveType {
        KILL_PLAYER_OR_CONQUER_24_TERRITORIES,
        CONQUER_TERRITORIES,
        CONQUER_CONTINENTS
    }

    private static final int MIN_ARMIES_FOR_CONQUER = 2;
    private static final int MIN_TERRITORIES_FOR_CONQUER = 18;
    private final Random random;
    private final GameManager gameManager;

    /**
     * Constructs an ObjectiveCardFactoryImpl with the specified GameManager.
     *
     * @param gameManager the GameManager that manages the game state
     */
    public ObjectiveCardFactoryImpl(final GameManager gameManager) {
        this.random = new Random();
        this.gameManager = gameManager;
    }

    @Override
    public ObjectiveCard createObjectiveCard(final Player owner) {
        final ObjectiveType type = getRandomObjectiveType();

        switch (type) {
            case KILL_PLAYER_OR_CONQUER_24_TERRITORIES:
                return new KillPlayerOrConquer24Builder(gameManager, owner).createCard();
            case CONQUER_TERRITORIES:
                return new ConquerNTerritoriesWithXArmiesBuilder(
                        gameManager,
                        owner,
                        MIN_TERRITORIES_FOR_CONQUER,
                        MIN_ARMIES_FOR_CONQUER).createCard();
            case CONQUER_CONTINENTS:
                return new ConquerNContinetsBuilder(gameManager, owner).createCard();
        }

        return null;
    }

    private ObjectiveType getRandomObjectiveType() {
        final ObjectiveType[] types = ObjectiveType.values();
        return types[random.nextInt(types.length)];
    }

}
