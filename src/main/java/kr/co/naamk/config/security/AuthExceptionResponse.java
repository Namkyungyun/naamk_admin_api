package kr.co.naamk.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.naamk.exception.type.ServiceMessageType;
import kr.co.naamk.web.dto.apiResponse.APIResponseEntityBuilder;

import java.io.IOException;

public class AuthExceptionResponse {
    // 인증 안한 상태로 접근 시
    public static void unAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ObjectMapper om = new ObjectMapper();
        Object responseDto = APIResponseEntityBuilder.create().service(request)
                .resultMessage(ServiceMessageType.SC_UNAUTHORIZED)
                .build();

        String responseBody = om.writeValueAsString(responseDto);
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(401);
        response.getWriter().println(responseBody);
    }
}
