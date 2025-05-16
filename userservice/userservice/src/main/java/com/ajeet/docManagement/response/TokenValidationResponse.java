package com.ajeet.docManagement.response;

public class TokenValidationResponse {
    private boolean valid;
    private String message;
    

    public TokenValidationResponse(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
       
    }

    // Getters and setters
    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}


