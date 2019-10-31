package hangman;

import java.util.*;
import java.io.*;
import java.nio.CharBuffer;

/**
 * Data Structures Project: Cheater's Hangman
 * @author Shiv
 */
public class Hangman {

    public static void main(String[] args) throws FileNotFoundException {

        boolean game = true;

        while (game == true) {
            // Get Game Info From User
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter a word length to guess: ");
            int wordLength = sc.nextInt();
            System.out.println("How many guesses do you want? ");
            int maxGuess = sc.nextInt();

            // Read Words From Text File
            Scanner sc2 = new Scanner(new File("/Users/Icey/Desktop/hangman/words.txt"));
            List<String> words = new ArrayList<String>();
            while (sc2.hasNext()) {
                words.add(sc2.next().toLowerCase());
                //System.out.println(words);
            }

            // if word length is not valid, enter a new length to guess.
            while (checkIfValid(words, wordLength) != true) {
                System.out.println("No words of length " + wordLength + " found. Enter a new length to guess: ");
                wordLength = sc.nextInt();
            }

            // if num of guesses is invalid, enter a new length to guess.
            while (maxGuess < 1) {
                maxGuess = sc.nextInt();
            }

            String InitialWord = (GenerateWord(words, wordLength));
            
            // SECRET WORDS OF SAME WORD LENGTH
            HashMap secretwords = new HashMap();   
            WordOptions(words,wordLength,secretwords);
            //System.out.println(secretwords);

            // START THE GAME
            playHangman(words, wordLength, maxGuess, InitialWord, secretwords);

            // Ask User if They Want to Play Again
            System.out.println("Would you like to play again? Type 'Y' for Yes or 'N' for No");
            String PlayAgain = sc.next();
            if (PlayAgain == "Y") {
                playHangman(words, wordLength, maxGuess, InitialWord, secretwords);
            } else {
                System.out.println("Thanks for playing...loser (:");
            }
            break;
        }
    }

    // Given a list of String (the text file) & the user entered length to guess,
    // find if word length user entered is valid.
    public static boolean checkIfValid(List<String> list, int length) {
        for (String S : list) {
            if (S.length() == length) {
                //System.out.println(S + " is " + length + " letters long! VALID");
                return true;
            }
        }
        return false;
    }

    // Generates & Returns a Random Word of Length User Entered.
    public static String GenerateWord(List<String> list, int length) {
        List<String> words2 = new ArrayList<String>();
        for (String S : list) {
            if (S.length() == length) {
                words2.add(S);
            }
        }

        Random rand = new Random();
        int i = rand.nextInt(words2.size());
        System.out.println("The initial word is: " + words2.get(i));

        return (words2.get(i));
    }

    // The Actual Game of Hangman
    public static void playHangman(List<String> list, int length, int guess, String word, HashMap<Integer, String> hmap) {
        Scanner S = new Scanner(System.in);
        List<Character> CorrectGuesses = new ArrayList<Character>();
        List<Character> IncorrectGuesses = new ArrayList<Character>();

        int turnNumber = 1;

        // while guess turns is not 0, keep guessing!
        while (guess != 0) {
            System.out.println();
            System.out.println("Guess a letter!");
            char letter = S.next().charAt(0);
            letter = Character.toLowerCase(letter);
            System.out.println();
            CharBuffer.wrap(new char[]{letter});

            if (AlreadyGuessed(CorrectGuesses, IncorrectGuesses, letter, turnNumber) == true) {
                while (AlreadyGuessed(CorrectGuesses, IncorrectGuesses, letter, turnNumber) == true) {
                    System.out.println("You already guessed this letter, enter a new letter: ");
                    letter = S.next().charAt(0);
                }
            }

            if (word.contains(CharBuffer.wrap(new char[]{letter}))) {
                //System.out.println("The word contains " + letter);
                CorrectGuesses.add(letter);
                System.out.println("Correct Guesses: " + CorrectGuesses);
                System.out.println("Incorrect Guesses: " + IncorrectGuesses);
                System.out.println("You have " + guess + " incorrect guesses remaining.");

            } else {
                //System.out.println("Sorry, the word does not contain '" + letter +"'");
                IncorrectGuesses.add(letter);
                guess--;
                System.out.println("Correct Guesses: " + CorrectGuesses);
                System.out.println("Incorrect Guesses: " + IncorrectGuesses);
                System.out.println("You have " + guess + " guesses remaining.");
            }

            System.out.println();

            if (word.equals(GenerateString(word, CorrectGuesses, turnNumber))) {
                System.out.println();
                System.out.println("Congrats! You Win.");
                break;
            }

            if (guess == 0) {
                System.out.println();
                System.out.println("YOU LOST HAHAHAHAHA, SO SAD. The word was: " + word);
                break;
            }

            turnNumber++;
        }

    }

    // creates a string of word for user
    public static String GenerateString(String word, List<Character> correct, int turnNum) {

        String hidden = word;
        //System.out.println(word);

        hidden = hidden.replaceAll("[a-zA-Z]", "□");

        if (turnNum == 1) {
            System.out.println("The hidden word is: " + hidden);
        }

        StringBuilder sb = new StringBuilder(hidden);

        for (int i = 0; i <= word.length() - 1; i++) {
            for (int j = 0; j <= correct.size() - 1; j++) {
                if (word.charAt(i) == correct.get(j)) {
                    sb.setCharAt(i, correct.get(j));
                }
            }
        }

        System.out.print("You word you have guessed so far: " + sb);

        return sb.toString();
    }

    // Checks if User Already Entered a Letter
    public static boolean AlreadyGuessed(List<Character> correct, List<Character> wrong, char letter, int turnNum) {
        // correct/wrong lists wouldn't contain a letter yet during the first round
        if (turnNum == 1) {
            return false;
        }

        for (int i = 0; i < correct.size() - 1; i++) {
            if (correct.get(i) == letter) {
                return true;
            }
        }

        for (int i = 0; i < wrong.size() - 1; i++) {
            if (wrong.get(i) == letter) {
                return true;
            }
        }

        // if user has yet to guessed letter
        return false;
    }

    // These are the words the program can potentially cheat with.
    public static void WordOptions(List <String> list, int num, HashMap<Integer, String> hmap) {
        for (int i = 0; i < list.size()-1; i++) {
            if (list.get(i).length() == num) {
                hmap.put(i, list.get(i)); // all words with the same length as user entered
                //System.out.println(S);
            }
        }
    }

}
