name := """blog-motor"""
organization := "space.hajnal"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.15"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test
libraryDependencies ++= Seq(
  guice,
  "com.typesafe.play" %% "play-slick" % "5.1.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "5.1.0",
  "org.xerial" % "sqlite-jdbc" % "3.46.1.3",
  "com.typesafe.slick" %% "slick" % "3.4.1",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.4.1"
)
libraryDependencies += "com.github.jwt-scala" %% "jwt-play" % "9.0.5"
dependencyOverrides += "org.scala-lang.modules" %% "scala-xml" % "2.2.0"


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "space.hajnal.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "space.hajnal.binders._"
