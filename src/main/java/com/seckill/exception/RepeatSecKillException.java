package com.seckill.exception;

import com.seckill.dto.SecKillExecution;

/**
 * Created by hgf on 16-5-22.
 */
public class RepeatSecKillException extends SecKillException {
    public RepeatSecKillException(String message) {
        super(message);
    }

    public RepeatSecKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
