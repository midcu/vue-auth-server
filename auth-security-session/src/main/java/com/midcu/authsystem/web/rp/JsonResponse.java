package com.midcu.authsystem.web.rp;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public class JsonResponse {
    
    public static void setJsonResult(HttpServletResponse response, String message, int status) throws IOException {

        response.setStatus(status);

        response.setContentType("application/json; charset=UTF-8");

        response.getWriter().print(new ExceptionResponse(message).toString());
    }
}
