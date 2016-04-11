package com.prepp.model;

import com.google.gson.annotations.SerializedName;

/**
 * <p/>
 * Project: <b>PREPP</b><br/>
 * Created by: Anand K. Rai on 21/1/16.<br/>
 * Email id: anand.rai@tothenew.com<br/>
 * <p/>
 */
public class RecordSave extends BaseModel {

    @SerializedName("data")
    private MobileNumberData data;

    public MobileNumberData getData() {
        return data;
    }

    public class MobileNumberData{

        private boolean mobileNumberVerified;

        public boolean isMobileNumberVerified() {
            return mobileNumberVerified;
        }

        public void setMobileNumberVerified(boolean mobileNumberVerified) {
            this.mobileNumberVerified = mobileNumberVerified;
        }
    }
}
