package io.github.encryptorcode.extra;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is a complementary annotation for custom gson implementations if you use any.
 * You need to make sure to skip fields with this annotation when returning the data back to user.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NoSerialization {
}