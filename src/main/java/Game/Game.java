package Game;

import Entities.Card;
import Entities.Common.Rank;
import Entities.Deck;
import Entities.Player;

import java.util.Scanner;

import static Game.Interactions.*;

public class Game {
    Deck pachet;
    Player jucator;
    Player calculator;
    Card carte;
    boolean playerTurn;
    Scanner input = new Scanner(System.in);
    String culoare = "";
    boolean esteAtacat = false;
    int numarCartiAtac = 0;

    public Game(){
        this.pachet = new Deck();
        this.pachet.InitialiseDeck();
        this.pachet.Shuffle();

        this.jucator = new Player();
        this.jucator = this.pachet.DealInitialHand();
        this.calculator = new Player();
        this.calculator = this.pachet.DealInitialHand();

        this.carte = new Card();
        this.carte = this.pachet.TakeCard();

        this.playerTurn = true;
    }

    public void Start() {
        System.out.println("Start Joc");
        PrintGameStatus();

        while(!this.pachet.IsEmpty() && this.jucator.NotEmpty() && this.calculator.NotEmpty()){

            // un boolean controleaza turele

            if(this.playerTurn){
                // mutarea jucatorului
                // daca in mana nu ai carti compatibile poti doar sa tragi carte

                if(this.esteAtacat){

                    // actiuniile jucatorului cand este atacat

                    if(this.jucator.HasStopResponse() || this.jucator.HasCounterResponse()){

                        System.out.println("Trebuie sa alegi:");
                        System.out.println("Joaca Doi sau J pentru contra-atac");
                        System.out.println("Joaca Patru sau K pentru a opri atacul");

                        while(true) {
                            int index = input.nextInt();

                            // joaca o carte de contra-atac

                            if ((index >= 0 && index < this.jucator.NumberOfCards()) &&
                                    ((this.jucator.GetCardByIndex(index).valoare == Rank.Doi) &&
                                            (this.jucator.GetCardByIndex(index).valoare == Rank.J))) {

                                System.out.println("Ai contra-atacat:");
                                System.out.println(this.jucator.GetCardByIndex(index).toString());

                                this.carte = this.jucator.PlayCard(index);
                                this.numarCartiAtac += 2;
                                break;
                            }

                            // joaca o carte de stop

                            if ((index >= 0 && index < this.jucator.NumberOfCards()) &&
                                    ((this.jucator.GetCardByIndex(index).valoare == Rank.Patru) &&
                                            (this.jucator.GetCardByIndex(index).valoare == Rank.K))) {

                                System.out.println("Ai oprit atacul:");
                                System.out.println(this.jucator.GetCardByIndex(index).toString());

                                this.carte = this.jucator.PlayCard(index);
                                this.numarCartiAtac = 0;
                                this.esteAtacat = false;
                                break;
                            }
                        }
                        // final raspunsul jucatorului la atac
                    }
                    else {
                        // nu are carti de contra, nici de stop

                        System.out.println("Nu poti opri atacul, trebuie sa tragi "
                                + this.numarCartiAtac + " carti");
                        System.out.println("'t' - trage carti");

                        char c = input.next().charAt(0);

                        while(true){
                            if(c == 't'){
                                for(int i = 0; i < this.numarCartiAtac; i++){
                                    this.calculator.AddCard(this.pachet.TakeCard());
                                }

                                System.out.println("Ai luat " + this.numarCartiAtac + " din pachet");
                                break;
                            }
                        }
                        this.numarCartiAtac = 0;
                        this.esteAtacat = false;
                    }
                    // final actiunea jucatorului cand este atacat
                }
                else{
                    // actiuniile jucatorului cand nu e atacat

                    if(CannotPlayCard(this.jucator, this.carte)){

                        System.out.println("'t' - trage carte");

                        char c = input.next().charAt(0);

                        // actiuni pentru jucator

                        while(true){
                            if(c == 't'){
                                this.jucator.AddCard(this.pachet.TakeCard());
                                System.out.println("Ai luat o carte din pachet");
                                break;
                            }
                        }

                    }
                    else{
                        System.out.println("'j' - joaca carte");
                        System.out.println("'t' - trage carte");

                        char c = input.next().charAt(0);

                        // actiuni pentru jucator

                        while(true){
                            if(c == 't'){
                                this.jucator.AddCard(this.pachet.TakeCard());
                                System.out.println("Ai luat o carte din pachet");
                                break;
                            }
                            if(c == 'j'){
                                System.out.println("Apasa numarul cartii pe care vrei sa o joci");
                                while(true){
                                    int index = input.nextInt();
                                    if((index >= 0 && index < this.jucator.NumberOfCards()) &&
                                            IsValidMove(this.carte, this.jucator.GetCardByIndex(index))) {

                                        System.out.println("Ai jucat:");
                                        System.out.println(this.jucator.GetCardByIndex(index).toString());

                                        this.carte = this.jucator.PlayCard(index);

                                        SpecialCard(this.carte);

                                        if(this.carte.valoare == Rank.Doi
                                                || this.carte.valoare == Rank.J){

                                            this.esteAtacat = true;
                                        }

                                        break;
                                    }
                                }
                                break;
                            }
                        }

                    }
                    // final actiunea jucatorului cand nu e atacat
                }
                // final mutarea jucatorului
            }
            else{
                // mutarea calculatorului

                try{
                    // delay pentru a nu muta instant
                    // noinspection BusyWait
                    Thread.sleep(3000);
                }
                catch(InterruptedException  e){
                    Thread.currentThread().interrupt();
                    return;
                }

                // actiuni pentru calculator

                if(this.esteAtacat){

                    // verifica daca poate sa contra-atace, apoi daca poate sa opreasca atacul

                    if(this.calculator.HasCounterResponse()){

                        // are carti de cotra-atac

                        int index = this.calculator.GetAttackCardIndex();

                        if(index >= 0){
                            System.out.println("Calculatorul a contra-atacat:");
                            System.out.println(this.calculator.GetCardByIndex(index).toString());
                            System.out.println("Numarul de carti din atac este:" + numarCartiAtac);

                            this.carte = this.calculator.PlayCard(index);

                            this.numarCartiAtac += 2;
                            this.esteAtacat = false;
                        }
                        else{
                            System.out.println("A aparut o eroare!!!");
                        }

                    }
                    else if(this.calculator.HasStopResponse()){

                        // are carti de aparare

                        int index = this.calculator.GetStopCardIndex();

                        if(index >= 0){
                            System.out.println("Calculatorul a oprit atacul:");
                            System.out.println(this.calculator.GetCardByIndex(index).toString());

                            this.carte = this.calculator.PlayCard(index);

                            this.numarCartiAtac = 0;
                            this.esteAtacat = false;
                        }
                        else{
                            System.out.println("A aparut o eroare!!!");
                        }

                    }
                    else{
                        // daca nu are ia carti din pchet

                        for(int i = 0; i < this.numarCartiAtac; i++){
                            this.calculator.AddCard(this.pachet.TakeCard());
                        }

                        this.numarCartiAtac = 0;
                        this.esteAtacat = false;
                    }

                }
                else{

                    if(CannotPlayCard(this.calculator, this.carte)){

                        this.calculator.AddCard(this.pachet.TakeCard());
                        System.out.println("Calculatorul a luat o carte din pachet");
                    }
                    else{
                        for(int i = 0; i < this.calculator.NumberOfCards(); i++){
                            if(IsValidMove(this.calculator.GetCardByIndex(i), this.carte)){

                                System.out.println("Calculatorul a jucat:");
                                System.out.println(this.calculator.GetCardByIndex(i).toString());

                                this.carte = this.calculator.PlayCard(i);
                                break;
                            }
                        }
                    }

                }

            }
            playerTurn = !playerTurn;
            PrintGameStatus();
        }
        System.out.println("Stop Joc");
    }

    public void PrintGameStatus(){
        System.out.println("-------------------------------------");
        System.out.println("Cartea jucata:" + System.lineSeparator() + this.carte.toString());
        System.out.println();
        System.out.println("Cartile tale:" + System.lineSeparator() + this.jucator.toString());
        System.out.println("Calculatorul are " + this.calculator.NumberOfCards() +" carti");
        System.out.println("-------------------------------------");
    }

    public static boolean CannotPlayCard(Player player, Card card){
        for(int i = 0; i < player.NumberOfCards(); i++){
            if(IsValidMove(player.GetCardByIndex(i), card))
                return false;
        }
        return true;
    }

    public void SpecialCard(Card card){
        switch(card.valoare){
            case As: this.playerTurn = HandleAce(this.playerTurn);
                break;
            case Sapte: this.culoare = HandleSeven();
                break;
            default:
                break;
        }
    }
}
