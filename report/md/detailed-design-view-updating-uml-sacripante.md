classDiagram
direction RL
    class ControllerImpl {
	    -TurnManager turnManager
	    +start()
	    +beginToPlay()
	    +beginMapSelection()
	    +gameOver()
    }

    class RisikoView {
	    +start()
	    +chooseMap()
	    +beginPlay()
	    +gameOver()
	    +showErrorMessage(String)
	    +getMapScene() MapScene
    }

    class MapScene {
	    +updateCurrentPlayer(String,String,String,List~GameCard~)
	    +changeTerritoryUnits(String,int)
	    +updateTerritoryOwner()
    }

	<<interface>> RisikoView
	<<interface>> MapScene

    ControllerImpl *-- RisikoView
    MapScene --* RisikoView

