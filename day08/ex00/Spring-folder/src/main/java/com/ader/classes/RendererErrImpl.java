package com.ader.classes;

import com.ader.interfaces.PreProcessor;
import com.ader.interfaces.Renderer;

public class RendererErrImpl implements Renderer{
    PreProcessor preProcessor;

    public RendererErrImpl(PreProcessor preProcessor)
    {
        this.preProcessor = preProcessor;
    }
    @Override
    public void render(String s)
    {
        String str = this.preProcessor.process(s);
        System.err.println(str);
    }
}
