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

  private val logger = Logger(this.getClass)

  def listHtml(): Action[AnyContent] = Action.async {
    for {
      headerTitle <- settingsDAO.getSetting("headerTitle")
      categories  <- blogPostDAO.listCategories()
      posts       <- blogPostDAO.list(0, 120)
    } yield {
      logger.debug(s"HeaderTitle for listHtml: ${headerTitle.getOrElse("(empty)")}")
      Ok(views.html.home(posts, headerTitle.getOrElse(""), categories))
    }
  }

  def show(slug: String): Action[AnyContent] = Action.async { implicit request =>
    val headerTitleFuture = settingsDAO.getSetting("headerTitle")
    val categoriesFuture  = blogPostDAO.listCategories()
    val blogPostFuture    = blogPostDAO.findBySlug(slug)

    for {
      headerTitleOpt <- headerTitleFuture
      categories     <- categoriesFuture
      blogPostOpt    <- blogPostFuture
    } yield {
      logger.debug(s"HeaderTitle for show($slug): ${headerTitleOpt.getOrElse("(empty)")}")
      blogPostOpt match {
        case Some(blogPost) =>
          val headerTitle = headerTitleOpt.getOrElse("Blog")
          Ok(views.html.blogPost(blogPost, headerTitle, categories))
        case None =>
          NotFound(views.html.notFound(headerTitleOpt.getOrElse("Blog"), categories))
      }
    }
  }
}
