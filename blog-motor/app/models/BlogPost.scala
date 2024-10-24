package models

import play.api.libs.json.{Json, OFormat}
import java.time.OffsetDateTime

case class BlogPost(
  id: Option[Long],
  title: String,
  content: String,
  category: String,
  date: OffsetDateTime,
  thumbnailUrl: String
)

object BlogPost {
  implicit val format: OFormat[BlogPost] = Json.format[BlogPost]
}
