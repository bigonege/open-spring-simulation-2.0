package org.springframework.context.stereotype;

import java.lang.annotation.*;

/**
 * @Auther: Wang Ky
 * @Date: 2018/12/8 10:53
 * @Description:
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Controller {
    String value() default "";
}
