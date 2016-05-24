package com.seckill.exception;

/**
 * Created by hgf on 16-5-22.
 */
public class ClosedSecKillException extends SecKillException {
    public ClosedSecKillException(String message) {
        super(message);
    }

    public ClosedSecKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
