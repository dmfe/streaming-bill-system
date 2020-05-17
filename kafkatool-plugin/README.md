To generate and install artifact for kafkatool dependency go to kafkatool directory and execute command:
$ mvn install:install-file \
 -Dfile=lib/ofjar.jar \
 -DgroupId=com.kafkatool \
 -DartifactId=kafkatool \
 -Dversion=2.0.7 \
 -Dpackaging=jar \
 -DgeneratePom=true

Build plugin:
$ mvn clean package

Usage:
copy target/kafkatool-plugin-1.0-jar-with-dependencies.jar into kafkatool_dir/plugins