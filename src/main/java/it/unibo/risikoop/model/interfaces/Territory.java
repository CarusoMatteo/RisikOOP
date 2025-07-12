package it.unibo.risikoop.model.interfaces;

import java.util.Set;

/**
 * 
 */
public interface Territory {
    /**
     * 
     * @return the namke
     */
    String getName();

    /**
     * 
     * @return the owner
     */
    Player getOwner();

    /**
     * set new Terriotry owner
     */
    void setOwner(Player owner);

    /**
     * set (if possible) a new owner.
     * 
     * @param newOwner
     * @return if it was possible or not to change the owner.
     */
    boolean changeOwner(Player newOwner);

    /**
     * Add new units to the territory.
     * 
     * @param addedUnits
     * @Throws IllegamArgumentException if the units are negative
     */
    void addUnits(int addedUnits);

    /**
     * Add new units to the territory.
     * 
     * @param removedUnits
     * @Throws IllegamArgumentException if the units are negative
     */
    void removeUnits(int removedUnits);

    /**
     * 
     * @return the total units
     */
    Integer getUnits();

    /**
     * get the Neightbours list.
     * 
     * @return all the neighbours
     */
    Set<Territory> getNeightbours();

}
