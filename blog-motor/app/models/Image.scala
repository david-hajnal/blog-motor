package models

case class Image(id: Option[Long], name: String, mimeType: String, data: Array[Byte])
