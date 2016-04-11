package com.prepp.model;

/**
 * <p/>
 * Project: <b>PREPP</b><br/>
 * Created by: Anand K. Rai on 20/1/16.<br/>
 * Email id: anand.rai@tothenew.com<br/>
 * <p/>
 */
public class VerificationData {

    private String email;

    private String activationCode;

    private String verificationType;

    public VerificationData(String email, String activationCode, String verificationType){
        this.email=email;
        this.activationCode=activationCode;
        this.verificationType=verificationType;
    }
}
