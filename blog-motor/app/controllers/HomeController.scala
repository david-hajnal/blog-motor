package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import models.BlogPost
import tables.BlogPostTable
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.SQLiteProfile.api._
import dao.BlogPostDAO
import dao.SettingsDAO

/** This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject() (
  val controllerComponents: ControllerComponents,
  settingsDAO: SettingsDAO,
  blogPostDAO: BlogPostDAO
)(implicit ec: ExecutionContext)
    extends BaseController {

  def listHtml(): Action[AnyContent] = Action.async {
    for {
      headerTitle <- settingsDAO.getSetting("header_title")
      categories  <- blogPostDAO.listCategories()
      posts       <- blogPostDAO.list(0, 12)
    } yield Ok(views.html.home(posts, headerTitle.getOrElse(""), categories))
  }

  def show(id: Long): Action[AnyContent] = Action.async {
    val headerTitleFuture = settingsDAO.getSetting("header_title")
    val categoriesFuture = blogPostDAO.listCategories()
    val blogPostFuture = blogPostDAO.findById(id)

    for {
      headerTitleOpt <- headerTitleFuture
      categories <- categoriesFuture
      blogPostOpt <- blogPostFuture
    } yield {
      blogPostOpt match {
        case Some(blogPost) =>
          val headerTitle = headerTitleOpt.getOrElse("")
          Ok(views.html.blogPost(blogPost, headerTitle, categories))
        case None =>
          val headerTitle = headerTitleOpt.getOrElse("")
          NotFound(views.html.notFound(headerTitle, categories))
      }
    }
  }
}
