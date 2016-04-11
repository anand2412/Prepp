package com.prepp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * <p/>
 * Project: <b>PREPP</b><br/>
 * Created by: Anand K. Rai on 20/1/16.<br/>
 * Email id: anand.rai@tothenew.com<br/>
 * <p/>
 */
public class QuestionOptionData {

    private int QuestionId;

    private String Question;

    private String Explanation;

    private String questionImageUrl;

    @SerializedName("options")
    private List<Options> optionsList;

    public String getExplanation() {
        return Explanation;
    }

    public String getQuestion() {
        return Question;
    }

    public int getQuestionId() {
        return QuestionId;
    }


    public List<Options> getOptionsList() {
        return optionsList;
    }

    public String getQuestionImageUrl() {
        return questionImageUrl;
    }
}
