package com.ajeet.docManagement.config;

public class OtpEntry {
    private String otp;
    private long timestamp;

    public OtpEntry(String otp) {
        this.otp = otp;
        this.timestamp = System.currentTimeMillis(); // in milliseconds
    }

    public String getOtp() {
        return otp;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

