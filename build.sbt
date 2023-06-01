import Dependencies.Compile._
import Dependencies.Test._

ThisBuild / scalaVersion := "3.1.2"
ThisBuild / organization := "com.47deg"
ThisBuild / versionScheme := Some("early-semver")

addCommandAlias("plugin-example", "reload; clean; publishLocal; examples/compile")
addCommandAlias("ci-test", "scalafmtCheckAll; scalafmtSbtCheck; github; root / test")
addCommandAlias("ci-it-test", "plugin / IntegrationTest / test")
addCommandAlias("ci-publish", "github; ci-release")

lazy val root =
  (project in file("./")).settings(publish / skip := true).aggregate(plugin, `munit-snap`, core)

lazy val bypassZinc = (project in file("./bypassZinc"))
  .settings(publish / skip := true)
  .aggregate(
    core,
    examples,
    `zero-arguments-no-continuation-treeview`,
    `zero-arguments-one-continuation-code-before-used-after`,
    `list-map`,
    `two-arguments-two-continuations`
  )

lazy val core = project.configs(IntegrationTest).settings(pluginSettings: _*)

lazy val plugin =
  project.configs(IntegrationTest).dependsOn(`munit-snap`, core).settings(pluginSettings: _*)

lazy val examples = project
  .dependsOn(plugin)
  .settings(examplesSettings: _*)
  .enablePlugins(ForceableCompilationPlugin)

lazy val `zero-arguments-no-continuation-treeview` =
  (project in file("./zero-arguments-no-continuation-treeview"))
    .settings(showTreeSettings: _*)
    .dependsOn(plugin)
    .enablePlugins(ForceableCompilationPlugin)

lazy val `zero-arguments-one-continuation-code-before-used-after` =
  (project in file("./zero-arguments-one-continuation-code-before-used-after"))
    .settings(showTreeSettings: _*)
    .dependsOn(plugin)
    .enablePlugins(ForceableCompilationPlugin)

lazy val `list-map` = (project in file("./list-map"))
  .settings(showTreeSettings: _*)
  .dependsOn(plugin)
  .enablePlugins(ForceableCompilationPlugin)

lazy val `two-arguments-two-continuations` =
  (project in file("./two-arguments-two-continuations"))
    .settings(showTreeSettings: _*)
    .dependsOn(plugin)
    .enablePlugins(ForceableCompilationPlugin)

lazy val `munit-snap` = (project in file("./munit-snap")).settings(munitSnapSettings)

lazy val munitSnapSettings = Seq(
  name := "munit-snap",
  autoAPIMappings := true,
  Test / fork := true,
  libraryDependencies += munit,
  libraryDependencies += circe,
  libraryDependencies += circeParser,
  libraryDependencies += circeGeneric % Test
)

lazy val commonSettings = Seq(
  javaOptions ++= javaOptionsSettings,
  autoAPIMappings := true,
  Test / fork := true
)

def testAndIntegrationTest(m: ModuleID): List[ModuleID] = List(m).flatMap { m =>
  List(m % Test, m % IntegrationTest)
}

lazy val pluginSettings: Seq[Def.Setting[_]] =
  Defaults.itSettings ++ Seq(
    exportJars := true,
    autoAPIMappings := true,
    publish / skip := true,
    Test / fork := true,
    libraryDependencies ++= List(
      "org.scala-lang" %% "scala3-compiler" % "3.1.2"
    ) ++ testAndIntegrationTest(munit),
    Test / javaOptions += {
      val `scala-compiler-classpath` =
        (Compile / dependencyClasspath)
          .value
          .files
          .map(_.toPath().toAbsolutePath().toString())
          .mkString(":")
      s"-Dscala-compiler-classpath=${`scala-compiler-classpath`}"
    },
    Test / javaOptions += {
      s"""-Dcompiler-scalacOptions=\"${scalacOptions.value.mkString(" ")}\""""
    },
    Test / javaOptions += Def.taskDyn {
      Def.task {
        val _ = (Compile / Keys.`package`).value
        val `scala-compiler-options` =
          s"${(plugin / Compile / packageBin).value}"
        s"""-Dscala-compiler-plugin=${`scala-compiler-options`}"""
      }
    }.value,
    IntegrationTest / fork := true,
    IntegrationTest / javaOptions := (Test / javaOptions).value
  )

lazy val showTreeSettings: Seq[Def.Setting[_]] =
  Seq(
    publish / skip := true,
    autoCompilerPlugins := true,
    resolvers += Resolver.mavenLocal,
    forceCompilation := true,
    Compile / scalacOptions += s"-Xplugin:${(plugin / Compile / packageBin).value}",
    Compile / scalacOptions += "-Xprint:continuationsCallsPhase",
    Test / scalacOptions += s"-Xplugin:${(plugin / Compile / packageBin).value}",
    Test / scalacOptions += "-Xprint:continuationsCallsPhase"
  )

lazy val examplesSettings: Seq[Def.Setting[_]] =
  Seq(
    publish / skip := true,
    autoCompilerPlugins := true,
    forceCompilation := true,
    resolvers += Resolver.mavenLocal,
    Compile / scalacOptions += s"-Xplugin:${(plugin / Compile / packageBin).value}",
    Test / scalacOptions += s"-Xplugin:${(plugin / Compile / packageBin).value}"
  )

lazy val scalalikeSettings: Seq[Def.Setting[_]] =
  Seq(
    classLoaderLayeringStrategy := ClassLoaderLayeringStrategy.Flat,
    javaOptions ++= javaOptionsSettings,
    autoAPIMappings := true,
    libraryDependencies ++= Seq(
      logback,
      scalacheck % Test,
      testContainers % Test,
      testContainersMunit % Test
    )
  )

lazy val javaOptionsSettings = Seq(
  "-XX:+IgnoreUnrecognizedVMOptions",
  "-XX:-DetectLocksInCompiledFrames",
  "-XX:+UnlockDiagnosticVMOptions",
  "-XX:+UnlockExperimentalVMOptions",
  "-XX:+UseNewCode",
  "--add-modules=java.base",
  "--add-modules=jdk.incubator.concurrent",
  "--add-opens java.base/jdk.internal.vm=ALL-UNNAMED",
  "--add-exports java.base/jdk.internal.vm=ALL-UNNAMED",
  "--enable-preview"
)
