package zsh.tools.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import zsh.tools.aop.anno.TimeConsumeLogger;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Aspect
@Order
public class TimeConsumeAspect {

    private final String POINT_CUT = "@annotation(zsh.tools.aop.anno.TimeConsumeLogger)";

    @Before(value=POINT_CUT)
    public void before(JoinPoint joinPoint) {
    }

    @After(value=POINT_CUT)
    public void doAfterAdvice(JoinPoint joinPoint) {
    }

    @Around(value=POINT_CUT)
    public void doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Signature signature = proceedingJoinPoint.getSignature();
        Method method = ((MethodSignature)signature).getMethod();

        TimeConsumeLogger annotation = method.getAnnotation(TimeConsumeLogger.class);
        Map<String, Object> attributesMap = AnnotationUtils.getAnnotationAttributes(annotation);
        String logTopic = (String) attributesMap.get("logTopic");
        TimeUnit timeUnit = (TimeUnit) attributesMap.get("timeUnit");

        if (logTopic.equals("")) {
            logTopic = proceedingJoinPoint.getTarget().getClass().toString();
        }
        Logger log = LoggerFactory.getLogger(logTopic);

        long s = System.currentTimeMillis();
        proceedingJoinPoint.proceed();
        long e = System.currentTimeMillis() - s;

        log.info( "{} -> COST: [{}] {}", method.toGenericString(), timeUnit.convert(e, TimeUnit.MILLISECONDS), timeUnit.name());
    }

    @AfterReturning(value = POINT_CUT, returning="result")
    public void doAfterReturningAdvice1(JoinPoint joinPoint, Object result) {
    }

    @AfterReturning(value = POINT_CUT,returning="result", argNames="result")
    public void doAfterReturningAdvice2(String result) {
    }

    @AfterThrowing(value = POINT_CUT,throwing = "exception")
    public void doAfterThrowingAdvice(JoinPoint joinPoint, Throwable exception) {
    }

}
