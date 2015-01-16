package com.hamrah.akka.persistence.rocksdb
package journal

import akka.persistence._
import akka.persistence.journal.JournalPerfSpec.Cmd
import akka.persistence.journal._
//import akka.persistence.snapshot._
import com.typesafe.config._

import scala.collection._
import scala.concurrent.Future
import scala.concurrent.duration._
import org.rocksdb._

import akka.actor.ActorLogging
import akka.serialization.SerializationExtension

class RocksDBJournal extends AsyncWriteJournal with ActorLogging {

  val config = new RocksDBJournalConfig(context.system.settings.config.getConfig("rocksdb-journal"))

  import context.dispatcher

  val serialization = SerializationExtension(context.system)
  val options = new Options()
  val o2 = options.setCreateIfMissing(true)
  log.info(s"opening db ${config.dir}")
  val rocksDB = RocksDB.open(o2, config.dir)

  def getKey(m: PersistentRepr): Array[Byte] = {
    getKey(m.persistenceId, m.sequenceNr)
  }
  def getKey(persistenceId: String, sequenceNr: Long, deleted: Boolean = false) = {
    (persistenceId + ":" + "%019d".format(sequenceNr) + ":" + deleted).getBytes
  }
  def parseKey(key: Array[Byte]): Seq[String] = {
    new String(key).split(":")
  }

  /* atomic write of all messages */
  def asyncWriteMessages(messages: immutable.Seq[PersistentRepr]): Future[Unit] = {
    Future {
      val batch = new WriteBatch()

      messages.foreach { m =>
        batch.put(getKey(m), serialization.serialize(m).get)
      }
      rocksDB.write(new WriteOptions(), batch)
      batch.dispose()
    }
  }

  def asyncDeleteMessagesTo(persistenceId: String, toSequenceNr: Long, permanent: Boolean): Future[Unit] = {
    Future {
      val iter = rocksDB.newIterator

      var seq = 1L
      iter.seek(getKey(persistenceId, seq))
      var continue = true

      while (iter.isValid && continue && seq <= toSequenceNr) {
        val parts = parseKey(iter.key)
        if (parts(0) == persistenceId) {

          rocksDB.remove(iter.key)
          if (!permanent) {
            rocksDB.put(getKey(parts(0), parts(1).toLong, true), iter.value)
          }
        } else {
          continue = false
        }
        seq += 1
        iter.next()
      }

    }
  }

  def asyncReplayMessages(persistenceId: String, fromSequenceNr: Long, toSequenceNr: Long, max: Long)(replayCallback: PersistentRepr ⇒ Unit): Future[Unit] = {
    Future {

      val iter = rocksDB.newIterator
      iter.seek(getKey(persistenceId, fromSequenceNr))
      var continue = true
      var seq: Long = fromSequenceNr
      var count: Long = 0
      while (iter.isValid && continue && max != 0 && count < max && (fromSequenceNr + count <= toSequenceNr)) {

        val parts = parseKey(iter.key)
        seq = parts(1).toLong

        if (parts(0) == persistenceId) {
          val payload = serialization.deserialize(iter.value, classOf[PersistentRepr]).get.update(deleted = parts(2).toBoolean)
          replayCallback(payload)
          continue = (parts.head == persistenceId)
        } else {
          continue = false
        }
        count += 1
        iter.next()
      }

    }
  }

  def asyncReadHighestSequenceNr(persistenceId: String, fromSequenceNr: Long): Future[Long] = {
    Future {
      var seq: Long = 0
      val iter = rocksDB.newIterator
      iter.seek(persistenceId.getBytes)
      var continue = true
      while (iter.isValid && continue) {
        val parts = parseKey(iter.key)
        if (parts(0) == persistenceId) {
          seq = parts(1).toLong
          continue = parts.head == persistenceId
        } else {
          continue = false
        }
        iter.next()
      }
      seq
    }
  }
}

