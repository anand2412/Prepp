package com.prepp.customviews;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.prepp.helper.ProgressDialogParams;

/**
 * Created by pankaj on 29/9/15.
 */
public class ProgressDialogHandler {
    private Context context;
    private ProgressDialog progressDialog;

    public ProgressDialogHandler(@NonNull Context context){
        this.context = context;
    }


    public void showProgressDialog(ProgressDialogParams params) {
        progressDialog = new ProgressDialog(context);
        if (params != null) {
            progressDialog.setTitle(params.getTitle());
            progressDialog.setMessage(params.getMessage());
            progressDialog.setIndeterminate(params.isIndeterminate());
            progressDialog.setCancelable(params.isCancellable());
        }
        progressDialog.show();
    }



    public void dismissProgressDialog() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }
}
