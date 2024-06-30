# Compilation:
    -this command compiles .java files into .class files, put them in target folder.
    javac -d target src/java/mr/t1337school/printer/*/*.java

# Launch app:
     # Usage: app <char for white pixels> <char for black pixels> <full path to black & white bmp image>
     # -cp specifies the classpath where java has to look for the compiled .class files
      java -cp ./target mr.t1337school.printer.app.Program . 0 it.bmp
