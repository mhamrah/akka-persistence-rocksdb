# akka-persistence-rocksdb #

An experimental [RocksDB](http://rocksdb.org/)-based persistence store for Akka. This is an alpha version; currently it is
more of an experiment with RocksDB and Akka-Persistence than a serious storage engine for Akka-Persistence.

## Install

Add bintray to your resolvers:

```
resolvers ++= Seq(
  "hamrah-bintray" at "https://dl.bintray.com/mhamrah/maven/"
)
```

Add akka-persistence-rocksdb to your build.sbt:

```
libraryDependencies ++= Seq (
  "com.hamrah" %% "akka-persistence-rocksdb" % "0.1.1"
)
```

## Notes

* Required Akka 2.4-Snapshot, as it uses the simplified akka-persistence API
* This project uses the RocksDB-Java module which includes native builds for Windows, Linux, and OS X.
* Unlike the LevelDB default backing store, keys are not mapped to numbers and stored separately. 
* Values are serialized using the default akka-persistence serializer
* For development, this project uses a [Go-style directory layout](http://blog.michaelhamrah.com/2014/12/go-style-directory-layout-for-scala-with-sbt/)

