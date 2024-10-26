package tables

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.SQLiteProfile.api._
import models.Image

class ImageTable(tag: Tag) extends Table[Image](tag, "images") {
  def id       = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name     = column[String]("name")
  def mimeType = column[String]("mime_type")
  def data     = column[Array[Byte]]("data")

  def * = (id.?, name, mimeType, data) <> ((Image.apply _).tupled, Image.unapply)
}
