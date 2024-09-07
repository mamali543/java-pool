package com.ader.classes;

import com.ader.interfaces.PreProcessor;

public class PreProcessorToUpperImpl implements PreProcessor{
    @Override
    public String process(String s){
        return s.toUpperCase();
    }
}
