package com.example.FinCore.vo.response;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.vo.SubscriptionVO;

public class SubscriptionResponse extends BasicResponse {

    private SubscriptionVO data;

    public SubscriptionResponse() {
        super();
    }

    public SubscriptionResponse(int code, String message, SubscriptionVO data) {
        super(code, message);   // 用父類的欄位
        this.data = data;
    }

    public SubscriptionResponse(ResponseMessages res, SubscriptionVO data) {
        super(res);
        this.data = data;
    }

    public SubscriptionVO getData() {
        return data;
    }

    public void setData(SubscriptionVO data) {
        this.data = data;
    }
}