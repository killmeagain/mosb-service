package ru.scala.learning.utils

import java.sql.{Connection, DriverManager}

import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor

trait Liquibase {

  def performMigrations(lbConfig: LiquibaseConfig): Unit
}

object Liquibase {

  def performMigrations(lbConfig: LiquibaseConfig): Unit = {

    val connection = DriverManager.getConnection(lbConfig.url, lbConfig.user, lbConfig.password)
    val liquibase = getLiquibase(lbConfig.migrationsXmlPathRelative, connection)
    try {
      liquibase.update("dev")
    } finally {
      connection.close()
    }
  }

  private def getLiquibase(migrationsXmlPathRelative: String, connection: Connection): liquibase.Liquibase = {
    val classLoaderAccessor = new ClassLoaderResourceAccessor(Thread.currentThread().getContextClassLoader)
    new liquibase.Liquibase(migrationsXmlPathRelative, classLoaderAccessor, new JdbcConnection(connection))
  }
}

case class LiquibaseConfig(url: String,
                           user: String,
                           password: String,
                           migrationsXmlPathRelative: String)