package com.martomate.hexacraft.world.entity.player

import com.flowpowered.nbt.{CompoundTag, StringTag, Tag}
import com.martomate.hexacraft.util.{CylinderSize, NBTUtil}
import com.martomate.hexacraft.world.{BlocksInWorld, CollisionDetector}
import com.martomate.hexacraft.world.block.HexBox
import com.martomate.hexacraft.world.coord.fp.CylCoords
import com.martomate.hexacraft.world.entity.EntityModel
import com.martomate.hexacraft.world.entity.ai.{EntityAI, EntityAIFactory}
import com.martomate.hexacraft.world.entity.base.BasicEntity

class PlayerEntity(
  _model: EntityModel,
  world: BlocksInWorld,
  aiFactory: EntityAIFactory[PlayerEntity]
)(implicit cylSizeImpl: CylinderSize)
    extends BasicEntity(_model) {
  override val boundingBox: HexBox = new HexBox(0.2f, 0, 1.75f)

  override def id: String = "player"

  private val ai: EntityAI = aiFactory.makeEntityAI(world, this)

  override def tick(collisionDetector: CollisionDetector): Unit = {
    ai.tick()
    velocity.add(ai.acceleration())

    velocity.x *= 0.9
    velocity.z *= 0.9

    super.tick(collisionDetector)
  }

  override def fromNBT(tag: CompoundTag): Unit = {
    super.fromNBT(tag)
    NBTUtil.getCompoundTag(tag, "ai").foreach(aiTag => ai.fromNBT(aiTag))
  }

  override def toNBT: Seq[Tag[_]] =
    super.toNBT :+ new StringTag("type", "player") :+ NBTUtil.makeCompoundTag("ai", ai.toNBT)
}

object PlayerEntity {
  def atStartPos(
    pos: CylCoords,
    world: BlocksInWorld,
    aiFactory: EntityAIFactory[PlayerEntity],
    model: EntityModel
  )(implicit cylSizeImpl: CylinderSize): PlayerEntity = {
    val pl = new PlayerEntity(model, world, aiFactory)
    pl.position = pos
    pl
  }
}
