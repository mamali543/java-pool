package mr.t1337school.printer.app;

import mr.t1337school.printer.logic.Logic;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.converters.IParameterSplitter;


public class Program {

    @Parameters(separators = "=")
    public static class Arguments {
        @Parameter(names="--white", required = true, description = "Specify the white color")
        private String white;

        @Parameter(names="--black", required = true, description = "Specify the black color")
        private String black;

        public String getWhiteColor() {
            return white != null ? white.trim() : null;
        }

        public String getBlackColor() {
            return black != null ? black.trim() : null;
        }
    }
    public static void main(String[] args){
        Arguments arguments = new Arguments();
        JCommander jCommander = JCommander.newBuilder()
                .addObject(arguments)
                .build();
        try {
            jCommander.parse(args);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("Usage: program_name --white=<color for white pixel> --black=<color for black pixel>");
            System.exit(1);
        }

        if ( arguments.getWhiteColor().isEmpty() || arguments.getBlackColor().isEmpty()) {
            System.err.println("Incorrect usage!\nUsage: program_name --white=<color for white pixel> --black=<color for black pixel>");
            System.exit(-1);
        }

        String white = arguments.getWhiteColor();
        String black = arguments.getBlackColor();
        new Logic(white, black).printImage();
    }
}
