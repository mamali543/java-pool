package com.ader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)//To make these annotations available only at compile time and not included in the class file.@
@Target(ElementType.FIELD)
public @interface HtmlInput {
    String type();

    String name();

    String placeholder() default ""; // Optional; provide a default value
}
