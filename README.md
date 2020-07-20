[![][reedelk-logo]][reedelk-url]

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/Kong/kong/blob/master/LICENSE)
[![Twitter](https://img.shields.io/twitter/follow/reedelk.svg?style=social&label=Follow)](https://twitter.com/intent/follow?screen_name=reedelk)

# Reedelk Runtime Platform (Community Edition)

Reedelk is a lightweight integration platform which allows to design, build and execute integration flows connecting data and applications.
Reedelk Integration Flow Designer helps companies building API-led integrations using an easy to use and integrated development studio for IntelliJ IDE: https://plugins.jetbrains.com/plugin/13420-reedelk-flow-designer.

Reedelk Reference Documentation: https://www.reedelk.com/documentation

## Development environment setup

### Installation

Clone the Reedelk runtime and all the modules repositories.

```
$ git clone https://github.com/reedelk/reedelk-runtime.git
$ git clone https://github.com/reedelk/module-core.git
$ git clone https://github.com/reedelk/module-csv.git
$ git clone https://github.com/reedelk/module-ftp.git
$ git clone https://github.com/reedelk/module-google-drive-v3.git
$ git clone https://github.com/reedelk/module-json.git
$ git clone https://github.com/reedelk/module-database.git
$ git clone https://github.com/reedelk/module-file.git
$ git clone https://github.com/reedelk/module-mail.git
$ git clone https://github.com/reedelk/module-mongodb.git
$ git clone https://github.com/reedelk/module-rabbitmq.git
$ git clone https://github.com/reedelk/module-rest.git
$ git clone https://github.com/reedelk/module-scheduler.git
$ git clone https://github.com/reedelk/module-xml.git
$ git clone https://github.com/reedelk/modules.git
```

Compile reedelk-runtime and all the modules
```
$ cd reedelk-runtime
$ mvn clean
$ mvn install
$ cd ../modules
$ mvn clean
$ mvn install
```

### Importing the project in IntelliJ

1. Select File -> Open -> Select {sources_dir}/reedelk-runtime/pom.xml -> When prompted select "Open as a project"
2. Import Modules -> Select {sources_dir}/modules/pom.xml

### Running the runtime on Java 11

Add to IntelliJ a new run config with the following VM options (only if you are running on JDK 11):

1. --add-opens java.base/java.net=ALL-UNNAMED
2. -Dio.netty.allocator.type=unpooled

  
Main class to be used for Run Configuration config: com.reedelk.runtime.Launcher

See attached image 'intellij-application-config.png' for the 'Application' configuration settings in IntelliJ.

### Build Tools

- JDK 8 / JDK 11
- Apache Maven 3.6.0
- Python 3.7.2



[reedelk-url]: https://www.reedelk.com/
[reedelk-logo]: https://www.reedelk.com/github/reedelk-logo-github-readme.png
