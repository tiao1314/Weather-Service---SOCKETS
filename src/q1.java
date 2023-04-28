import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class q1 {

    static List<Player> highScores = new LinkedList<>();
    static File highScoresFile = new File("highscores.txt");

    public static void main(String[] args) {
        // running the 3 methods
        loadHighScores();
        saveHighScores();
        printHighScores();

    }

    static void loadHighScores() {
        try (Scanner scanner = new Scanner(highScoresFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String name = parts[0];
                int score = Integer.parseInt(parts[1]);
                highScores.add(new Player(name, score));
            }
        } catch (IOException e) {
            System.err.println("Error loading high scores: " + e.getMessage());
        }
    }

    static void saveHighScores() {
        try (FileWriter writer = new FileWriter(highScoresFile)) {
            for (Player player : highScores) {
                writer.write(player.name + "," + player.score + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving high scores: " + e.getMessage());
        }
    }

    static void printHighScores() {
        Collections.sort(highScores, Comparator.comparing(Player::getScore).reversed());

        System.out.println("High Scores:");
        for (Player player : highScores) {
            System.out.println(player.name + ": " + player.score);
        }
    }

    static class Player {
        String name;
        int score;

        public Player(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public int getScore() {
            return score;
        }
    }
}
