package tables

import models.BlogPost
import slick.jdbc.SQLiteProfile.api._
import java.time.OffsetDateTime
import slick.jdbc.JdbcType

class BlogPostTable(tag: Tag) extends Table[BlogPost](tag, "blog_posts") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def title = column[String]("title")
  def content = column[String]("content")
  def slug = column[String]("slug")
  def category = column[String]("category")
  def date = column[OffsetDateTime]("date")
  def thumbnailUrl = column[String]("thumbnail_url")

  def * = (id.?, title, content, slug, category, date, thumbnailUrl) <> ((BlogPost.apply _).tupled, BlogPost.unapply)
}

object BlogPostTable {
  val blogPosts = TableQuery[BlogPostTable]

  implicit val offsetDateTimeColumnType: JdbcType[OffsetDateTime] = MappedColumnType.base[OffsetDateTime, String](
    _.toString, // Convert OffsetDateTime to String for storage
    OffsetDateTime.parse // Parse String to OffsetDateTime when reading from DB
  )
}
