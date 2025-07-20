package dao

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.SQLiteProfile.api._
import play.api.Logger

@Singleton
class SettingsDAO @Inject()(dbProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val db = dbProvider.db
  private val logger = Logger(this.getClass)

  // Log database initialization
  logger.debug(s"SettingsDAO initialized with database: ${dbProvider.toString}")

  private class SettingsTable(tag: Tag) extends Table[(String, String)](tag, "settings") {
    def key = column[String]("key", O.PrimaryKey)
    def value = column[String]("value")

    def * = (key, value)
  }

  private val settings = TableQuery[SettingsTable]

  def getSetting(key: String): Future[Option[String]] = {
    logger.debug(s"SettingsDAO.getSetting called for key: '$key'")
    val query = settings.filter(_.key === key).map(_.value)
    logger.debug(s"Generated SQL query for key '$key': ${query.result.statements.headOption.getOrElse("No statement")}")

    val result = db.run(query.result.headOption)
    result.foreach { value =>
      logger.debug(s"SettingsDAO.getSetting result for key '$key': ${value.map(v => s"'$v'").getOrElse("None")}")
    }
    result.recover {
      case ex =>
        logger.error(s"Error getting setting for key '$key': ${ex.getMessage}", ex)
        None
    }
  }

  def updateSetting(key: String, value: String): Future[Int] = {
    logger.debug(s"SettingsDAO.updateSetting called for key: '$key', value: '$value'")
    val result = db.run(settings.insertOrUpdate((key, value)))
    result.foreach { rowsAffected =>
      logger.debug(s"SettingsDAO.updateSetting for key '$key' affected $rowsAffected rows")
    }
    result.recover {
      case ex =>
        logger.error(s"Error updating setting for key '$key': ${ex.getMessage}", ex)
        0
    }
  }
}
