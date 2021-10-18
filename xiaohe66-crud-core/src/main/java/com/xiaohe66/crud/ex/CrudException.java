package com.xiaohe66.crud.ex;

import lombok.Getter;

/**
 * @author xiaohe
 * @since 2021.09.27 14:34
 */
@Getter
public class CrudException extends RuntimeException{

    private final String table;
    private final String params;

    public CrudException(String table, String params) {
        this.table = table;
        this.params = params;
    }

    public CrudException(String message, String table, String params) {
        super(message);
        this.table = table;
        this.params = params;
    }

    public CrudException(String message, Throwable cause, String table, String params) {
        super(message, cause);
        this.table = table;
        this.params = params;
    }

    public CrudException(Throwable cause, String table, String params) {
        super(cause);
        this.table = table;
        this.params = params;
    }
}
