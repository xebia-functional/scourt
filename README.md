# unwrapped

## BUILD NOTICE

The following projects have been unaggregated and must be compiled seperately and run with runMain:

* continuationsPluginExample
* zero-arguments-no-continuation-treeview
* zero-arguments-one-continuation-code-before-used-after
* list-map
* two-arguments-two-continuations

To run these projects, you must use `runMain` with the fully qualified name of the main.

```shell
$ sbt
root> continuationsPluginExample/runMain examples.ThreeDependentcontinuations
```

This is so that ci build will continue to work until
https://github.com/sbt/zinc/pull/1174 is merged and resolved.

### Build and run in your local environment:

Pre-requisites:

- [Java 19](https://openjdk.org/projects/jdk/19/)
- Scala 3

1. Download the latest Project Loom [Early-Access build](https://openjdk.org/projects/jdk/19/) for your system architecture.

    This is easy to do if you are using [SDKMAN](https://sdkman.io/). First list java
    versions with `sdk list java`, and `sdk install java
    19-ea-<XX>-open`, where XX is the latest Java.net version 19
    release number. Make it the default with `sdk default java
    19-ea-<XX>-open`.

2. Set your `JAVA_HOME` to the path you extracted above.

  Unnecessary if you have set the ea build to be your default in sdkman.

You can now compile and run the tests:

```shell
env JAVA_OPTS='--enable-preview --add-modules jdk.incubator.concurrent' sbt "clean; compile; test"
```

**NOTE**: The Loom project is defined as an incubator module that is a means to distribute APIs which are not final or completed to get feedback from the developers.
You should include the `-add-module` Java option to add the module to the class path of the project.
