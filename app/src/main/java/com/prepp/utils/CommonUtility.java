package com.prepp.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.prepp.model.ResponseMessage;
import com.prepp.PreppApp;
import com.prepp.R;

import java.util.List;

import retrofit.RetrofitError;


/**
 * <p/>
 * Project: <b>PREPP</b><br/>
 * Created by: Anand K. Rai on 20/1/16.<br/>
 * Email id: anand.rai@tothenew.com<br/>
 * <p/>
 */
public class CommonUtility {

    public static void showToast(Context context, String error){

        Toast.makeText(context,error,Toast.LENGTH_LONG).show();
    }

    public static void showErrorMessage(Context context, List<ResponseMessage> responseMessageList) {
        String error = "";
        for (int i = 0; i < responseMessageList.size(); i++) {
            error = error + ", " + responseMessageList.get(i).getMessage();
        }
        error = error.substring(2, error.length());
        showToast(context, error);
    }

    public static void showRetrofitErrorMessage(RetrofitError error) {
        Context context = PreppApp.getAppContext();
        try {
            switch (error.getKind()) {
                case NETWORK:
                    showToast(context, context.getString(R.string.no_internet_connection));
                case UNEXPECTED:
                    showToast(context, error.getResponse().getReason());
                case HTTP:
                    switch (error.getResponse().getStatus()) {
                        case 401:
                            showToast(context,  context.getString(R.string.msg_401));
                        case 404:
                            showToast(context,  context.getString(R.string.msg_404));
                        default:
                            showToast(context,  error.getResponse().getReason());
                    }
                default:
                    showToast(context, context.getString(R.string.unknow_error_kind) + error.getKind());
            }
        }catch (Exception e){
            showToast(context,  context.getString(R.string.err_msg));
        }

    }

    public static void loadImageWithGlide(Activity activity, String url, ImageView imageView){
        if(activity!=null){
            Glide.with(activity)
                    .load(url)
                    .placeholder(R.drawable.ic_action_a)
                    .crossFade()
                    .error(R.drawable.ic_action_a)
                    .into(imageView);
        }
    }
}
