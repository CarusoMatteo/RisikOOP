classDiagram
direction TB
    class Controller {
	    +start()
	    +beginToPlay()
	    +beginMapSelection()
	    +gameOver()
    }

    class DataRetrieveController {
	    +getPlayerList() List~Player~
	    +getActualMap() Graph
	    +getTerritories() Set~Territory~
	    +getCurrentPlayer() Player
	    getContinents() Set~Continent~
	    +isOwned(String , String) boolean
    }

    class DataAddingController {
	    +addPlayer(String , int , int , int) boolean
	    +loadWorldFromFile(File) boolean
	    +setDefaultMap()
    }

	<<interrface>> DataRetrieveController
	<<interface>> DataAddingController

    Controller --* DataRetrieveController
    Controller --* DataAddingController

