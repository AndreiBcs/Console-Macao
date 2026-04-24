package Entities;

import java.util.ArrayList;
import java.util.List;

public class Player {
    List<Card> mana;

    public Player() {
        this.mana = new ArrayList<Card>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        this.mana.forEach(carte -> sb.append(this.mana.indexOf(carte))
                                            .append(" - ")
                                            .append(carte.toString())
                                            .append(System.lineSeparator()));

        return sb.toString();
    }

    public void AddCard(Card carte){
        this.mana.add(carte);
    }

    public Card PlayCard(int index){
        var carte = this.mana.get(index);
        this.mana.remove(carte);
        return carte;
    }

    public Card GetCardByIndex(int index){
        return this.mana.get(index);
    }

    public boolean IsEmpty(){
        return this.mana.isEmpty();
    }

    public int NumberOfCards() {
        return this.mana.size();
    }
}
