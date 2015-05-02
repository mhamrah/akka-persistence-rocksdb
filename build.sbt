name := "akka-persistence-rocksdb"

organization := "com.hamrah"

homepage := Some(url("https://github.com/mhamrah/akka-persistence-rocksdb"))

startYear := Some(2015)

scmInfo := Some(
  ScmInfo(
    url("https://github.com/mhamrah/akka-persistence-rocksdb"),
    "scm:git:https://github.com/mhamrah/akka-persistence-rocksdb.git",
    Some("scm:git:git@github.com:mhamrah/akka-persistence-rocksdb.git")
  )
)

licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))

/* scala versions and options */
scalaVersion := "2.11.6"

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
  // -- testing --
  "org.scalatest" %% "scalatest" % "2.2.2" % "test"
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
  ,"org.rocksdb" % "rocksdbjni" % "3.10.1"
//  ,"org.rocksdb" % "rocksdbjni" % "3.8.0" classifier "osx"
  ,"ch.qos.logback" % "logback-classic" % "1.1.3"
  , "commons-io"                  % "commons-io"                   % "2.4"              % "test" 
)

fork := true

//javaOptions in Test += "-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5004"

testOptions in Test += Tests.Argument("-oDS")

resolvers ++= Seq(
  // Resolver.sonatypeRepo("snapshots")
  //Resolver.typesafeRepo("snapshots")
  "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
)

scalaSource in Compile := baseDirectory.value / "src"

scalaSource in Test := baseDirectory.value / "src"

excludeFilter in (Compile, unmanagedSources) := HiddenFileFilter || "*_test.scala"

excludeFilter in (Test, unmanagedSources) := HiddenFileFilter

resourceDirectory in Compile := baseDirectory.value / "resources"

resourceDirectory in Test := baseDirectory.value / "resources_test"

seq(bintrayPublishSettings:_*)

releaseSettings
