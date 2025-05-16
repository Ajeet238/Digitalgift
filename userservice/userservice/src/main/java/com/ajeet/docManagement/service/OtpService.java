package com.ajeet.docManagement.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ajeet.docManagement.config.OtpEntry;

import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private final Map<String, OtpEntry> otpStore = new ConcurrentHashMap<>();
    private static final long EXPIRY_DURATION = 5 * 60 * 1000; // 5 minutes in ms

    public String sendOtp(String phone) {
        String otp = String.valueOf(new Random().nextInt(9000) + 1000);
        otpStore.put(phone, new OtpEntry(otp));
        
        System.out.println("OTP for " + phone + " is " + otp); 
        return otp;
    }

    public boolean verifyOtp(String phone, String otp) {
        OtpEntry entry = otpStore.get(phone);
        if (entry == null) return false;

        long now = System.currentTimeMillis();
        if (now - entry.getTimestamp() > EXPIRY_DURATION) {
            otpStore.remove(phone);
            return false; // expired
        }
        return otp.equals(entry.getOtp());
    }

    // Cleanup expired OTPs every minute
    @Scheduled(fixedRate = 60 * 1000)
    public void cleanExpiredOtps() {
        long now = System.currentTimeMillis();
        otpStore.entrySet().removeIf(e -> now - e.getValue().getTimestamp() > EXPIRY_DURATION);
        System.out.println("Expired OTPs cleaned at: " + Instant.now());
    }
}
