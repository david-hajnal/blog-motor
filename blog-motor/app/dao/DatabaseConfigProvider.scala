package dao

import javax.inject.{Inject, Singleton}
import slick.jdbc.JdbcBackend.Database
import play.api.Configuration
import play.api.Logger

@Singleton
class DatabaseConfigProvider @Inject()(config: Configuration) {
  private val logger = Logger(this.getClass)

  // Log the database configuration being used
  val databaseUrl = config.getOptional[String]("slick.dbs.default.db.url")
  logger.debug(s"DatabaseConfigProvider initializing with URL: ${databaseUrl.getOrElse("(no URL configured)")}")

  // Check if DATABASE_URL environment variable is set
  val envDatabaseUrl = sys.env.get("DATABASE_URL")
  logger.debug(s"DATABASE_URL environment variable: ${envDatabaseUrl.getOrElse("(not set)")}")

  // Log all database-related configuration
  val dbDriver = config.getOptional[String]("slick.dbs.default.db.driver")
  logger.debug(s"Database driver: ${dbDriver.getOrElse("(not configured)")}")

  val db: Database = Database.forConfig("slick.dbs.default.db", config.underlying)
  logger.debug(s"Database instance created successfully")
}
