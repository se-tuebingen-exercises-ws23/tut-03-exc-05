package com.martomate.hexacraft.game

import com.martomate.hexacraft.world.CollisionDetector
import com.martomate.hexacraft.world.player.Player
import com.martomate.hexacraft.{GameKeyboard, GameMouse}
import org.lwjgl.glfw.GLFW._

class PlayerInputHandler(
  mouse: GameMouse,
  keyboard: GameKeyboard,
  player: Player,
  collisionDetector: CollisionDetector
) {
  def tick(moveWithMouse: Boolean): Unit = updatePlayer(moveWithMouse)

  // TODO: make Map[key: Int, state: Int] so that the game only receives key presses when it's not overlayed, or make this method not always be called
  private def updatePlayer(moveWithMouse: Boolean): Unit = {
    def keyPressed(key: Int): Boolean = keyboard.getKey(key) == GLFW_PRESS

    val speed =
      if keyPressed(GLFW_KEY_LEFT_CONTROL)
      then 0.075
      else if keyPressed(GLFW_KEY_LEFT_ALT)
      then 12.0
      else if keyPressed(GLFW_KEY_RIGHT_CONTROL)
      then 120.0
      else 4.3

    val rSpeed = 0.05
    if player.flying
    then player.velocity.y = 0

    val cosMove = Math.cos(player.rotation.y) * speed
    val sinMove = Math.sin(player.rotation.y) * speed

    if keyPressed(GLFW_KEY_W)
    then
      player.velocity.z -= cosMove
      player.velocity.x += sinMove

    if keyPressed(GLFW_KEY_S)
    then
      player.velocity.z += cosMove
      player.velocity.x -= sinMove

    if keyPressed(GLFW_KEY_D)
    then
      player.velocity.x += cosMove
      player.velocity.z += sinMove

    if keyPressed(GLFW_KEY_A)
    then
      player.velocity.x -= cosMove
      player.velocity.z -= sinMove

    if keyPressed(GLFW_KEY_SPACE)
    then
      if player.flying
      then player.velocity.y = speed
      else if player.velocity.y == 0
      then player.velocity.y = 5

    if keyPressed(GLFW_KEY_LEFT_SHIFT)
    then
      if player.flying
      then player.velocity.y = -speed

    if keyPressed(GLFW_KEY_UP)
    then player.rotation.x -= rSpeed

    if keyPressed(GLFW_KEY_DOWN)
    then player.rotation.x += rSpeed

    if keyPressed(GLFW_KEY_LEFT)
    then player.rotation.y -= rSpeed

    if keyPressed(GLFW_KEY_RIGHT)
    then player.rotation.y += rSpeed

    if keyPressed(GLFW_KEY_PAGE_UP)
    then player.rotation.z -= rSpeed

    if keyPressed(GLFW_KEY_PAGE_DOWN)
    then player.rotation.z += rSpeed

    if keyPressed(GLFW_KEY_R) && keyPressed(GLFW_KEY_DELETE)
    then player.rotation.set(0, 0, 0)

    if moveWithMouse
    then
      val mouseMoved = mouse.moved
      player.rotation.y += mouseMoved.x * rSpeed * 0.05
      player.rotation.x -= mouseMoved.y * rSpeed * 0.05

    if player.rotation.x < -math.Pi / 2
    then player.rotation.x += (math.Pi * 2)
    else if player.rotation.x > math.Pi / 2
    then player.rotation.x -= (math.Pi * 2)

    if player.rotation.y < 0
    then player.rotation.y += (math.Pi * 2)
    else if player.rotation.y > math.Pi * 2
    then player.rotation.y -= (math.Pi * 2)

    if player.rotation.z < 0
    then player.rotation.z += (math.Pi * 2)
    else if player.rotation.z > math.Pi * 2
    then player.rotation.z -= (math.Pi * 2)

    player.velocity.x *= 0.75
    player.velocity.z *= 0.75

    val velLen = math.hypot(player.velocity.x, player.velocity.z)
    if velLen > speed
    then
      player.velocity.x *= speed / velLen
      player.velocity.z *= speed / velLen

    if player.flying
    then player.position.add(player.velocity.x / 60, player.velocity.y / 60, player.velocity.z / 60)
    else {
      player.velocity.y -= 9.82 / 60
      player.velocity.div(60)
      val (pos, vel) = collisionDetector.positionAndVelocityAfterCollision(
        player.bounds,
        player.position,
        player.velocity
      )
      player.position.set(pos)
      player.velocity.set(vel)
      player.velocity.mul(60)
    }
  }
}
