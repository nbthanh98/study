package com.thanhnb.jwtauth.quartz;

import lombok.Data;

@Data
public class ServerResponse {
    private int statusCode;
    private String msg;
    private Object data;

    public static ServerResponse make(int statusCode, String msg, Object data) {
        ServerResponse response = new ServerResponse();
        response.setStatusCode(statusCode);
        response.setData(data);
        response.setMsg(msg);
        return response;
    }
}
