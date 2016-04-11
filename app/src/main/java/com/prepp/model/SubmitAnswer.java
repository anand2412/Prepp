package com.prepp.model;

/**
 * <p/>
 * Project: <b>eQuestionnire</b><br/>
 * Created by: Anand K. Rai on 2/10/15.<br/>
 * Email id: anand.rai@tothenew.com<br/>
 * <p/>
 */
public class SubmitAnswer {

    private int answerId;

    private int questionId;

    private boolean isAttempted;

    public SubmitAnswer(int answerId, int quesId, boolean isAttempted){
        this.answerId=answerId;
        this.questionId=quesId;
        this.isAttempted=isAttempted;
    }


}
