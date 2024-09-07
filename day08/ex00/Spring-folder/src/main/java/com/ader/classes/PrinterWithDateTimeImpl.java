package com.ader.classes;
import java.time.LocalDateTime;

import com.ader.interfaces.Printer;
import com.ader.interfaces.Renderer;

public class PrinterWithDateTimeImpl implements Printer{

    private final Renderer renderer;

    public PrinterWithDateTimeImpl(Renderer renderer){this.renderer = renderer;}

    @Override
    public void displayMessage(String msg){this.renderer.render(LocalDateTime.now() + " " + msg);}
}
