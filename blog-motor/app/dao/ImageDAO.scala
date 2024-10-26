package dao

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.SQLiteProfile.api._
import models.Image
import tables.ImageTable

@Singleton
class ImageDAO @Inject()(dbProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val db        = dbProvider.db
  private val images = TableQuery[ImageTable]

  def create(image: Image): Future[Long] = {
    db.run(images.returning(images.map(_.id)) += image)
  }

  def listAll(): Future[Seq[Image]] = {
    db.run(images.result)
  }

  def findById(id: Long): Future[Option[Image]] = {
    db.run(images.filter(_.id === id).result.headOption)
  }
}
