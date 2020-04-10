package com.gepardec.hogarama.rest.unitmanagement.interceptor;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * JAX-RS endpoints annotated with {@link DetermineOwner} are intercepted by {@link DetermineOwnerInterceptor}.
 * @see DetermineOwnerInterceptor
 */
@Inherited
@InterceptorBinding
@Retention(RUNTIME)
@Target({METHOD, TYPE})
public @interface DetermineOwner {
}

