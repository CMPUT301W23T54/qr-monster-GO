package com.example.qr_monster_go;

public class ScannableCode {

    String code;
    int score;

    public ScannableCode(String code) {
        this.code = code;
        //calculate score in the constructor
        score = calculate_score(code);
    }

    public int calculate_score(String code){
        //function
        return 0;
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
