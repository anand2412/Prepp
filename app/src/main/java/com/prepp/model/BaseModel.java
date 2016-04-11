package com.prepp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * Project: <b>PREPP</b><br/>
 * Created by: Anand K. Rai on 20/1/16.<br/>
 * Email id: anand.rai@tothenew.com<br/>
 * <p/>
 */
public abstract class BaseModel {

    @SerializedName("status")
    private int mStatus;

    @SerializedName("messages")
    private List<ResponseMessage> mResponseMessageList;


    public BaseModel() {
        mResponseMessageList = new ArrayList<ResponseMessage>();
    }

    public List<ResponseMessage> getResponseMessageList() {
        return mResponseMessageList;
    }

    public boolean getStatus(){
        return mStatus == 1;
    }

    public void setStatus(boolean status) {
        if (status)
            mStatus = 1;
        else
            mStatus = 0;
    }

    public void setResponseMessage(ResponseMessage responseMessage) {
        mResponseMessageList.add(responseMessage);
    }

}
