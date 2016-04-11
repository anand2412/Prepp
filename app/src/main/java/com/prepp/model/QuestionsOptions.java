package com.prepp.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * <p/>
 * Project: <b>eQuestionnire</b><br/>
 * Created by: Anand K. Rai on 1/10/15.<br/>
 * Email id: anand.rai@tothenew.com<br/>
 * <p/>
 */
public class QuestionsOptions extends BaseModel {

    @SerializedName("data")
    private Map<String,QuestionOptionData> questionOptionDataMap;

    public Map<String, QuestionOptionData> getQuestionOptionDataMap() {
        return questionOptionDataMap;
    }
}
