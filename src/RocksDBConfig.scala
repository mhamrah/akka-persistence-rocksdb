package com.hamrah.akka.persistence.rocksdb

import com.typesafe.config._

class RocksDBJournalConfig(config: Config) {

  val dir: String = config.getString("dir")

}
