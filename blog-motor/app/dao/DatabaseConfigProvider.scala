
package dao

import javax.inject.{Inject, Singleton}
import slick.jdbc.JdbcBackend.Database
import play.api.Configuration

@Singleton
class DatabaseConfigProvider @Inject()(config: Configuration) {
  val db: Database = Database.forConfig("slick.dbs.default.db", config.underlying)
}
