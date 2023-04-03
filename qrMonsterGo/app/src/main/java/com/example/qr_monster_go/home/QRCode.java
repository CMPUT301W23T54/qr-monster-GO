package com.example.qr_monster_go.home;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class represents a scanned code object that players can collect
 * On creation this class generates a score and name based on
 * a SHA-256 hash from a scanned codes contents
 */
public class QRCode {
    public String code;
    public int score;
    String name;
    ArrayList<String> playerList;  //stores the players who have Scanned the code
    String geolocation;  //for now geolocation is represented as a string
    ArrayList<String> commentList;
    byte[] imageMap;

    String visualRep;



    /**
     * @param code - the hash String of the QRCode
     *
     * This is the QRCode constructor. It takes in the hash String as an identifier of the
     * QRCode and construct a QRCode object.
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

    /**
     * @param code - the hash String of a QRCode
     * @return - the List<String>  object that represent the repeat in code
     *
     * This method takes in a hash String of QRCode object, and
     * assists calculate_score method in calculating the score of a QRCode.
     */
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


    /**
     * @param code - the hash String of the code
     * @return - the String visual representation of the code
     *
     *   This is a method that takes in a the hash String of the QRCode
     *   and returns the String visual representation of that code.
     */
    public String generateVisualRep(String code){
        ArrayList<String> headVisual = new ArrayList<>();
        ArrayList<String> eyebrowsVisual = new ArrayList<>();
        ArrayList<String> eyesVisual = new ArrayList<>();
        ArrayList<String> chinVisual = new ArrayList<>();
        Collections.addAll(headVisual, "       ____\n", "       ||||\n", "       ^^^^\n", "       !_!_\n", "       @#@#\n", "       ****\n", "       <><>\n", "       &&&&\n", "       ~~~~\n");
        Collections.addAll(eyebrowsVisual,"   @|  \\ / |@\n", "  @|~  ~|@\n" );
        Collections.addAll(eyesVisual,"    /|  ◕ ◕  |\\\n", "    /|  - -   |\\\n", "    /|  > <  |\\\n", "    /|  # #  |\\\n" );
        Collections.addAll(chinVisual,"     \\%%%%/\n", "     \\@!@!/\n", "     \\&&&&/\n", "     \\~~~~/\n" );
        String s1 = "";
        String s2 = "      /      \\\n";
        String s3 = "    \\|        |/\n";
        String s4 = "";
        String s5 = "" ;
        String s6 = "     |   ^    |\n" ;
        String s7 = "     |  \\_/  |\n" ;
        String s8 = "     \\____/\n";
        int i = 0;
        int y = 0;
        String now = "";
        while (i < 32){
            now += code.charAt(i);
            if (now.length() == 4){
                int on = Integer.parseInt(now, 16);
                now = "";
                if(y == 0){
                    s1 = headVisual.get(on % 10);
                }
                if (y == 3){
                    s4 = eyebrowsVisual.get(on % 2);
                }
                if(y == 4){
                    s5 = eyesVisual.get(on % 4);
                }
                if (y == 7){
                    s8 = chinVisual.get(on % 4);
                }
                y += 1;
            }
            i+=1;

        }

        return s1 + s2 + s3 + s4 + s5 + s6 + s7 + s8;
    }


    /**
     * @param player - a String of Player's username
     *
     * This method takes in a String of a player and then add it to the
     * QRCode playerList.
     */
    public void addPlayer(String player) {
        this.playerList.add(player);
    }

    /**
     * @return - the hash String of a QRCode object
     *
     * This method returns the hash String of a QRCode object.
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code - the hash String of the QR code
     *
     * This method takes in a hash String and assign to the QRCode object.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return - the Score of the code
     *
     * This method returns the score of the QRCode.
     */
    public int getScore() {
        return score;
    }

    /**
     * @param score - the int score that needs to be set
     *
     * This method takes in an int and assign to the QRCode's score.
     */
    public void setScore(int score) {
        this.score = score;
    }


    /**
     * @param location - a String representing the geolocation
     *
     * This method takes in a String representing the geolocation of the object
     * and then assign to the QRCode geolocation field.
     */
    public void setGeolocation(String location) {this.geolocation = location;}

    /**
     * @param newList - the ArrayList<String> object representing the new list that need to be assigned
     *
     * This method takes in an ArrayList<String> object and assign it to playerlist field
     * of the QRCode.
     */
    public void setPlayerList(ArrayList<String> newList) {this.playerList = newList;}

    /**
     * @param data - a byte[] that store the image of the QRCode
     *
     * This method takes in an byte[] object that assign it to the imageMap
     * field of the QRCode.
     */
    public void setImageMap(byte[] data){this.imageMap = data;}

    /**
     * @return - the String name of the QRCode
     *
     * This method returns the String name of a QRCode object.
     */
    public String getName() {
        return name;
    }


    /**
     * @return - an ArrayList<String> that represent the playerList of the QRCode.
     *
     * This method returns the playerList of the QRCode.
     */
    public ArrayList<String> getPlayerList() {
        return playerList;
    }

    /**
     * @return - the String of geolocation of the QRCode
     *
     * This method returns the String Geolocation of the QRCode object.
     */
    public String getGeolocation() { return geolocation; }


    /**
     * @return - the ArrayList<String> representing commentList of QRCode object
     *
     * This method returns the CommentList of the QRCode object.
     */
    public ArrayList<String> getCommentList() {return commentList;}

    /**
     * @return - the String of imageMap of the QRCode
     *
     * This method returns the String imageMap of the QRCode object.
     */
    public byte[] getImageMap() {return imageMap;}

}
