package com.example.qr_monster_go;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class represents a scanned code object that players can collect
 * On creation this class generates a score and name based on
 * a SHA-256 hash from a scanned codes contents
 */
public class QRCode {
    String code;
    int score;
    String name;
    ArrayList<String> playerList;  //stores the players who have Scanned the code
    String geolocation;  //for now geolocation is represented as a string
    ArrayList<String> commentList;
    byte[] imageMap;

    String visualRep;



    /**
     * @param code
     */
    public QRCode(String code) {
        this.code = code;
        //calculate score in the constructor
        this.score = calculate_score(this.code);
        this.name = generateName(this.code.substring(0, 7)); // only pass in a substring to avoid overflow when converting to binary
        this.playerList = new ArrayList<String>();
        this.geolocation = "";
        this.commentList = new ArrayList<String>();
        this.visualRep = generateVisualRep(this.code);
    }

    /**
     * This function generates a scanned codes score based
     * on its SHA-256 hash
     *
     * @param code
     *      This is an SHA-256 hash of a scanned codes contents
     *
     * @return
     *      Returns the calculated score(int)
     */
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

    /**
     * This function generates a name based on the contents of
     * the SHA-256 hash of a scanned codes contents
     *
     * @param code
     *      This is an SHA-256 hash of a scanned codes contents
     * @return
     *      Returns the generated name(String)
     */
    public String generateName(String code) {
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


    public String generateVisualRep(String code){
        String s1 = "       ____\n" ;
        String s2 = "      /    \\\n";
        String s3 = "    \\|      |/\n";
        String s4 = "    @|  \\ / |@\n";
        String s5 = "    /|  ◕ ◕ |\\\n" ;
        String s6 = "     |   ^  |\n" ;
        String s7 = "     |  \\_/ |\n" ;
        String s8 = "      \\____/\n";


        if(code.charAt(0) == '1'){
            s1 = "       | | |\n";
            s2 = "      ######\n";
        }

        if(code.charAt(1) == '1'){
            s3.replace('\\', ' ' );
            s3.replace('/', ' ');
            s4.replace('@', ' ');
            s5.replace('\\', ' ' );
            s5.replace('/', ' ');

        }

        if(code.charAt(2) == '1'){
            s4.replace('\\', '^');
            s4.replace('/', '^');
        }

        if(code.charAt(3) == '1'){
            s6 = "     |   0) |\n";
        }
        if(code.charAt(4) == '1'){
            s7 = "     |  (==)|\n";
        }

        if(code.charAt(5) == '1'){
            s8 = "     | //// |\n";
        }

        if(code.charAt(6) == '1'){
            s5.replace('◕', 'x');
        }

        return s1 + s2 + s3 + s4 + s5 + s6 + s7 + s8 ;
    }

    
    public void addPlayer(String player) {
        this.playerList.add(player);
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

    public void setGeolocation(String location) {this.geolocation = location;}

    public void setPlayerList(ArrayList<String> newList) {this.playerList = newList;}

    public void setImageMap(byte[] data){this.imageMap = data;}

    public String getName() {
        return name;
    }


    public ArrayList<String> getPlayerList() {
        return playerList;
    }

    public String getGeolocation() { return geolocation; }

    public ArrayList<String> getCommentList() {return commentList;}

    public byte[] getImageMap() {return imageMap;}

}
