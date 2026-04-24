package Entities;

import Entities.Common.Rank;
import Entities.Common.Suit;

public class Card {
    public Suit culoare;
    public Rank valoare;

    public Card(){ }

    @Override
    public String toString() {
        return this.valoare + " " + this.culoare;
    }

    public Card(Suit culoare, Rank valoare) {
        this.culoare = culoare;
        this.valoare = valoare;
    }
}
