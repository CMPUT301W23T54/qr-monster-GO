package com.example.qr_monster_go;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScannableCode {
    String code;
    int score;
    String name;

    public ScannableCode(String code) {
        this.code = code;
        //calculate score in the constructor
        this.score = calculate_score(this.code);
        this.name = generateName(this.code.substring(0, 2)); // only pass in first 2 characters(all that is needed for name generation)
    }

    public int calculate_score(String code){
        List<String> repeats = getRepeats(code);
        double score = 0;

        for (int i = 0; i < repeats.size(); i++) {
            String str = repeats.get(i);
            char character = str.charAt(0);
            // get the decimal value of the hexadecimal character
            double value = Integer.parseInt(String.valueOf(character), 16);
            double numRepeat = str.length();

            // increase the score by the decimal value to the power of repeated characters(-1)
            score = score + Math.pow(value, numRepeat - 1);

        }

        return (int) score;
    }

    private List<String> getRepeats(String code) {
        List<String> repeats = new ArrayList<>();

        for (int i = 0; i < code.length() - 1; i++) {
            char c = code.charAt(i);

            if (Character.isLetterOrDigit(c)) {
                // calculate the start and finish indexes of repeated characters
                int j = i + 1;
                while (j < code.length() && code.charAt(j) == c) {
                    j++;
                }
                // add the substring from the start and ending indexes of repeats to repeats array
                if (j - i > 1) {
                    repeats.add(code.substring(i, j));
                }

                i = j - 1;
            }
        }

        return repeats;
    }

    private String generateName(String code) {
        // name mapping arrays
        ArrayList<String> names0 = new ArrayList<>(
                Arrays.asList("cool", "Fro", "Mo", "Mega", "Spectral", "Crab"));
        ArrayList<String> names1 = new ArrayList<>(
                Arrays.asList("hot", "Glo", "Lo", "Ultra", "Sonic", "Shark"));

        StringBuilder builder = new StringBuilder();

        // convert code into binary
        int hex = Integer.parseInt(code, 16);
        String binary = Integer.toBinaryString(hex);

        // increment through bits and add name portions to builder accordingly
        for (int i = 0; i < names0.size(); i++) {
            if (binary.charAt(i) == '0') {
                builder.append(names0.get(i));
            }
            else {
                builder.append(names1.get(i));
            }

            // add space after first name portion
            if (i == 0) {
                builder.append(" ");
            }
        }

        return builder.toString();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


    public String getName() {
        return name;
    }

}
