package org.springframework.context.stereotype;

import java.lang.annotation.*;

/**
 * @Auther: Wang Ky
 * @Date: 2018/12/8 10:56
 * @Description:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface Component {
    String value() default "";
}
