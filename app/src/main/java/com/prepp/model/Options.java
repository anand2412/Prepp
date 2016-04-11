package com.prepp.model;

/**
 * <p/>
 * Project: <b>eQuestionnire</b><br/>
 * Created by: Anand K. Rai on 1/10/15.<br/>
 * Email id: anand.rai@tothenew.com<br/>
 * <p/>
 */
public class Options {

    private String optionValue;

    private boolean isAnswer;

    private int optionId;

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public boolean isAnswer() {
        return isAnswer;
    }

    public void setIsAnswer(boolean isAnswer) {
        this.isAnswer = isAnswer;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

}
