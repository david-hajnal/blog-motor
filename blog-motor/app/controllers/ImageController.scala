package controllers

import dao.ImageDAO
import play.api.mvc._

import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton
class ImageController @Inject()(cc: ControllerComponents, imageDAO: ImageDAO)(implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  def getImage(id: Long): Action[AnyContent] = Action.async { implicit request =>
    imageDAO.findById(id).map {
      case Some(image) =>
        Ok(image.data).as("image/jpg") // Set MIME type based on your image type (e.g., "image/jpeg" for JPEG)
      case None =>
        NotFound("Image not found")
    }
  }
}
