package flashcard;

import java.util.*;

public class FlashcardApp {
    public static void main(String[] args) {
        if (args.length == 0 || Arrays.asList(args).contains("--help")) {
            printHelp();
            return;
        }
        String fileName = args[0];
        boolean invert = Arrays.asList(args).contains("--invertCards");
        int repetitions = getIntOption(args, "--repetitions", 1);
        String order = getOption(args, "--order", "random");

        Map<String, String> cards = loadCards(fileName);
        List<Card> cardList = new ArrayList<>();
        for (Map.Entry<String, String> entry : cards.entrySet()) {
            cardList.add(new Card(entry.getKey(), entry.getValue()));
        }

        CardOrganizer organizer = getOrganizer(order);
        organizer.organize(cardList);

        try (Scanner scanner = new Scanner(System.in)) {

            long start = System.currentTimeMillis();
            boolean allCorrect = true;

            for (Card card : cardList) {
                int correctCount = 0;
                while (correctCount < repetitions) {
                    System.out.print((invert ? card.answer : card.question) + " -> ");
                    String answer = scanner.nextLine();
                    card.attempts++;
                    if ((invert ? card.question : card.answer).equalsIgnoreCase(answer)) {
                        System.out.println("Correct!");
                        correctCount++;
                        card.correctStreak++;
                    } else {
                        System.out.println("Wrong. Try again.");
                        allCorrect = false;
                        card.correctStreak = 0;
                    }
                }
                if (card.attempts > 5)
                    System.out.println("Achievement: REPEAT on " + card.question);
                if (card.correctStreak >= 3)
                    System.out.println("Achievement: CONFIDENT on " + card.question);
            }

            long totalTime = System.currentTimeMillis() - start;
            if (allCorrect)
                System.out.println("Achievement: CORRECT");
            if (cardList.size() > 0 && totalTime / cardList.size() < 5000)
                System.out.println("Bonus: Fast response");
        }
    }

    private static void printHelp() {
        System.out.println("Usage: flashcard <cards-file> [options]");
        System.out.println("Options:");
        System.out.println("  --help                     Show help information");
        System.out.println("  --order <order>           Card order: random, worst-first, recent-mistakes-first");
        System.out.println("  --repetitions <num>       Number of times a card must be answered correctly");
        System.out.println("  --invertCards             Swap question and answer display");
    }

    private static Map<String, String> loadCards(String fileName) {
        Map<String, String> cards = new LinkedHashMap<>();
        try (Scanner scanner = new Scanner(new java.io.File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("::");
                if (parts.length == 2) {
                    cards.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to read cards: " + e.getMessage());
            System.exit(1);
        }
        return cards;
    }

    private static String getOption(String[] args, String name, String defaultVal) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals(name))
                return args[i + 1];
        }
        return defaultVal;
    }

    private static int getIntOption(String[] args, String name, int defaultVal) {
        try {
            return Integer.parseInt(getOption(args, name, String.valueOf(defaultVal)));
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    private static CardOrganizer getOrganizer(String order) {
        switch (order) {
            case "worst-first":
                return new WorstFirstSorter();
            case "recent-mistakes-first":
                return new RecentMistakesFirstSorter();
            default:
                return new RandomSorter();
        }
    }
}