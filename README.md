# ESB Runtime

ESB Runtime Project.

#### Installation
1. git clone https://bitbucket.org/reedelk/runtime.git
2. git clone https://bitbucket.org/reedelk/commons.git
3. git clone https://bitbucket.org/reedelk/modules.git
4. git clone https://bitbucket.org/reedelk/parent.git
5. cd parent
6. mvn clean
7. mvn install

#### Importing the project in IntelliJ

Select File -> Open -> Select {sources_dir}/parent/pom.xml -> When prompted select "Open as a project"

#### Running the runtime on Java 11

Add to IntelliJ a new run config with the following VM options (only if you are running on JDK 11):

1. --add-opens java.base/java.net=ALL-UNNAMED
2. --add-opens java.base/jdk.internal.misc=ALL-UNNAMED
3. -Dnashorn.args="--no-deprecation-warning"
4. -Dio.netty.allocator.type=unpooled

  
Main class to be used for Run Configuration config: com.reedelk.runtime.Launcher

See attached image 'intellij-application-config.png' for the 'Application' configuration settings in IntelliJ.

#### Build Tools

- JDK 8 / JDK 11
- Apache Maven 3.6.0
- Python 3.7.2
