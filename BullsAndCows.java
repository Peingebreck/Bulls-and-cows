package bullscows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Random;

public class BullsAndCows {
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    static Random random = new Random();

    public static void main(String[] args) throws IOException {
        String secretCode = generateRandomCode();
        if (secretCode.length() > 0) {
            playGame(secretCode);
        }
    }

    static boolean checkInputFormat(String number) {
        if (!number.matches("\\d+") || Integer.parseInt(number) <= 0) {
            System.out.printf("Error: \"%s\" isn't a valid number.", number);
            return false;
        } else {
            return true;
        }
    }

    static String generateRandomCode() throws IOException {
        StringBuilder secretCode = new StringBuilder();
        System.out.println("Input the length of the secret code:");
        String strCodeLength = reader.readLine();
        if (checkInputFormat(strCodeLength)) {
            int codeLength = Integer.parseInt(strCodeLength);
            if (codeLength > 36) {
                System.out.printf("Error: can't generate a secret number with a length of %d because there aren't enough unique symbols.", codeLength);
            } else {
                System.out.println("Input the number of possible symbols in the code:");
                String strNumOfSymbols = reader.readLine();
                if (checkInputFormat(strNumOfSymbols)) {
                    int numOfSymbols = Integer.parseInt(strNumOfSymbols);
                    if (numOfSymbols < codeLength) {
                        System.out.printf("Error: it's not possible to generate a code with a length of %d with %d unique symbols.", codeLength, numOfSymbols);
                    } else if (numOfSymbols > 36) {
                        System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
                    } else {
                        while (secretCode.length() < codeLength) {
                            if (numOfSymbols <= 10) {
                                int randomNum = random.nextInt(numOfSymbols);
                                if (secretCode.indexOf(String.valueOf(randomNum)) == -1) {
                                    secretCode.append(randomNum);
                                }
                            } else {
                                char randomNum = (char) (random.nextInt(numOfSymbols + 39) + 48);
                                if (secretCode.indexOf(String.valueOf(randomNum)) == -1 &&
                                        (randomNum <= 57 || randomNum >= 97)) {
                                    secretCode.append(randomNum);
                                }
                            }
                        }
                        String bound = numOfSymbols <= 10 ? String.format("(0-%d)", numOfSymbols - 1) :
                                String.format("(0-9, a-%c)", (char) numOfSymbols + 86);
                        System.out.printf("The secret is prepared: %s %s.\n", "*".repeat(secretCode.length()), bound);
                        System.out.println("Okay, let's start a game!");
                    }
                }
            }
        }
        return String.valueOf(secretCode);
    }

    static void playGame(String secretCode) throws IOException {
        int turn = 1;
        String result;
        while (true) {
            System.out.printf("Turn %d:\n", turn);
            String guess = reader.readLine();
            if (guess.length() != secretCode.length()) {
                System.out.printf("Secret code length should be %d\n", secretCode.length());
                turn++;
            } else {
                int[] grade = countBullsAndCows(guess, secretCode);
                if (grade[0] == 0 && grade[1] == 0) {
                    result = "None";
                } else if (grade[0] == 0) {
                    result = String.format("%d cow%s", grade[1], grade[1] == 1 ? "" : "s");
                } else if (grade[1] == 0) {
                    result = String.format("%d bull%s", grade[0], grade[0] == 1 ? "" : "s");
                } else {
                    result = String.format("%d bull%s and %d cow%s",
                            grade[0], grade[0] == 1 ? "" : "s", grade[1], grade[1] == 1 ? "" : "s");
                }
                System.out.printf("Grade: %s\n", result);
                if (grade[0] == secretCode.length()) {
                    System.out.println("Congratulations! You guessed the secret code.");
                    break;
                } else {
                    turn++;
                }
            }
        }
    }

    static int[] countBullsAndCows(String guess, String secretCode) {
        int[] grade = {0, 0};
        for (int i = 0; i < secretCode.length(); i++) {
            if (guess.charAt(i) == secretCode.charAt(i)) {
                grade[0]++;
            } else if (secretCode.contains(Character.toString(guess.charAt(i)))){
                grade[1]++;
            }
        }
        return grade;
    }
}
