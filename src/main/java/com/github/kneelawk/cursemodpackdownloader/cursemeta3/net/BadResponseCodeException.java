package com.github.kneelawk.cursemodpackdownloader.cursemeta3.net;

import java.io.IOException;

public class BadResponseCodeException extends IOException {

    /**
     *
     */
    private static final long serialVersionUID = 5596135043380794664L;

    public BadResponseCodeException() {
    }

    public BadResponseCodeException(String message) {
        super(message);
    }

    public BadResponseCodeException(Throwable cause) {
        super(cause);
    }

    public BadResponseCodeException(String message, Throwable cause) {
        super(message, cause);
    }

}
