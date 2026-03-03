package com.example.extensions;

import org.junit.jupiter.api.extension.ExtendWith;
import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(CleanupExtension.class)
public @interface CleanupUser {
    String tokenField() default "token";
}