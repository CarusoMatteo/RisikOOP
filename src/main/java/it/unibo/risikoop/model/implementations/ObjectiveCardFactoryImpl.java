package it.unibo.risikoop.model.implementations;

import java.util.Random;

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

        // TODO Auto-generated method stub
        return null;
    }

    private ObjectiveType getRandomObjectiveType() {
        ObjectiveType[] types = ObjectiveType.values();
        return types[random.nextInt(types.length)];
    }

    private ObjectiveCard createKillPlayerObjective(Player owner) {

        Player target = gameManager.getPlayers()
                .get(random.nextInt(gameManager.getPlayers().size()));

        String description = "Kill player " + target.getName() + "from the board or conquer at least 24 territories.";

        return new ObjectiveCardImpl(
                description,
                owner,
                gameManager,
                new KillPlayerOrConquer24TerritoriesSpec(target));
    }

    private ObjectiveCard createConquerTerritoriesObjective(Player owner) {

        String description = "Conquer at least 18 territories with at least 2 armies in each.";

        return new ObjectiveCardImpl(
                description,
                owner,
                gameManager,
                new ConquerTerritoriesWithMinArmiesSpec(2, 18));
    }
}
