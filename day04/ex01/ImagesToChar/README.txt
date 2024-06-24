#Use the jar command to create the JAR file. You need to specify the manifest file, the name of the output JAR file,
 and the directories containing your classes and resources.

 >>>>>>>: jar -cfm yourJarName.jar manifest.txt -C path/to/compiled/classes/ . -C path/to/resources/ .

-c creates a new archive.
-f specifies the output file name.
-m includes a manifest file.
The -C option changes to the directory and includes the specified files.


# '-d' define *.class store folder - './target/'
# 'src/java/mr/t1337school/printer/*/*.java' - is path to source *.java files
javac -d ./target/ src/java/mr/t1337school/printer/*/*.java

# '-jar' - run jar archive
# './target/images-to-chars-printer.jar' - archive path
# '. 0' -command line arguments
java -jar ./target/images-to-chars-printer.jar . 0