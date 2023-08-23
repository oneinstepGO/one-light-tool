package com.oneinstep.light.api;

public interface ResponseModel {
    /**
     * 获取响应码
     *
     * @return 响应码
     */
    String getCode();

    /**
     * 获取响应信息
     *
     * @return 响应信息
     */
    String getMessage();
}