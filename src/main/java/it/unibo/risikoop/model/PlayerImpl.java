package it.unibo.risikoop.model;

import java.util.List;

public class PlayerImpl implements Player {
    private String name;
    private List<Territory> territories;

    /**
     * 
     * @return
     */
    @Override
    public List<Territory> getTerritories() {
        return List.of();
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getName'");
    }

    @Override
    public Integer getTotalUnits() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTotalUnits'");
    }

    @Override
    public List<TerritoryCard> getTerritoryCards() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTerritoryCards'");
    }

}
