package com.prepp.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * <p/>
 * Project: <b>e-Protege</b><br/>
 * Created by: Anand K. Rai on 24/9/15.<br/>
 * Email id: anand.rai@tothenew.com<br/>
 * <p/>
 */
public class BranchListData extends BaseModel {

    @SerializedName("data")
    private BranchList branchList;

    public BranchList getBranchData() {
        return branchList;
    }


    public class BranchList {

        @SerializedName("branches")
        private List<BranchDetail> branchDetailList;


        public List<BranchDetail> getBranchDetailList() {
            return branchDetailList;
        }
    }
}
