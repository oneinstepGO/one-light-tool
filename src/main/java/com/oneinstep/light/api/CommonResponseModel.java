package com.oneinstep.light.api;

import lombok.Getter;

@Getter
public enum CommonResponseModel implements ResponseModel {

    /**
     * 处理失败
     */
    FAILURE("-1", "失败"),

    /**
     * 处理成功
     */
    SUCCESS("10000000", "成功");

    private final String code;

    private final String message;

    CommonResponseModel(String code, String msg) {
        this.code = code;
        this.message = msg;
    }

}