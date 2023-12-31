package com.martomate.hexacraft.world.chunk

import com.flowpowered.nbt.{ByteTag, CompoundTag, Tag}
import com.martomate.hexacraft.util.{CylinderSize, NBTUtil}
import com.martomate.hexacraft.world.BlocksInWorld
import com.martomate.hexacraft.world.chunk.storage.{ChunkStorage, DenseChunkStorage, SparseChunkStorage}
import com.martomate.hexacraft.world.entity.EntityRegistry

class ChunkData(init_storage: ChunkStorage, world: BlocksInWorld, registry: EntityRegistry)(implicit
  cylSize: CylinderSize
) {
  var storage: ChunkStorage = init_storage
  val entities: EntitiesInChunk = new EntitiesInChunk(world, registry)
  var isDecorated: Boolean = false

  def optimizeStorage(): Unit =
    if (storage.isDense) {
      if (storage.numBlocks < 32) {
        storage = new SparseChunkStorage(storage)
      }
    } else {
      if (storage.numBlocks > 48) {
        storage = new DenseChunkStorage(storage)
      }
    }

  def fromNBT(nbt: CompoundTag): Unit = {
    storage.fromNBT(nbt)
    entities.fromNBT(nbt)
    isDecorated = NBTUtil.getBoolean(nbt, "isDecorated", default = false)
  }

  def toNBT: Seq[Tag[_]] =
    storage.toNBT ++ entities.toNBT :+ new ByteTag("isDecorated", isDecorated)
}
