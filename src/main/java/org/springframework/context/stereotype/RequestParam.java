package org.springframework.context.stereotype;

import java.lang.annotation.*;

/**
 * @Auther: Wang Ky
 * @Date: 2018/12/8 10:53
 * @Description:
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RequestParam {
    String value() default "";
}
