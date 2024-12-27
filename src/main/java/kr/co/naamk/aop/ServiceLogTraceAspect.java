package kr.co.naamk.aop;

import kr.co.naamk.aop.logtrace.LogTrace;
import kr.co.naamk.aop.logtrace.LogTraceStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("prod")
@Slf4j
@Aspect
@RequiredArgsConstructor
@Component
public class ServiceLogTraceAspect {
    private final LogTrace logTrace;

    @Pointcut("execution(* kr.co.naamk.api..*(..))")
    public void allAPI() {}

    @Pointcut("execution(* kr.co.naamk.config.security..*(..))")
    public void securityConfig() {}

//    @Pointcut("within(com.flyff.universe.utils.TokenProvider)")
//    public void tokenProvider() {}


//    @Around("allAPI() || tokenProvider() || securityConfig()")
    @Around("allAPI() || securityConfig()")
    public Object traceLog(ProceedingJoinPoint joinPoint) throws Throwable {
        String callingMethod = joinPoint.getSignature().toShortString();

        LogTraceStatus status = null;
        try {
            status = logTrace.begin(callingMethod);
            Object result = joinPoint.proceed();
            logTrace.end(status);

            return result;
        } catch(Exception e) {
            logTrace.exception(status, e);
            throw  e;
        }
    }
}
