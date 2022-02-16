package ru.scala.learning.conf

import com.typesafe.config.{Config, ConfigFactory}

object Config {

  val config: Config = ConfigFactory.load();

  val port: Int = config.getInt("port")

  val dbConfigName: String = "db"
  val dbUrl: String = config.getString(s"$dbConfigName.url")
  val dbUser: String = config.getString(s"$dbConfigName.user")
  val dbPassword: String = config.getString(s"$dbConfigName.password")
  val migrations: String = config.getString("migrations")

}
