package Entities;

import Entities.Common.Rank;

import java.util.ArrayList;
import java.util.List;

public class Player {
    public List<Card> mana;

    public Player() {
        this.mana = new ArrayList<>();
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

    public boolean NotEmpty(){
        return !this.mana.isEmpty();
    }

    public int NumberOfCards() {
        return this.mana.size();
    }

    public boolean HasStopResponse(){

        return this.mana.stream()
                .anyMatch(carte ->
                        carte.valoare == Rank.Patru
                                || carte.valoare == Rank.K);

    }

    public boolean HasCounterResponse(){

        return this.mana.stream()
                .anyMatch(carte ->
                        carte.valoare == Rank.Doi
                                || carte.valoare == Rank.J);

    }

    public int GetStopCardIndex(){
        for(int i = 0; i < this.mana.size(); i++){
            if(this.mana.get(i).valoare == Rank.Patru
                    || this.mana.get(i).valoare == Rank.K){

                return i;
            }
        }
        return -1;
    }

    public int GetAttackCardIndex(){
        for(int i = 0; i < this.mana.size(); i++){
            if(this.mana.get(i).valoare == Rank.Doi
                    || this.mana.get(i).valoare == Rank.J){

                return i;
            }
        }
        return -1;
    }
}
