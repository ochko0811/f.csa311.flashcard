package flashcard;

import java.util.List;
import java.util.Comparator;

public class RecentMistakesFirstSorter implements CardOrganizer {
    public void organize(List<Card> cards) {
        cards.sort(Comparator.comparingInt(c -> -c.correctStreak));
    }
}