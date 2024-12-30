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

    @Pointcut("execution(* kr.co.naamk.web..*(..))")
    public void allAPI() {}

    @Pointcut("execution(* kr.co.naamk.config.security..*(..))")
    public void securityConfig() {}

//    @Pointcut("within(com.flyff.universe.utils.TokenProvider)")
//    public void tokenProvider() {}


//    @Around("allAPI() || tokenProvider() || securityConfig()")
    @Around("allAPI() || securityConfig()")
    public Object traceLog(ProceedingJoinPoint joinPoint) throws Throwable {

        String targetName = getTargetName(joinPoint);

        LogTraceStatus status = null;
        try {
            status = logTrace.begin(targetName);
            Object result = joinPoint.proceed();
            logTrace.end(status);

            return result;
        } catch(Exception e) {
            logTrace.exception(status, e);
            throw  e;
        }
    }


    private String getTargetName(ProceedingJoinPoint joinPoint) {
        Class<?>[] interfaces = joinPoint.getTarget().getClass().getInterfaces();
        String targetName = joinPoint.getSignature().toShortString();

        // CrudRepository 하위 인터페이스를 찾기
        for (Class<?> iface : interfaces) {
            if (org.springframework.data.repository.CrudRepository.class.isAssignableFrom(iface)) {
                targetName = iface.getSimpleName() + "." + joinPoint.getSignature().getName();;
                break;
            }
        }


        return targetName;

    }
}
