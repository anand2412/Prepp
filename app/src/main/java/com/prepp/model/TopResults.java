package com.prepp.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * <p/>
 * Project: <b>PREPP</b><br/>
 * Created by: Anand K. Rai on 24/1/16.<br/>
 * Email id: anand.rai@tothenew.com<br/>
 * <p/>
 */
public class TopResults extends BaseModel {

    @SerializedName("data")
    private HashMap<String,String> topResultData;

    public HashMap<String, String> getTopResultData() {
        return topResultData;
    }
}
