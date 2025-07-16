package it.unibo.risikoop.view.interfaces;

import java.util.List;

import it.unibo.risikoop.model.implementations.Color;
import it.unibo.risikoop.model.interfaces.ObjectiveCard;
import it.unibo.risikoop.model.interfaces.cards.GameCard;

/**
 * Interface for the MapScene.
 * Defines methods to update the view of the current player and their cards.
 */
public interface MapScene {

        /**
         * Updates the current player information displayed in the MapScene.
         * 
         * @param playerName    The name of the current player.
         * @param playerColor   The color of the current player.
         * @param objectiveCard The objective card of the current player.
         * @param cards         The cards of the current player.
         */
        void updateCurrentPlayer(String playerName,
                        Color playerColor,
                        ObjectiveCard objectiveCard,
                        List<GameCard> cards);

        /**
         * change the displayed units for the the specific territory;
         * 
         * @param territoryName
         */
        public void changeTerritoryUnits(String territoryName, int units);

}
