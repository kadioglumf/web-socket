package com.kadioglumf.socket.annotations;

import com.kadioglumf.socket.WebSocketRequestDispatcher;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ChannelHandler {

  /**
   * The channel pattern that the handler will be mapped to by {@link WebSocketRequestDispatcher}
   * using Spring's {@link org.springframework.util.AntPathMatcher}
   */
  String value() default "";

  String[] allowedRoles() default {};
}
