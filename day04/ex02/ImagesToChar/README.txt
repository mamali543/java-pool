to compile:
    mkdir lib
    cp -r ../../../../JCDP-4.0.2.jar ../../../../JColor-5.5.1.jar ../../../../jcommander-1.82.jar lib
    place the external libraries (.jar, JCDP and Jcommander) in the lib directory
    javac -cp "./lib/*" -d target src/java/mr/t1337school/printer/*/*.java

to run:
     cp -rf ./src/resources ./target/
     java -cp "target/:lib/*" mr.t1337school.printer.app.Program --white=white --black=black
     the colon : is used as a delimiter to separate different paths within the classpath

to archive:
     cp -rf ./src/resources ./target/
     jar cmf src/manifest.txt ./target/images-to-chars-printer.jar -C ./target/ . #./target is the path of compiled classes and resources needed, the root of classpath where java will look

to run archive:
    java -jar ./target/images-to-chars-printer.jar --white=white --black=red