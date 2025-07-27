**2.2.1 Gestione del “mazzo” di carte obiettivo**
classDiagram
    class ObjectiveCardFactory{
        <<interface>>
        + ObjectiveCard createObjectiveCard(Player owner)
    }

    class AbstractObjectiveCardBuilder {
        <<abstarct>>
        +createCard() ObjectiveCard
        #buildDescription() String
        #buildSpecification() Specification
    }

    class ConquerNContinetsBuilder {

    }

    class ConquerNTerritoriesWithXArmiesBuilder {
    }

    class KillPlayerOrConquer24Builder {
    }

    AbstractObjectiveCardBuilder <|-- ConquerNContinetsBuilder
    AbstractObjectiveCardBuilder <|-- ConquerNTerritoriesWithXArmiesBuilder
    AbstractObjectiveCardBuilder <|-- KillPlayerOrConquer24Builder
    ObjectiveCardFactory <|-- AbstractObjectiveCardBuilder
---

**2.2.3 Gestione delle fasi di gioco**
---

---
config:
  class:
    hideEmptyMembersBox: false
  layout: elk
  look: classic
  theme: mc
---
classDiagram
direction TB
    class GamePhase {
        <<interface>>
	    +boolean selectTerritory(Territory t)
	    +boolean isComplete()
    }
    class PhaseDescribable {
        <<interface>>
	    + String getInnerStatePhaseDescription()
    }
    class PhaseWithActionToPerforme {
        <<interface>>
	    + void performAction()
    }
    class PhaseWithAttack {
        <<interface>>
	    + Optional showAttackResults()
	    + void enableFastAttack()
    }
    class PhaseWithInitialization {
        <<interface>>
	    + void initializationPhase()
    }
    class PhaseWithTransaction {
        <<interface>>
	    + void nextState()
	    + InternalState getInternalState()
    }
    class PhaseWithUnits {
        <<interface>>
	    + void setUnitsToUse(int units)
    }
    class AttackPhase {
    }
    class ComboPhaseImpl {
    }
    class InitialReinforcementPhase {
    }
    class MovementPhase {
    }
    class ReinforcementPhase {
    }
    GamePhase <|-- AttackPhase
    GamePhase <|-- ComboPhaseImpl
    GamePhase <|-- InitialReinforcementPhase
    GamePhase <|-- MovementPhase
    GamePhase <|-- ReinforcementPhase
    PhaseDescribable <|-- AttackPhase
    PhaseDescribable <|-- InitialReinforcementPhase
    PhaseDescribable <|-- MovementPhase
    PhaseDescribable <|-- ReinforcementPhase
    PhaseWithActionToPerforme <|-- AttackPhase
    PhaseWithActionToPerforme <|-- InitialReinforcementPhase
    PhaseWithActionToPerforme <|-- MovementPhase
    PhaseWithAttack <|-- AttackPhase
    PhaseWithInitialization <|-- AttackPhase
    PhaseWithInitialization <|-- InitialReinforcementPhase
    PhaseWithInitialization <|-- MovementPhase
    PhaseWithInitialization <|-- ReinforcementPhase
    PhaseWithTransaction <|-- AttackPhase
    PhaseWithTransaction <|-- MovementPhase
    PhaseWithUnits <|-- AttackPhase
    PhaseWithUnits <|-- MovementPhase
---

**2.2.2 Composizione delle condizioni di vittoria con Specification Pattern**
classDiagram
    class Specification~T~ {
        <<interface>>
        +isSatisfiedBy(candidate T) boolean
        +and(other Specification~T~) Specification~T~
        +or(other Specification~T~) Specification~T~
        +not() Specification~T~
    }

    class KillPlayerSpec {
        +isSatisfiedBy(ctx PlayerGameContext) boolean
    }

    class ConquerTerritoriesSpec {
        +isSatisfiedBy(ctx PlayerGameContext) boolean
    }

    class ConquerContinentsSpec {
        +isSatisfiedBy(ctx PlayerGameContext) boolean
    }

    class ConquerTerritoriesWithMinArmiesSpec {
        +isSatisfiedBy(ctx PlayerGameContext) boolean
    }

    class KillPlayerOrConquer24TerritoriesSpec {
        +isSatisfiedBy(candidate PlayerGameContext) boolean
    }

    %% Inheritance from Specification
    Specification~T~ <|.. KillPlayerSpec
    Specification~T~ <|.. ConquerTerritoriesSpec
    Specification~T~ <|.. ConquerContinentsSpec
    Specification~T~ <|.. ConquerTerritoriesWithMinArmiesSpec
    Specification~T~ <|.. KillPlayerOrConquer24TerritoriesSpec

    %% Usage relationships
    KillPlayerOrConquer24TerritoriesSpec ..> KillPlayerSpec
    KillPlayerOrConquer24TerritoriesSpec ..> ConquerTerritoriesSpec