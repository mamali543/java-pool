package mr.t1337school.printer.logic;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import com.diogonunes.jcolor.Ansi;
import com.diogonunes.jcolor.Attribute;
public class Logic {
    private final Attribute whitePixColor;
    private final Attribute blackPixColor;
    public Logic(String white, String black){
        this.whitePixColor = resolveColor(white);
        this.blackPixColor = resolveColor(black);
    }

    public void printImage(){
        //read an image file into memory
        try{
            BufferedImage image = ImageIO.read(Logic.class.getResource("/resources/it.bmp"));
            for(int y = 0; y < image.getHeight(); y++){
                for (int x = 0; x < image.getWidth(); x++)
                {
                    int color = image.getRGB(x, y);
                    if ((color & 0x00FFFFFF) == 0)
                        System.out.print(Ansi.colorize(" ", blackPixColor));
                    else
                        System.out.print(Ansi.colorize(" ", whitePixColor));
                }
                System.out.println();
            }
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }
    }

    private Attribute resolveColor(String input) throws IllegalColorException {
        input = input.toUpperCase();
        switch (input) {
            case "RED":
                return Attribute.RED_BACK();
            case "GREEN":
                return Attribute.GREEN_BACK();
            case "BLUE":
                return Attribute.BLUE_BACK();
            case "BLACK":
                return Attribute.BLACK_BACK();
            case "WHITE":
                return Attribute.WHITE_BACK();

            case "BRIGHT_RED":
                return Attribute.BRIGHT_RED_BACK();
            case "BRIGHT_GREEN":
                return Attribute.BRIGHT_GREEN_BACK();
            case "BRIGHT_BLUE":
                return Attribute.BRIGHT_BLUE_BACK();
            case "BRIGHT_BLACK":
                return Attribute.BRIGHT_BLACK_BACK();
            case "BRIGHT_WHITE":
                return Attribute.BRIGHT_WHITE_BACK();

            default:
                throw new IllegalColorException();
        }
    }
}