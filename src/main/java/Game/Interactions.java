package Game;

import Entities.Card;
import Entities.Common.Suit;

import java.util.Scanner;

public class Interactions {
    public static boolean IsValidMove(Card card1, Card card2){
        return card1.culoare.equals(card2.culoare) || card1.valoare.equals(card2.valoare);
    }

    public static boolean HandleAce(boolean turn) {
        System.out.println("A fost jucat un As, adversarul sta o tura");
        return !turn;
    }

    public static String HandleSeven() {
        System.out.println("Ai jucat un Sapte, poti schimba culoarea");
        System.out.println("Apasa numarul culorii in care vrei sa schimbi");
        System.out.println("0 - Romb" + System.lineSeparator() +
                            "1 - Rosu" + System.lineSeparator() +
                            "2 - Negru" + System.lineSeparator() +
                            "3 - Trefla");
        String suit = "";

        while (true) {
            var sc = new Scanner(System.in);
            int index = sc.nextInt();

            if(index >= 0 && index < 4){

                suit = switch (index) {
                    case 0 -> Suit.Romb.name();
                    case 1 -> Suit.Rosu.name();
                    case 2 -> Suit.Negru.name();
                    case 3 -> Suit.Trefla.name();
                    default -> suit;
                };

                break;
            }
        }

        return suit;
    }

}
