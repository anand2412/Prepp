package com.prepp.model;

/**
 * <p/>
 * Project: <b>PREPP</b><br/>
 * Created by: Anand K. Rai on 20/1/16.<br/>
 * Email id: anand.rai@tothenew.com<br/>
 * <p/>
 */
public class UserData {

    private String branchCode;

    private String email;

    private String mobileNumber;

    private String verificationType;

    private boolean socialFlag;

    private String name;

    public UserData(String branchCode, String email, String verificationType, String name, String mobileNumber,boolean socialFlag){

        this.branchCode=branchCode;
        this.email=email;
        this.mobileNumber=mobileNumber;
        this.name=name;
        this.socialFlag=socialFlag;
        this.verificationType=verificationType;
    }

    public UserData(String email){
        this.email=email;

    }
}
