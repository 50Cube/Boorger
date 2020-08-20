package pl.lodz.p.it.boorger.configuration.transactions;

import org.springframework.core.annotation.AliasFor;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Transactional
public @interface ServiceReadOnlyTransaction {

    @AliasFor(attribute = "propagation", annotation = Transactional.class)
    Propagation propagation() default Propagation.REQUIRES_NEW;

    @AliasFor(attribute = "isolation", annotation = Transactional.class)
    Isolation isolation() default Isolation.READ_COMMITTED;

    @AliasFor(attribute = "rollbackFor", annotation = Transactional.class)
    Class<? extends Throwable>[] rollbackFor() default AppBaseException .class;

    @AliasFor(attribute = "readOnly", annotation = Transactional.class)
    boolean readOnly() default true;
}
