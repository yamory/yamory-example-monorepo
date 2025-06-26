ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.12"
ThisBuild / organization := "com.example"

lazy val commonSettings = Seq(
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.2.15" % Test
  )
)

lazy val root = (project in file("."))
  .settings(
    name := "sbt-monorepo-util",
    publish := {},
    publishLocal := {}
  )
  .aggregate(core, api, main)

lazy val core = (project in file("core"))
  .settings(
    commonSettings,
    name := "core",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % "2.9.0"
    )
  )

lazy val api = (project in file("api"))
  .settings(
    commonSettings,
    name := "api",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % "10.5.0",
      "com.typesafe.akka" %% "akka-stream" % "2.8.0",
      "com.typesafe.akka" %% "akka-http-spray-json" % "10.5.0"
    )
  )
  .dependsOn(core)

lazy val main = (project in file("main"))
  .settings(
    commonSettings,
    name := "main",
    assembly / mainClass := Some("com.example.main.Main"),
    assembly / assemblyJarName := "app.jar"
  )
  .dependsOn(core, api)
  .enablePlugins(AssemblyPlugin)