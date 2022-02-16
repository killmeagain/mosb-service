ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.7"

lazy val root = (project in file("."))
  .settings(
    name := "mosb-service"
  )

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.6.17",
  "com.typesafe.akka" %% "akka-stream" % "2.6.17",
  "com.typesafe.akka" %% "akka-http" % "10.2.7",

  "com.google.inject" % "guice" % "5.0.1",
  "org.liquibase" % "liquibase-core" % "4.6.2",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
  "com.typesafe.slick" %% "slick" % "3.3.3",
  "com.github.tminglei" %% "slick-pg" % "0.20.2",
  "com.github.tminglei" %% "slick-pg_play-json" % "0.20.2",
  "org.postgresql" % "postgresql" % "42.3.1",
  "com.typesafe" % "config" % "1.4.0",

  "com.typesafe.play" %% "play-json" % "2.10.0-RC5",
  "de.heikoseeberger" %% "akka-http-play-json" % "1.39.2",

)
