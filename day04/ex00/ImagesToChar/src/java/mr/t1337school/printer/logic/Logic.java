package mr.t1337school.printer.logic;


import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.*;

public class Logic {
    private final char whiteP;
    private final char blackP;
    private final String fileP;
    public Logic(char white_pixel, char black_pixel, String filePath){
        this.whiteP = white_pixel;
        this.blackP = black_pixel;
        this.fileP = filePath;
    }

    public void printImage(){
        //read an image file into memory
        try{
            BufferedImage image = ImageIO.read(new File(fileP));
            for(int y = 0; y < image.getHeight(); y++){
                for (int x = 0; x < image.getWidth(); x++)
                {
                    int color = image.getRGB(x, y);
                    if (color == Color.BLACK.getRGB())
                        System.out.print(blackP);
                    else
                        System.out.print(whiteP);
                }
                System.out.println();
            }
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }
    }
}