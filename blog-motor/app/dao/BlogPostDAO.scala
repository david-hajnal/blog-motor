package dao

import models.BlogPost
import tables.BlogPostTable
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.SQLiteProfile.api._

@Singleton
class BlogPostDAO @Inject() (dbProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val db        = dbProvider.db
  private val blogPosts = BlogPostTable.blogPosts

  def listCategories(): Future[Seq[String]] = {
    db.run(blogPosts.map(_.category).distinct.result)
  }

  def list(page: Int, pageSize: Int): Future[Seq[BlogPost]] = {
    val offset = (page - 1) * pageSize
    db.run(blogPosts.sortBy(_.date.desc).drop(offset).take(pageSize).result)
  }

  def count(): Future[Int] = {
    db.run(blogPosts.length.result)
  }

  def listAll(): Future[Seq[BlogPost]] = {
    db.run(blogPosts.sortBy(_.date.desc).result)
  }

  def findById(id: Long): Future[Option[BlogPost]] =
    db.run(blogPosts.filter(_.id === id).result.headOption)

  def create(blogPost: BlogPost): Future[BlogPost] = {
    val insertQuery = (blogPosts returning blogPosts.map(_.id)
      into ((post, id) => post.copy(id = Some(id)))) += blogPost
    db.run(insertQuery)
  }

  def update(id: Long, blogPost: BlogPost): Future[Int] = {
    val updateQuery = blogPosts.filter(_.id === id)
      .map(post => (post.title, post.content, post.category, post.date))
      .update((blogPost.title, blogPost.content, blogPost.category, blogPost.date))
    db.run(updateQuery)
  }

  def delete(id: Long): Future[Int] = db.run(blogPosts.filter(_.id === id).delete)
}

