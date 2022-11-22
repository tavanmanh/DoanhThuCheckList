package com.viettel.coms.dto;

import org.json.JSONObject;

public class CallBotDTO {

    private String urlCallbot;
    private JSONObject callbotRequest;

    public String getUrlCallbot() {
        return urlCallbot;
    }

    public void setUrlCallbot(String urlCallbot) {
        this.urlCallbot = urlCallbot;
    }

    public JSONObject getCallbotRequest() {
        return callbotRequest;
    }

    public void setCallbotRequest(JSONObject callbotRequest) {
        this.callbotRequest = callbotRequest;
    }
}
