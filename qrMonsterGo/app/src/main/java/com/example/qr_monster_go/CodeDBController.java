package com.example.qr_monster_go;

import java.util.ArrayList;

public class CodeDBController {



    public CodeDBController(QrMonsterGoDB database) {
    }



    public ArrayList<ScannableCode> getPlayerCode(Player player, QrMonsterGoDB db){
        ArrayList<ScannableCode> codes;
        codes = new ArrayList<>();
        CollectionReference codeReference = db.getCollectionReference("Code");
        return codes;
    }

    public ArrayList<ScannableCode> getcodeMap(){
        ArrayList<ScannableCode> codes;

    }



}
