package flashcard;

import java.util.List;
import java.util.Comparator;

public class WorstFirstSorter implements CardOrganizer {
    public void organize(List<Card> cards) {
        cards.sort(Comparator.comparingInt(card -> card.correctStreak));
    }
}