package it.unibo.risikoop.model.implementations;

public record Color(int r, int g, int b) {
    public final boolean equals(final Color col) {
        return this.r == col.r && this.g == col.g && this.b == col.b;
    }
}
