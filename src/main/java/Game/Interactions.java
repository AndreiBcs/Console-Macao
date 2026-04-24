package Game;

import Entities.Card;

public class Interactions {
    public static boolean IsValidMove(Card card1, Card card2){
        return card1.culoare.equals(card2.culoare) || card1.valoare.equals(card2.valoare);
    }
}
