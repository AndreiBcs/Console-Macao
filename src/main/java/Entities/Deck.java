package Entities;

import Entities.Common.Rank;
import Entities.Common.Suit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    List<Card> pachet;

    public Deck() {
        this.pachet = new ArrayList<>();
    }

    public void InitialiseDeck(){
        for(var s : Suit.values()){
            for(var r : Rank.values()){
                pachet.add(new Card(s, r));
            }
        }
    }

    public void Shuffle(){
        Collections.shuffle(this.pachet);
    }

    public Card TakeCard() {
        var card = this.pachet.getFirst();
        this.pachet.removeFirst();
        return card;
    }

    public Player DealInitialHand(){
        var cards = new Player();
        for(int i = 0; i < 5; i++){
            var card = this.pachet.getFirst();

            cards.AddCard(card);
            this.pachet.removeFirst();
        }
        System.out.println();

        return cards;
    }

    public boolean IsEmpty(){
        return this.pachet.isEmpty();
    }
}
