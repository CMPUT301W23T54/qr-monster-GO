package com.example.qr_monster_go;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.hash.Hashing;

import org.checkerframework.checker.units.qual.C;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class QRCodeTest {
    @Test
    public void testCalculateScore() {
        String content = "BFG5DGW54";
        String hashValue = Hashing.sha256()
                .hashString(content, StandardCharsets.UTF_8)
                .toString();

        QRCode code = new QRCode(hashValue);
        int score = code.getScore();
        assertEquals(code.calculate_score(hashValue), score);
        assertEquals(19, score);

        code = new QRCode("696ce4dbd7bb57cbfe58b64f530f428b74999cb37e2ee60980490cd9552de3a6");
        assertEquals(111, code.getScore());
    }

    @Test
    public void testGenerateName() {
        String content = "BFG5DGW54";
        String hashValue = Hashing.sha256()
                .hashString(content, StandardCharsets.UTF_8)
                .toString();

        QRCode code = new QRCode(hashValue);
        String name = code.getName();
        assertEquals(name, code.generateName(hashValue.substring(0, 2)));
        assertEquals("hot FroMoMegaSpectralCrab", code.getName());
    }



}
