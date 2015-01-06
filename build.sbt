name := "akka-persistence-rocksdb"

organization := "com.hamrah"

version := "0.1.0-SNAPSHOT"

homepage := Some(url("https://github.com/mhamrah/akka-persistence-rocksdb"))

startYear := Some(2014)

scmInfo := Some(
  ScmInfo(
    url("https://github.com/mhamrah/akka-persistence-rocksdb"),
    "scm:git:https://github.com/mhamrah/akka-persistence-rocksdb.git",
    Some("scm:git:git@github.com:mhamrah/akka-persistence-rocksdb.git")
  )
)

/* scala versions and options */
scalaVersion := "2.10.4"

// These options will be used for *all* versions.
scalacOptions ++= Seq(
  "-deprecation"
  ,"-unchecked"
  ,"-encoding", "UTF-8"
  ,"-Xlint"
  ,"-Yclosure-elim"
  ,"-Yinline"
  ,"-Xverify"
  ,"-feature"
  ,"-language:postfixOps"
  //,"-optimise"
)

javacOptions ++= Seq("-Xlint:unchecked", "-Xlint:deprecation")

val akka = "2.4-SNAPSHOT"

/* dependencies */
libraryDependencies ++= Seq (
  "com.github.nscala-time" %% "nscala-time" % "1.4.0"
  // -- testing --
  , "org.scalatest" %% "scalatest" % "2.2.2" % "test"
  // -- Logging --
  //,"com.typesafe.scala-logging" %% "scala-logging" % "3.1.0"
  // -- Akka --
  ,"com.typesafe.akka" %% "akka-testkit" % akka % "test"
  ,"com.typesafe.akka" %% "akka-actor" % akka
  ,"com.typesafe.akka" %% "akka-slf4j" % akka

  ,"com.typesafe.akka" %% "akka-persistence-experimental" % akka
  ,"com.typesafe.akka" %% "akka-persistence-experimental-tck" % akka //% "test"
  // -- config --
  ,"com.typesafe" % "config" % "1.2.1"
  ,"org.rocksdb" % "rocksdbjni" % "3.8.0"
//  ,"org.rocksdb" % "rocksdbjni" % "3.8.0" classifier "osx"
  ,"ch.qos.logback" % "logback-classic" % "1.1.2"
)

fork := true

//javaOptions in Test += "-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5004"

testOptions in Test += Tests.Argument("-oDS")

resolvers ++= Seq(
  // Resolver.sonatypeRepo("snapshots")
  Resolver.typesafeRepo("snapshots")
)

scalaSource in Compile := baseDirectory.value / "src"

scalaSource in Test := baseDirectory.value / "src"

excludeFilter in (Compile, unmanagedSources) := HiddenFileFilter || "*_test.scala"

excludeFilter in (Test, unmanagedSources) := HiddenFileFilter

resourceDirectory in Compile := baseDirectory.value / "resources"

resourceDirectory in Test := baseDirectory.value / "resources"
