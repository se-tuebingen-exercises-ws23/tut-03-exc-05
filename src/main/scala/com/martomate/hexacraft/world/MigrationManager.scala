package com.martomate.hexacraft.world

import com.flowpowered.nbt.ShortTag
import com.martomate.hexacraft.util.NBTUtil

import java.io.File
import scala.util.{Success, Try}

object MigrationManager {
  val LatestVersion: Short = 2

  // TODO: write tests for old versions by using nullable infrastructure

  def migrateIfNeeded(saveDir: File): Unit = {
    val saveFile = new File(saveDir, "world.dat")
    val nbtData = NBTUtil.loadTag(saveFile)
    val version = NBTUtil.getShort(nbtData, "version", 1)

    if (version > LatestVersion) {
      throw new IllegalArgumentException(
        s"The world saved at ${saveDir.getAbsolutePath} was saved using a too new version. " +
          s"The latest supported version is $LatestVersion but the version was $version."
      )
    }

    for (v <- version.toInt until LatestVersion) {
      migrateFrom(v, saveDir)
      nbtData.getValue.put("version", new ShortTag("version", (v + 1).toShort))
      NBTUtil.saveTag(nbtData, saveFile)
    }
  }

  /**
   * Upgrades the save file from version `fromVersion` to version `fromVersion + 1` <br><br>
   * <b>NOTE:</b> This might be irreversible!
   */
  private def migrateFrom(fromVersion: Int, saveDir: File): Unit = fromVersion match {
    case 1 => migrateFromV1(saveDir)
    case _ =>
  }

  private def migrateFromV1(saveDir: File): Unit = {
    val oldChunksDir = new File(saveDir, "chunks")
    if (oldChunksDir.isDirectory) {
      for (file <- oldChunksDir.listFiles()) {
        if (file.isFile) {
          val fileName = file.getName
          Try(fileName.substring(0, fileName.indexOf('.')).toLong) match {
            case Success(coords) =>
              val to = new File(saveDir, s"data/${coords >> 12}/${coords & 0xfff}.dat")
              if (!to.exists()) {
                to.getParentFile.mkdirs()
                if (!file.renameTo(to)) {
                  println(s"Failed to move ${file.getAbsolutePath} to ${to.getAbsolutePath}")
                }
              }
            case _ =>
          }
        }
      }
      oldChunksDir.delete()
    }
  }
}
