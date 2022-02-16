package ru.scala.learning

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import com.google.inject.{AbstractModule, Guice}
import ru.scala.learning.conf.Config
import ru.scala.learning.utils.{Liquibase, LiquibaseConfig}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import scala.io.StdIn
import slick.jdbc.JdbcBackend._

object Server {
    def main(args: Array[String]): Unit = {
        val ec = ExecutionContext.global
        val system = ActorSystem("mosb-service", defaultExecutionContext = Some(ec))
        val slickDb = initSlick(Config.dbConfigName)

        initGuice(system, ec, slickDb)
        performMigrations(Config.dbUrl, Config.dbUser, Config.dbPassword, Config.migrations)

        runServer(Config.port, Some("localhost"))
    }

    private def runServer(port: Int, listenFrom: Option[String]): Future[ServerBinding] = {

        implicit val ec: ExecutionContext = inject[ExecutionContext]
        implicit val system: ActorSystem = inject[ActorSystem]

        val listenFromAnyIp = "0.0.0.0"
        val bindingFuture = Http().newServerAt(
            interface = listenFrom.getOrElse(listenFromAnyIp),
            port = port
        ).bindFlow()


        bindingFuture.onComplete {
            case Success(_) =>
                val ipRestriction = listenFrom.filterNot(_ == listenFromAnyIp).map(ip => s"(only from '$ip') ").getOrElse("")
            case Failure(ex) => ()
        }


        println(s"Server now online. Please navigate to http://localhost:${port}/hello\nPress RETURN to stop...")

        bindingFuture
    }

    private def performMigrations(dbUrl: String, dbUser: String, dbPassword: String, migrationsFilePath: String): Unit = {
        Liquibase.performMigrations(LiquibaseConfig(dbUrl, dbUser, dbPassword, migrationsFilePath))
    }

    private def initGuice(system: ActorSystem,
                          ec: ExecutionContext,
                          slickDb: Database): Unit = {
        val injector = Guice.createInjector(new AbstractModule() {
            override def configure(): Unit = {
                bind(classOf[ActorSystem]).toInstance(system)
                bind(classOf[ExecutionContext]).toInstance(ec)
                bind(classOf[Database]).toInstance(slickDb)
            }
        })
        //Записываем injector в статическое поле объекта, чтобы иметь к нему доступ в классах,
        // которые инициализирует не guice
        shareInjector(injector)
    }

    private def initSlick(dbConfigName: String): Database = {
        Database.forConfig(dbConfigName)
    }
}

