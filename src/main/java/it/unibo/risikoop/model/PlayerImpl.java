package it.unibo.risikoop.model;

import java.util.List;

public class PlayerImpl implements Player {
    private final String name;
    private final Color color;
    private List<Territory> territories;

    /**
     * 
     * @return
     */
    public PlayerImpl(String name, Color col) {
        this.name = name;
        this.color = new Color(col.r(), col.g(), col.b());
    }

    @Override
    public List<Territory> getTerritories() {
        return List.of();
    }

    @Override
    public String getName() {
        return name;
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

    @Override
    public Color getColor() {
        return new Color(color.r(), color.g(), color.b());
    }

}
