# Reedelk Runtime

Reedelk runtime project set up.

#### Installation
* git clone https://bitbucket.org/reedelk/runtime.git
* git clone https://bitbucket.org/reedelk/modules.git
* git clone https://bitbucket.org/reedelk/module-core.git
* git clone https://bitbucket.org/reedelk/module-csv.git
* git clone https://bitbucket.org/reedelk/module-database.git
* git clone https://bitbucket.org/reedelk/module-file.git
* git clone https://bitbucket.org/reedelk/module-json.git
* git clone https://bitbucket.org/reedelk/module-mail.git
* git clone https://bitbucket.org/reedelk/module-rabbitmq.git
* git clone https://bitbucket.org/reedelk/module-rest.git
* git clone https://bitbucket.org/reedelk/module-scheduler.git
* git clone https://bitbucket.org/reedelk/module-xml.git

* cd runtime
* mvn clean
* mvn install
* cd ../modules
* mvn clean
* mvn install

#### Importing the project in IntelliJ

Select File -> Open -> Select {sources_dir}/parent/pom.xml -> When prompted select "Open as a project"

#### Running the runtime on Java 11

Add to IntelliJ a new run config with the following VM options (only if you are running on JDK 11):

1. --add-opens java.base/java.net=ALL-UNNAMED
2. -Dio.netty.allocator.type=unpooled

  
Main class to be used for Run Configuration config: com.reedelk.runtime.Launcher

See attached image 'intellij-application-config.png' for the 'Application' configuration settings in IntelliJ.

#### Build Tools

- JDK 8 / JDK 11
- Apache Maven 3.6.0
- Python 3.7.2
