package com.oneinstep.light.api;

import lombok.Data;

import static com.oneinstep.light.api.CommonResponseModel.FAILURE;

@Data
public class Response<T> {

    /**
     * 错误码
     */
    private String code;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 各个层级使用的正式数据
     */
    private T data;

    /**
     * 构建成功状态，包含data的resp
     *
     * @param data 回包数据
     * @param <T>  回包数据类型
     * @return resp
     */
    public static <T> Response<T> ok(T data) {
        Response<T> response = new Response<>();
        response.setCode(CommonResponseModel.SUCCESS.getCode());
        response.setMessage(CommonResponseModel.SUCCESS.getMessage());
        response.setData(data);
        return response;
    }

    public static <T> Response<T> ok() {
        return ok(null);
    }

    /**
     * 失败对象建造方法
     **/
    public static <T> Response<T> error(String message) {
        return error(FAILURE.getCode(), message);
    }

    public static <T> Response<T> error(ResponseModel responseCode) {
        return error(responseCode.getCode(), responseCode.getMessage());
    }

    public static <T> Response<T> error(String code, String message) {
        Response<T> response = new Response<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

}