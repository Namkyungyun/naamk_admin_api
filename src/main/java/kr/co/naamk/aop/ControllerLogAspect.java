package kr.co.naamk.aop;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.naamk.web.dto.apiResponse.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.regex.Pattern;

@Slf4j
@Aspect
@Component
public class ControllerLogAspect {

    final String CALLING_ARROW = ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>";
    final String END_ARROW = "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<";
    private final String SPACE = "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t";

    @Pointcut("execution(* kr.co.naamk.web.controller..*(..))")
    public void allController() {}

    @Around("allController()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        StringBuilder commonStr = new StringBuilder();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        APIResponse result = null;

        if(requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

            // ipAddress
            String ipAddress = getIpAddress(request);
            commonStr.append(SPACE).append("[ipAddress] ").append(ipAddress).append("\n");
            
            // httpMethod & requestAPI uri
            String httpMethod = request.getMethod();
            String requestURI = request.getRequestURI();
            commonStr.append(SPACE).append("[requestURI] ").append(httpMethod).append(" ").append(requestURI).append("\n");
        }

        try {
            /** 비즈니스 로직으로 연결되기 전 inbound 정보 log 남기기
             * 로그 규격
             *  [request API] {httpMethod} {api uri}
             *  [request ARGS] {request args}
             * */
            String beforeMessage = commonStr + getRequestArgs(joinPoint);
            log.info("{} CALLING {}\n{}", CALLING_ARROW, getNow(), beforeMessage);

            result = (APIResponse) joinPoint.proceed();

            /** 비즈니스 로직 연결 이후 성공 케이스에 대한 log 남기기
             * 로그 규격
             *  [request IpAddress] {ipAddress}
             *  [request API] {httpMethod} {api uri}
             *  [response MSG] {response msg}
             * */
            String afterMessage = commonStr + getResponseMessage(result);
            log.info("{} COMPLETED {}\n{}", END_ARROW, getNow(), afterMessage);


            return result;

        } catch(Exception e) {
            /** 비즈니스 로직 연결 이후 예외 케이스에 대한 log 남기기
             * 로그 규격
             *  [request IpAddress] {ipAddress}
             *  [request API] {httpMethod} {api uri}
             *  [response MSG] {response msg}
             * */
            String exMessage = commonStr + getExceptionMessage(result, e);

            log.error("{} EXCEPTION {}\n{}", END_ARROW, getNow(), exMessage);

            throw e;
        }
    }


    private String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");

        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("X-Real-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();

            if (isIPv4(request.getRemoteAddr())) {
                ipAddress = "Client IP (IPv4), " + request.getRemoteAddr();
            } else if (isIPv6(ipAddress)) {
                ipAddress = "Client IP (IPv6), " + request.getRemoteAddr();
            } else {
                ipAddress = "Client IP (Unknown format): " + request.getRemoteAddr();
            }
        }

        return ipAddress;
    }


    // IP 주소가 IPv4인지 확인
    public static boolean isIPv4(String ip) {
        String IPV4_PATTERN = "^([0-9]{1,3}\\.){3}[0-9]{1,3}$";
        return Pattern.compile(IPV4_PATTERN).matcher(ip).matches();
    }

    // IP 주소가 IPv6인지 확인
    public static boolean isIPv6(String ip) {
        String IPV6_PATTERN =  "^(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3,3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3,3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))$";
        return Pattern.compile(IPV6_PATTERN).matcher(ip).matches();
    }


    private String getNow() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return dateFormat.format(System.currentTimeMillis());
    }

    private String getRequestArgs(ProceedingJoinPoint joinPoint) {
        StringBuilder sb = new StringBuilder();

        // request arguments
        Object[] args = joinPoint.getArgs();
        int argsCount = args.length;

        if(argsCount > 0) {
            sb.append(SPACE).append("[arguments] ");

            for(Object o : joinPoint.getArgs()) {
                sb.append("<").append(o.getClass().getSimpleName()).append("> ");
                sb.append(o.getClass().getSimpleName());
                sb.append(", ");
            }
            sb.delete(sb.length() - 2, sb.length()); // 마지막 , 지우기
        }

        return sb.toString();
    }

    private String getResponseMessage(APIResponse result) {
        StringBuilder sb = new StringBuilder();

        // status code
        Integer statusCode = result.getHeader().getResultCode();
        sb.append(SPACE).append("[result]").append("\n");
        sb.append(SPACE).append("\t- return code : ").append(statusCode).append("\n");

        // body
        Object body = Objects.requireNonNull(result.getBody());
        String name = body.getClass().getName();
        sb.append(SPACE).append("\t- return val : ").append(name);

        return sb.toString();
    }

    private String getExceptionMessage(APIResponse result, Exception e) {
        StringBuilder sb = new StringBuilder();

        // status code
        if(result != null) {
            Integer statusCode = result.getHeader().getResultCode();
            sb.append(SPACE).append("[result]").append("\n");
            sb.append(SPACE).append("\t- return code : ").append(statusCode).append("\n");
        }

        // error message
        String message = e.getMessage();
        String exceptionType = e.getClass().getName();
        sb.append(SPACE).append("\t- error type : ").append(exceptionType).append("\n");
        sb.append(SPACE).append("\t- error msg : ").append(message).append("\n");

        return sb.toString();
    }
}
