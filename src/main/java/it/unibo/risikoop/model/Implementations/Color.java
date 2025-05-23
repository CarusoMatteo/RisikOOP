package it.unibo.risikoop.model.Implementations;

public record Color(int r, int g, int b) {
    public final boolean equals(Color col) {
        return this.r == col.r && this.g == col.g && this.b == col.b;
    }
}
