import scala.util.Properties.isMac

enablePlugins(LauncherJarPlugin)

lazy val root = Project("hexacraft", file("."))
  .settings(Defaults.coreDefaultSettings: _*)
  .settings(mainSettings: _*)

def mainSettings: Seq[Def.SettingsDefinition] = Seq(
  name := "Hexacraft",
  organization := "com.martomate",
  version := "0.10",
  scalaVersion := "3.3.1",
  scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature"),
  javaOptions ++= (if (isMac) Some("-XstartOnFirstThread") else None),
  publishArtifact := false,
  libraryDependencies ++= lwjglDependencies ++ otherDependencies,
  logBuffered := false,
  fork := true
)

def lwjglDependencies = {
  val lwjglVersion = "3.3.3"

  val platforms = Seq(
    "natives-windows",
    "natives-windows-arm64",
    "natives-linux",
    "natives-linux-arm64",
    "natives-macos",
    "natives-macos-arm64"
  )

  Seq(
    "org.lwjgl" % "lwjgl" % lwjglVersion,
    "org.lwjgl" % "lwjgl-glfw" % lwjglVersion,
    "org.lwjgl" % "lwjgl-opengl" % lwjglVersion
  ) ++ platforms.flatMap(platform =>
    Seq(
      "org.lwjgl" % "lwjgl" % lwjglVersion classifier platform,
      "org.lwjgl" % "lwjgl-glfw" % lwjglVersion classifier platform,
      "org.lwjgl" % "lwjgl-opengl" % lwjglVersion classifier platform
    )
  )
}

def otherDependencies = Seq(
  "org.joml" % "joml" % "1.10.4",
  "com.eclipsesource.minimal-json" % "minimal-json" % "0.9.5",
  "com.flowpowered" % "flow-nbt" % "1.0.0"
)
