package flashcard;

public class Card {
    public String question;
    public String answer;
    public int attempts = 0;
    public int correctStreak = 0;

    public Card(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }
}