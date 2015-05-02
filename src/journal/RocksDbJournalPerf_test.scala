package com.hamrah.akka.persistence.rocksdb
package journal

import akka.persistence.journal._
import com.typesafe.config.ConfigFactory
import akka.actor._
import akka.testkit._
import org.scalatest._
import org.rocksdb._
import java.io.File
import org.apache.commons.io.FileUtils

//@DoNotDiscover
class RocksDbJournalPerfSpec
    extends JournalSpec with JournalPerfSpec {

  lazy val config = ConfigFactory.parseString(
    s"""
      akka.test.timefactor = 3
      akka.persistence.snapshot-store.plugin = "akka.persistence.snapshot-store.local"
      akka.persistence.snapshot-store.local.dir = "target/snapshots"
      rocksdb-journal.dir = "target/journal-perf-spec"
      akka.persistence.journal.plugin = "rocksdb-journal"
    """
  )

  val storageLocations = List(
    new File(system.settings.config.getString("rocksdb-journal.dir"))
  )

  override def beforeAll() {
    super.beforeAll()
    storageLocations foreach FileUtils.deleteDirectory

  }

  override def afterAll() {
    storageLocations foreach FileUtils.deleteDirectory
    super.afterAll()

  }
}
