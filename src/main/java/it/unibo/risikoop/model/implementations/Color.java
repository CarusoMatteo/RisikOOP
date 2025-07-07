package it.unibo.risikoop.model.implementations;

/**
 * player's color class.
 * 
 * @param r
 * @param g
 * @param b
 */
public record Color(int r, int g, int b) {
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof final Color col) {
            return this.r == col.r && this.g == col.g && this.b == col.b;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.r * 10 + this.b * 100 + this.g * 1000;
    }
}
