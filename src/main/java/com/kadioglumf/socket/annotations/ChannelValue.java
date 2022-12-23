package com.kadioglumf.socket.annotations;

import java.lang.annotation.*;

/**
 * Mark a parameter as the channel's value
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ChannelValue {
}
