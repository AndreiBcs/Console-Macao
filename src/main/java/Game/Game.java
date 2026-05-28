package Game;

import Entities.Card;
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

        while(!this.pachet.IsEmpty() && !this.jucator.IsEmpty() && !this.calculator.IsEmpty()){

            // un boolean controleaza turele

            if(this.playerTurn){
                // mutarea jucatorului
                // daca in mana nu ai carti compatibile poti doar sa tragi carte

                if(!CanPlayCard(this.jucator, this.carte)){

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
                                    break;
                                }
                            }
                            break;
                        }
                    }

                }

                playerTurn = !playerTurn;
            }
            else{
                // mutarea calculatorului

                try{
                    // delay pentru a nu muta instant

                    Thread.sleep(3000);
                }catch(InterruptedException  e){
                    Thread.currentThread().interrupt();
                    return;
                }

                // actiuni pentru calculator

                if(!CanPlayCard(this.calculator, this.carte)){

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

                playerTurn = !playerTurn;
            }
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

    public static boolean CanPlayCard(Player player, Card card){
        for(int i = 0; i < player.NumberOfCards(); i++){
            if(IsValidMove(player.GetCardByIndex(i), card))
                return true;
        }
        return false;
    }

    public void SpecialCard(Card card){
        switch(card.valoare){
            case As: this.playerTurn = HandleAce(this.playerTurn);
                break;
            case Doi, J: HandleCounterAttack();
                break;
            case Sapte: this.culoare = HandleSeven(this.culoare);
                break;
            case Patru, K: HandleStopper();
                break;
            default:
                break;
        }
    }
}
