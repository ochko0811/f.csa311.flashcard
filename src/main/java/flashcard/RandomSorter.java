package flashcard;

import java.util.Collections;
import java.util.List;

public class RandomSorter implements CardOrganizer {
    public void organize(List<Card> cards) {
        Collections.shuffle(cards);
    }
}