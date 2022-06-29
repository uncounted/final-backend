package com.hanghae0705.sbmoney.model.dto;

import lombok.Data;

@Data
public class Message {

    private boolean result;
    private String respMsg;
    private Object data;

    public Message(boolean result, String respMsg, Object data) {
        this.result = result;
        this.respMsg = respMsg;
        this.data = data;
    }

    public Message(boolean result, String respMsg) {
        this.result = result;
        this.respMsg = respMsg;
    }
}
