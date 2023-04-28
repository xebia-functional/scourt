### Build and run in your local environment:

Pre-requisites:

- [Project Loom](https://jdk.java.net/loom/)
- Scala 3

1. Download the latest Project Loom [Early-Access build](https://jdk.java.net/loom/) for your system architecture.
2. Set your `JAVA_HOME` to the path you extracted above.

You can now compile and run the tests:

```shell
env JAVA_OPTS='--add-modules jdk.incubator.concurrent' sbt "clean; compile; test"
```

**NOTE**: The Loom project is defined as an incubator module that is a means to distribute APIs which are not final or completed to get feedback from the developers.
You should include the `-add-module` Java option to add the module to the class path of the project.
