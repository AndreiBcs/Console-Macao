package Game;

import Entities.Card;
import Entities.Common.Suit;
import Entities.Player;

import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Interactions {
    public static boolean IsValidMove(Card card1, Card card2){
        return card1.culoare.equals(card2.culoare)
                || card1.valoare.equals(card2.valoare);
    }

    public static boolean HandleAce(boolean turn) {
        System.out.println("A fost jucat un As, adversarul sta o tura");
        return !turn;
    }

    public static Suit HandlePlayerSeven() {
        System.out.println("Ai jucat un Sapte, poti schimba culoarea");
        System.out.println("Apasa numarul culorii in care vrei sa schimbi");
        System.out.println("0 - Romb" + System.lineSeparator() +
                            "1 - Rosu" + System.lineSeparator() +
                            "2 - Negru" + System.lineSeparator() +
                            "3 - Trefla");
        Suit suit = null;

        while (true) {
            var sc = new Scanner(System.in);
            int index = sc.nextInt();

            if(index >= 0 && index < 4){

                suit = switch (index) {
                    case 0 -> Suit.Romb;
                    case 1 -> Suit.Rosu;
                    case 2 -> Suit.Negru;
                    case 3 -> Suit.Trefla;
                    default -> suit;
                };

                break;
            }
        }

        return suit;
    }

    public static Suit HandleCalculatorSeven(Player player){

        return player.mana.stream()
                .collect(Collectors.groupingBy(card -> card.culoare, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

}
