[![][reedelk-logo]][reedelk-url]

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/reedelk/reedelk-runtime/blob/master/LICENSE)
[![Twitter](https://img.shields.io/twitter/follow/reedelk.svg?style=social&label=Follow)](https://twitter.com/intent/follow?screen_name=reedelk)
# Reedelk Runtime Platform (Community Edition)

Reedelk is a lightweight data integration platform which allows to design, build and execute integration flows connecting data and applications using pre-built components.
Reedelk Integration Flow Designer helps companies building API-led integrations using an easy to use and integrated development plugin for [IntelliJ IDE](https://plugins.jetbrains.com/plugin/13420-reedelk-flow-designer).

[![][reedelk-intellij-plugin]][reedelk-url]

[Getting Started](https://www.reedelk.com/documentation/getting-started) |
[Documentation](https://www.reedelk.com/documentation) |
[Components](https://www.reedelk.com/documentation/components) |
[Releases](https://www.reedelk.com/documentation/releases) |
[Slack Developers Community](https://join.slack.com/t/reedelk/shared_invite/zt-fz3wx56f-XDylXpqXERooKeOtrhdZug)

## Summary

- [**Features**](#features)
- [**Development**](#development)
- [**License**](#license)

## Features

- **Low Footprint Runtime**: Reedelk Runtime has been specifically designed to be 
lightweight in terms of memory and deployment size.

- **Fast Bootstrap Time**: Reedelk Runtime bootstrap time is fast, around 3/4 seconds even 
with hundreds of components installed. This characteristic makes it the perfect choice
to be used within Docker containers and cloud deployments where the ability of scaling fast is foundamental.

- **Runtime Administration Console**: The modules deployed on Reedelk Data Integration Platform can be 
managed from an Administration console. It provides for each installed module info about 
their deployment state, integration flows packaged in the module and errors information. 
The Administration console can be used to manage the lifecycle of modules as well including 
installation, update and removal of modules.

- **OpenAPI Support**: Reedelk supports Open API initiative. Open API definitions are automatically generated and 
published for all REST based integration flows. 
The Open API definition can be customized with user defined metadata including JSON/XML schemas for HTTP responses, 
path parameters, query parameters and headers. OpenAPI definitions can also be imported from the Reedelk IntelliJ plugin
to generate blueprints of the REST flows defined in the API specification.

- **Docker Support**: You can use pre-built Reedelk Docker images to easily deploy and run your integration modules in seconds. 
Docker images are available for each version of Reedelk Data Integration Platform. 
Check out this 5 minutes [video](https://youtu.be/zYFPCA1PDDQ) tutorial explaining how to develop and deploy a simple Microservice using a Reedelk Docker image.

- **Reedelk IntelliJ Flow Designer**: The Reedelk IntelliJ flow designer plugin allows developers to quickly design, 
create and deploy integration flows from IntelliJ IDE with an easy to use visual flow designer. The IntelliJ plugin has 
been designed to significantly decrease the time needed to develop, test and validate changes in the flows by providing 
hot-swap capabilities and component aware suggestions. The plugin provides also a set of features to easily develop custom 
Reedelk components to be used inside integration flows.

- **Custom Components Development**: Extendable architecture for adding functionality with custom developed data integration components.

- **Data Streaming Support**: Reedelk is based on [Project Reactor](https://projectreactor.io/) and it can handle streaming of data using data streams.


## Development

### Installation

Clone the Reedelk Runtime, module-rest and module-core repositories.

```
$ git clone https://github.com/reedelk/reedelk-openapi.git
$ git clone https://github.com/reedelk/reedelk-runtime.git
$ git clone https://github.com/reedelk/module-core.git
$ git clone https://github.com/reedelk/module-rest.git
```

Compile Reedelk Runtime project and the Module REST and Module Core.
```
$ cd reedelk-openapi
$ mvn install
$ cd ..

$ cd reedelk-runtime
$ mvn install
$ cd ..

$ cd ../module-core
$ mvn install
$ cd ..

$ cd ../module-rest
$ mvn install
$ cd ..
```

### Importing the project in IntelliJ

1. Select File -> Open -> Select {sources_directory}/reedelk-runtime/pom.xml -> When prompted select "Open as a project"
2. Import REST Module -> Select {sources_directory}/module-rest/pom.xml
3. Import Core Module -> Select {sources_directory}/module-core/pom.xml

### Running the runtime on Java 11

Add to IntelliJ a new Run configuration with the following VM options (only if you are running on JDK 11):

1. --add-opens java.base/java.net=ALL-UNNAMED
2. -Dio.netty.allocator.type=unpooled

Main class to be used for Run Configuration config: com.reedelk.runtime.Launcher. 
See the picture below for the 'Application' configuration settings in IntelliJ.

[![][reedelk-intellij-project-setup]][reedelk-url]

### Build Tools

- JDK 8 / JDK 11
- Apache Maven 3.6.0
- Python 3.7.2

## License

```
Copyright (c) 2019-2020 Apiable Oy - Reedelk is an Apiable product.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[reedelk-url]: https://www.reedelk.com/
[reedelk-logo]: https://www.reedelk.com/github/reedelk-logo-github-readme.png
[reedelk-intellij-plugin]: https://www.reedelk.com/github/intellij-plugin.png
[reedelk-intellij-project-setup]: https://www.reedelk.com/github/intellij-application-config.png
