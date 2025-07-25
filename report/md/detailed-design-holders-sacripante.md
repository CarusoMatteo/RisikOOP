classDiagram
direction LR
    class GameManager {
	    +getTerritory(String) Optional~Territory~
	    +getContinent(String) Optional~Continent~
	    +getTerritories() Set~Territory~
	    +removeAllTerritoriesAndContinents()
	    +getTerritoryNeightbours(String)
	    +addUnits(String , int)
	    +removeUnits(String , int)
	    +setWorldMap(Graph)
	    +setContinents(Set~Continent~)
	    +getContinents()
	    +getActualWorldMap() Graph;
    }

    class PlayersHolder {
	    +addPlayer(String , Color) boolean
	    +getPlayers() List~Player~
	    +removePlayer(String) boolean
    }

    class Territory {
	    +getName() String
	    +getOwner() Player
	    +getUnits() Integer
	    +getNeightbours() List~Territory~
    }

    class Continent {
	    +getName() String
	    +getUnitReward() Integer
    }

    class Player {
	    +getTotalUnits() Integer
	    +getGameCards() List~GameCard~
	    +getName() String
	    +getColor() Color
	    +getKiller() Optional~Player~
	    +getUnitsToPlace() int
	    +getHand() PlayerHand
	    +isEliminated() boolean
	    +getObjectiveCard() ObjectiveCard
    }

    class TerritoryHolder {
	    +addTerritory(Territory) boolean
	    +getTerritories() Collection~Territory~
	    +removeTerritory(Territory) boolean
    }

	<<interface>> Territory
	<<interface>> Continent
	<<interface>> Player

    GameManager --|> PlayersHolder
    Continent --|> TerritoryHolder
    PlayersHolder --* Player
    Player --|> TerritoryHolder
    TerritoryHolder --* Territory

