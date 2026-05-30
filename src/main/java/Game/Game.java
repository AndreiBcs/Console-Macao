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
    boolean esteAtacat = false;
    int numarCartiAtac = 2;

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

        while(this.jucator.NotEmpty() && this.calculator.NotEmpty()){

            if(this.playerTurn){
                // mutarea jucatorului

                if(this.esteAtacat){

                    // actiuniile jucatorului cand este atacat

                    if(this.jucator.HasStopResponse() || this.jucator.HasCounterResponse()){

                        // are carti de counter-attack sau stop
                        HandlePlayerAttackMove();
                    }
                    else {
                        // nu are carti de contra, nici de stop
                        HandlePlayerLosingAttack();
                    }
                }
                else{
                    // actiuniile jucatorului cand nu e atacat

                    if(CannotPlayCard(this.jucator, this.carte)){

                        // daca nu poate juca nicio carte asteapta input-ul jucatorului
                        // pentru a lua o carte din pachet

                        System.out.println("'t' - trage carte");

                        char c = input.next().charAt(0);

                        while(true){
                            if(c == 't'){
                                this.jucator.AddCard(this.pachet.TakeCard());
                                System.out.println("Ai luat o carte din pachet");
                                break;
                            }
                        }
                    }
                    else{
                        // daca poate juca o carte asteapta input-ul jucatorului

                        HandlePlayerNormalMove();
                    }
                }
            }
            else{
                // mutarea calculatorului
                CalculatorMoveDelay();

                if(this.esteAtacat){

                    // daca este atacat 1. verifica daca are counter-attack
                    //                  2. verifica daca poate opri atacul
                    //                  3. ia cartile daca pierde atacul

                    HandleCalculatorAttackMove();

                }
                else{
                    HandleCalculatorNormalMove();
                }

            }
            playerTurn = !playerTurn;
            PrintGameStatus();
            CheckGameStatus();
        }
        System.out.println("Stop Joc");
    }

    void PrintGameStatus(){
        System.out.println("-----/-----/-----/-----/-----/-----/-----/-----/-----/--");
        System.out.println("----/-----/-----/-----/-----/-----/-----/-----/-----/---");
        System.out.println("---/-----/-----/-----/-----/-----/-----/-----/-----/----");
        System.out.println("--/-----/-----/-----/-----/-----/-----/-----/-----/-----");
        System.out.println("Cartea jucata:" + System.lineSeparator() + this.carte.toString());
        System.out.println();
        System.out.println("Cartile tale:" + System.lineSeparator() + this.jucator.toString());
        System.out.println("Calculatorul are " + this.calculator.NumberOfCards() +" carti");
        System.out.println("-----/-----/-----/-----/-----/-----/-----/-----/-----/--");
        System.out.println("----/-----/-----/-----/-----/-----/-----/-----/-----/---");
        System.out.println("---/-----/-----/-----/-----/-----/-----/-----/-----/----");
        System.out.println("--/-----/-----/-----/-----/-----/-----/-----/-----/-----");
    }

    void CheckGameStatus(){

        // verifica daca jucatorul / calculatorul a ramas fara carti

        if(this.jucator.NumberOfCards() == 0){
            System.out.println("Ai castigat!");
        }

        if(this.calculator.NumberOfCards() == 0){
            System.out.println("Calculatorul a castigat!");
        }

        // daca pachetul e gol se initializeaza altul si se scot din el
        // cartile care inca sunt in uz

        if(this.pachet.IsEmpty()){
            this.pachet = new Deck();
            this.pachet.InitialiseDeck();

            this.pachet.RemoveCard(this.carte);

            this.jucator.mana.forEach(card -> this.pachet.RemoveCard(card));
            this.calculator.mana.forEach(card -> this.pachet.RemoveCard(card));

            this.pachet.Shuffle();
        }
    }

    static boolean CannotPlayCard(Player player, Card card){
        for(int i = 0; i < player.NumberOfCards(); i++){
            if(IsValidMove(player.GetCardByIndex(i), card))
                return false;
        }
        return true;
    }

    void HandleSpecialCard(Card card, Player player){
        switch(card.valoare){
            case As: this.playerTurn = HandleAce(this.playerTurn);
                break;
            case Sapte: {
                    if(player == this.jucator){
                        this.carte.culoare = HandlePlayerSeven();

                    }
                    else if(player == this.calculator){
                        this.carte.culoare = HandleCalculatorSeven(this.calculator);
                    }
                System.out.println("Culoarea cartii jucate a fost schimbata in "
                        + this.carte.culoare.toString().toUpperCase());
                }
                break;
            default:
                break;
        }
    }

    void CalculatorMoveDelay(){

        try{
            // delay pentru a nu muta instant
            Thread.sleep(3000);
        }
        catch(InterruptedException  e){
            Thread.currentThread().interrupt();
        }

    }

    void HandleCalculatorNormalMove(){
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

                    HandleSpecialCard(this.carte, this.calculator);

                    if((this.carte.valoare == Rank.J)
                            || (this.carte.valoare == Rank.Doi)){

                        this.esteAtacat = true;
                    }

                    break;
                }
            }
        }
    }

    void HandleCalculatorAttackMove(){

        int index;

        if(this.calculator.HasCounterResponse()){

            index = this.calculator.GetAttackCardIndex();

            System.out.println("Calculatorul a contra-atacat:");
            System.out.println(this.calculator.GetCardByIndex(index).toString());

            this.numarCartiAtac += 2;
            System.out.println("Numarul de carti din atac este: " + numarCartiAtac);

            this.carte = this.calculator.PlayCard(index);
        }
        else if(this.calculator.HasStopResponse()){

            index = this.calculator.GetStopCardIndex();

            System.out.println("Calculatorul a oprit atacul:");
            System.out.println(this.calculator.GetCardByIndex(index).toString());

            this.numarCartiAtac = 2;
            this.esteAtacat = false;

            this.carte = this.calculator.PlayCard(index);
        }
        else{
            // ia carti din pachet

            for(int i = 0; i < this.numarCartiAtac; i++){
                this.calculator.AddCard(this.pachet.TakeCard());
            }
            System.out.println("Calculatorul a luat " + this.numarCartiAtac
                                + " carti din pachet");

            this.numarCartiAtac = 2;
            this.esteAtacat = false;
        }

    }

    void HandlePlayerNormalMove(){

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

                        HandleSpecialCard(this.carte, this.jucator);

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

    void HandlePlayerLosingAttack(){

        System.out.println("Nu poti opri atacul, trebuie sa tragi "
                + this.numarCartiAtac + " carti");
        System.out.println("'t' - trage carti");

        char c = input.next().charAt(0);

        while(true){
            if(c == 't'){
                for(int i = 0; i < this.numarCartiAtac; i++){
                    this.jucator.AddCard(this.pachet.TakeCard());
                }

                System.out.println("Ai luat " + this.numarCartiAtac + " din pachet");
                break;
            }
        }

        this.numarCartiAtac = 2;
        this.esteAtacat = false;
    }

    void HandlePlayerAttackMove(){

        System.out.println("Esti atacat!");
        System.out.println("Joaca Doi sau J pentru contra-atac");
        System.out.println("Joaca Patru sau K pentru a opri atacul");
        System.out.println();
        System.out.println("'j' - joaca carte");
        System.out.println("'t' - trage carte");

        char c = input.next().charAt(0);

        // actiuni pentru jucator

        while(true){
            if(c == 't'){
                for(int i = 0; i < this.numarCartiAtac; i++){
                    this.jucator.AddCard(this.pachet.TakeCard());
                }

                System.out.println("Ai luat " + this.numarCartiAtac
                                    + " carti din pachet");

                this.numarCartiAtac = 2;
                this.esteAtacat = false;
                break;
            }
            if(c == 'j'){
                System.out.println("Apasa numarul cartii pe care vrei sa o joci");
                while(true){
                    int index = input.nextInt();

                    // joaca o carte de contra-atac

                    if ((index >= 0 && index < this.jucator.NumberOfCards()) &&
                            ((this.jucator.GetCardByIndex(index).valoare == Rank.Doi) ||
                                    (this.jucator.GetCardByIndex(index).valoare == Rank.J))) {

                        System.out.println("Ai contra-atacat:");
                        System.out.println(this.jucator.GetCardByIndex(index).toString());

                        this.carte = this.jucator.PlayCard(index);

                        this.numarCartiAtac += 2;
                        System.out.println("Numarul de carti din atac este: " + numarCartiAtac);

                        break;
                    }

                    // joaca o carte de stop

                    if ((index >= 0 && index < this.jucator.NumberOfCards()) &&
                            ((this.jucator.GetCardByIndex(index).valoare == Rank.Patru) ||
                                    (this.jucator.GetCardByIndex(index).valoare == Rank.K))) {

                        System.out.println("Ai oprit atacul:");
                        System.out.println(this.jucator.GetCardByIndex(index).toString());

                        this.carte = this.jucator.PlayCard(index);
                        this.numarCartiAtac = 2;
                        this.esteAtacat = false;
                        break;
                    }
                }
                break;
            }
        }
    }
}
