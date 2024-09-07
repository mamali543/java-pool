package com.ader.classes;

import com.ader.interfaces.Printer;
import com.ader.interfaces.Renderer;

public class PrinterWithPrefixImpl implements Printer{
    private final Renderer renderer;
    private String prefix = "";

    public PrinterWithPrefixImpl(Renderer renderer){ this.renderer = renderer; }

    public void setPrefix(String prefix){ this.prefix = prefix; }

    @Override
    public void displayMessage(String msg)
    {
        if (!prefix.isEmpty()) {
			prefix += " ";
		}
        this.renderer.render(prefix+msg);
    }
}
