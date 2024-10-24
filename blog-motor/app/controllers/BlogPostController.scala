package controllers

import dao.BlogPostDAO
import models.BlogPost
import javax.inject._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BlogPostController @Inject()(cc: ControllerComponents, blogPostDAO: BlogPostDAO)
                                  (implicit ec: ExecutionContext) extends AbstractController(cc) {

  def list(): Action[AnyContent] = Action.async {
    blogPostDAO.listAll().map { posts =>
      Ok(play.api.libs.json.Json.toJson(posts))
    }
  }

  def listPaginated(page: Int): Action[AnyContent] = Action.async {
    val pageSize = 6
    for {
      total <- blogPostDAO.count()
      posts <- blogPostDAO.list(page, pageSize)
    } yield {
      val totalPages = (total + pageSize - 1) / pageSize // Calculate the total number of pages
      Ok(views.html.blogPostList(posts, page, totalPages))
    }
  }

  def get(id: Long): Action[AnyContent] = Action.async {
    blogPostDAO.findById(id).map {
      case Some(post) => Ok(play.api.libs.json.Json.toJson(post))
      case None => NotFound
    }
  }

  def create(): Action[play.api.libs.json.JsValue] = Action.async(parse.json) { request =>
    request.body.validate[BlogPost].map { postData =>
      blogPostDAO.create(postData).map { createdPost =>
        Created(play.api.libs.json.Json.toJson(createdPost))
      }
    }.getOrElse(Future.successful(BadRequest("Invalid JSON")))
  }

  def update(id: Long): Action[play.api.libs.json.JsValue] = Action.async(parse.json) { request =>
    request.body.validate[BlogPost].map { postData =>
      blogPostDAO.update(id, postData).map {
        case 0 => NotFound
        case _ => NoContent
      }
    }.getOrElse(Future.successful(BadRequest("Invalid JSON")))
  }

  def delete(id: Long): Action[AnyContent] = Action.async {
    blogPostDAO.delete(id).map {
      case 0 => NotFound
      case _ => NoContent
    }
  }
}
