package com.sample.config.dbutils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * Created by himanshu.virmani on 06/09/16.
 */
@Aspect
@Component
public class ReadOnlyConnectionInterceptor implements Ordered {

    private int order;

    /*
    The last bit to clarify is the magical @Value("20").
    It is used to set the order parameter of our interceptor.
    The thing is, we need to make sure that the DataSource type is set before the @Transactional annotation kicks in.
    Otherwise connection will already be bound to the thread at the time our @ReadOnlyConnection gets processed.
    So basically we need set the order below the order of transactions annotation (20 < 100).
     */
    @Value("20")
    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Pointcut(value="execution(public * *(..))")
    public void anyPublicMethod() { }

    @Around("@annotation(readOnlyConnection)")
    public Object proceed(ProceedingJoinPoint pjp, ReadOnlyConnection readOnlyConnection) throws Throwable {
        try {
            DbContextHolder.setDbType(DbType.SLAVE);
            Object result = pjp.proceed();
            DbContextHolder.clearDbType();
            return result;
        } finally {
            // restore state
            DbContextHolder.clearDbType();
        }
    }
}
