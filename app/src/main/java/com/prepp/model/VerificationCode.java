package com.prepp.model;

/**
 * <p/>
 * Project: <b>PREPP</b><br/>
 * Created by: Anand K. Rai on 24/1/16.<br/>
 * Email id: anand.rai@tothenew.com<br/>
 * <p/>
 */
public class VerificationCode {

    private final String activationCode;

    private  String email;

    public VerificationCode(String activationCode, String email){
        this.email=email;
        this.activationCode=activationCode;
    }
}
