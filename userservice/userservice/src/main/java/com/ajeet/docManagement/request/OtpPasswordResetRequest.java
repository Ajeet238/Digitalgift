package com.ajeet.docManagement.request;

import lombok.Data;

@Data
public class OtpPasswordResetRequest {
    private String phone;
    private String otp;
    private String newPassword;
    private String email;
}
