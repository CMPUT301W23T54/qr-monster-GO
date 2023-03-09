package com.example.qr_monster_go;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScannableCode {

    String code;
    int score;

    public ScannableCode(String code) {
        this.code = code;
        //calculate score in the constructor
        this.score = calculate_score(this.code);
    }

    public int calculate_score(String code){
        List<String> repeats = getRepeats(code);
        Log.d(String.valueOf(repeats), "calculate_score: ");
        double score = 0;

        for (int i = 0; i < repeats.size(); i++) {
            String str = repeats.get(i);
            char character = str.charAt(0);
            double value = Integer.parseInt(String.valueOf(character), 16);
            double numRepeat = str.length();

            score = score + Math.pow(value, numRepeat - 1);

        }

        return (int) score;
    }

    private List<String> getRepeats(String code) {
        List<String> repeats = new ArrayList<>();
        for (int i = 0; i < code.length() - 1; i++) {
            char c = code.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                int j = i + 1;
                while (j < code.length() && code.charAt(j) == c) {
                    j++;
                }
                if (j - i > 1) {
                    repeats.add(code.substring(i, j));
                }
                i = j - 1;
            }
        }
        Log.d(String.valueOf(repeats), "getRepeats: ");
        return repeats;
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





}
