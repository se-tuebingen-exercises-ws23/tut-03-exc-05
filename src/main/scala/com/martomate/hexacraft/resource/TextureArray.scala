package com.martomate.hexacraft.resource

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.{GL11, GL12, GL30}

object TextureArray {
  private val textures = collection.mutable.Map.empty[String, TextureArray]

  private var boundTextureArray: TextureArray = _

  def unbind(): Unit = {
    TextureArray.boundTextureArray = null
    GL11.glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, 0)
  }

  def getTextureArray(name: String): TextureArray = textures(name)

  def registerTextureArray(
    name: String,
    texSize: Int,
    images: ResourceWrapper[Seq[TextureToLoad]]
  ): TextureArray =
    if (!textures.contains(name)) new TextureArray(name, texSize, images)
    else textures(name)
}

class TextureArray(
  val name: String,
  val texSize: Int,
  wrappedImages: ResourceWrapper[Seq[TextureToLoad]]
) extends Resource
    with Texture {
  private var texID: Int = _

  TextureArray.textures += name -> this

  load()

  protected def load(): Unit = {
    val images = wrappedImages.get
    val height = texSize
    val width = texSize * images.length
    val buf = BufferUtils.createByteBuffer(height * width * 4)
    for (image <- images) {
      val pix = image.pixels
      for (j <- 0 until texSize) {
        for (i <- 0 until texSize) {
          val idx = i + j * texSize
          buf.put((pix(idx) >> 16).toByte)
          buf.put((pix(idx) >> 8).toByte)
          buf.put((pix(idx) >> 0).toByte)
          buf.put((pix(idx) >> 24).toByte)
        }
      }
    }
    buf.flip
    texID = GL11.glGenTextures()
    bind()
    GL12.glTexImage3D(
      GL30.GL_TEXTURE_2D_ARRAY,
      0,
      GL11.GL_RGBA,
      texSize,
      texSize,
      width / texSize * height / texSize,
      0,
      GL11.GL_RGBA,
      GL11.GL_UNSIGNED_BYTE,
      buf
    )

    GL11.glTexParameteri(
      GL30.GL_TEXTURE_2D_ARRAY,
      GL11.GL_TEXTURE_MIN_FILTER,
      GL11.GL_NEAREST_MIPMAP_LINEAR
    )
    GL11.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)
    GL11.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE)
    GL11.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE)
    GL30.glGenerateMipmap(GL30.GL_TEXTURE_2D_ARRAY)
  }

  protected def reload(): Unit = {
    unload()
    load()
  }

  def bind(): Unit =
    if (TextureArray.boundTextureArray != this) {
      TextureArray.boundTextureArray = this
      GL11.glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, texID)
    }

  def unload(): Unit = {
    if (TextureArray.boundTextureArray == this) TextureArray.unbind()
    GL11.glDeleteTextures(texID)
  }
}
