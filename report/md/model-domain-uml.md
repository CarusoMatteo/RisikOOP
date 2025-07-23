```mermaid
classDiagram
direction BT
    class GameManager { <<interface>>
	    +getTerritories() List~Territory~
	    +removeAllTerritoriesAndContinents()
	    +setContinents(List~Continent~)
	    +getContinents() List~Continent~
	    +getActualWorldMap() Graph
    }

    class Territory { <<interface>>
	    +getName() String
	    +getOwner() Player
	    +getUnits() Integer
	    +getNeightbours() List~Territory~
    }

    class Continent { <<interface>>
	    +getName() String
	    +getUnitReward() Integer
    }

    class Player { <<interface>>
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

    class GameCard { <<interface>>
	    +getType() UnitType
	    +Optional~Player~ getOwner()
	    +isTerritoryCard() boolean
	    +updateOwner(Player) boolean 
    }

    class CardDeck { <<interface>>
	    +drawCard() GameCard 
	    +addCards(Set~GameCard~) boolean 
	    +isEmpty() boolean
    }

    class ObjectiveCard { <<interface>>
	    +isAchieved() boolean
	    +getDescription() String 
    }

    class TerritoryCard { <<interface>>
	    +getAssociatedTerritory() Territory
    }

    class TurnManager { <<interface>>
	    +getCurrentPlayer() Player 
	    +nextPlayer() Player 
	    +isLastPlayer() boolean  
    }

    Territory --* GameManager
    Player --* GameManager
    GameCard *-- Player
    ObjectiveCard *-- Player
    TerritoryCard --|> GameCard
    Player *--* Territory
    Continent --* Territory
    CardDeck *-- GameCard
    TurnManager *-- Player
    TerritoryCard *-- Territory
```