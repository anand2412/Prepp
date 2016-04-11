package com.prepp.model;

import com.google.gson.annotations.SerializedName;

/**
 * <p/>
 * Project: <b>PREPP</b><br/>
 * Created by: Anand K. Rai on 20/1/16.<br/>
 * Email id: anand.rai@tothenew.com<br/>
 * <p/>
 */
public class ResponseMessage {

    // TODO : Change data type to int
    @SerializedName("code")
    private String mCode;

    @SerializedName("message")
    private String mMessage;

    public ResponseMessage() {
        mCode = "";
        mMessage = "";
    }

    public String getCode() {
        return mCode;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setCode(String code) {
        this.mCode = code;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }
}
