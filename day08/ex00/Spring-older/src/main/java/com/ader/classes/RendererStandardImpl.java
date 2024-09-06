package com.ader.classes;

import com.ader.interfaces.PreProcessor;
import com.ader.interfaces.Renderer;

public class RendererStandardImpl implements Renderer{
    PreProcessor preProcessor;

    public RendererStandardImpl(PreProcessor preProcessor)
    {
        this.preProcessor = preProcessor;
    }
    @Override
    public void render(String s)
    {
        String str = this.preProcessor.process(s);
        System.out.println(str);
    }
}
