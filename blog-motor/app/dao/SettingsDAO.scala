package dao

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.SQLiteProfile.api._

@Singleton
class SettingsDAO @Inject()(dbProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val db        = dbProvider.db

  private class SettingsTable(tag: Tag) extends Table[(String, String)](tag, "settings") {
    def key = column[String]("key", O.PrimaryKey)
    def value = column[String]("value")

    def * = (key, value)
  }

  private val settings = TableQuery[SettingsTable]

  def getSetting(key: String): Future[Option[String]] = {
    db.run(settings.filter(_.key === key).map(_.value).result.headOption)
  }
}
