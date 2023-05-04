package com.gg.server.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    // error code 등록
    //404
    NOT_FOUND(404, "COMMON-ERR-404", "API NOT FOUND"),
    //500
    INTERNAL_SERVER_ERR(500, "COMMON-ERR-500","INTERNAL SERVER ERROR"),
    //400 잘못된 요청 코드
    BAD_REQUEST(400, "COMMON-ERR-400", "BAD REQUEST"),
    VALID_FAILED(400, "GAME-ERR-400" , "Valid Test Failed.")
    ;
    private int status;
    private String errCode;
    private String message;

    public void setMessage(String msg) {
        this.message = msg;
    }
}
