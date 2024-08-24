package com.ader.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE) //not ignored during runtime
@Target(ElementType.TYPE)  // Applicable to classes, interfaces, enums
public @interface HtmlForm {
    String action();
    String method();
}

