package com.example.qr_monster_go;

/**
 * This interface is to allow ScanCodeActivity to
 * easily access the results of a ScanFragment instance
 */
public interface ScanResultReceiver {
    public void scanResultData();
    public void setCurrentLocation();
    public void setQRStrings(String codeFormat, String content);
}
