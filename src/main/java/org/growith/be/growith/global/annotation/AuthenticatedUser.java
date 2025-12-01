package org.growith.be.growith.global.annotation;


import io.swagger.v3.oas.annotations.Parameter;

import java.lang.annotation.*;

@Documented
@Target({ElementType.PARAMETER})
@Parameter(hidden = true)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthenticatedUser {
}
