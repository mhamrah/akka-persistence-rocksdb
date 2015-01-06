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

class RocksDbJournalSpec
    extends JournalSpec {

  lazy val config = ConfigFactory.parseString(
    s"""
      |rocksdb-journal.dir = "target/journal-spec"
      |akka.persistence.journal.plugin = "rocksdb-journal"
    """.stripMargin
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
