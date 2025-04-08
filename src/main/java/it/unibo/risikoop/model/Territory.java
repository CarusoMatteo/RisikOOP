package it.unibo.risikoop.model;

import java.util.List;

public interface Territory {
    /**
     * 
     * @return
     */
    public String getName();

    /**
     * 
     * @return
     */
    public Player getOwner();

    /**
     * 
     * @param newOwner
     * @return
     */
    public boolean setOwner(Player newOwner);

    /**
     * Add new units to the territory
     * 
     * @param addedUnits
     * @Throws IllegamArgumentException if the units are negative
     */
    public void addUnits(int addedUnits);

    /**
     * Add new units to the territory
     * 
     * @param removedUnits
     * @Throws IllegamArgumentException if the units are negative
     */
    public void removeUnits(int removedUnits);

    /**
     * 
     * @return
     */
    public Integer getUnits();

    /**
     * 
     * @return
     */
    public List<Territory> getNeightbours();

    // TODO: Decidere come decidere i continenti e le appartenenze
}
