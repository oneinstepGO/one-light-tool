package com.oneinstep.light.exception;

import com.oneinstep.light.api.ResponseModel;

import static com.oneinstep.light.api.CommonResponseModel.FAILURE;

/**
 *
 */
public class LightBaseException extends RuntimeException implements ResponseModel {

    private final String code;

    private final String message;

    public LightBaseException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public LightBaseException(ResponseModel responseModel) {
        super(responseModel.getMessage());
        this.code = responseModel.getCode();
        this.message = responseModel.getMessage();
    }

    public LightBaseException(String message) {
        super(message);
        this.code = FAILURE.getCode();
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
