package controllers

import dao.ImageDAO
import play.api.mvc._

import javax.inject._
import scala.concurrent.ExecutionContext
<<<<<<< HEAD
import actions.AuthenticatedAction
import play.api.i18n.I18nSupport

@Singleton
class ImageController @Inject()(cc: ControllerComponents, imageDAO: ImageDAO, authenticatedAction: AuthenticatedAction)(implicit ec: ExecutionContext)
  extends AbstractController(cc) with I18nSupport {
=======

@Singleton
class ImageController @Inject()(cc: ControllerComponents, imageDAO: ImageDAO)(implicit ec: ExecutionContext)
  extends AbstractController(cc) {
>>>>>>> a7bc41d (fix image load and save, docker fixes)

  def getImage(id: Long): Action[AnyContent] = Action.async { implicit request =>
    imageDAO.findById(id).map {
      case Some(image) =>
        Ok(image.data).as("image/jpg") // Set MIME type based on your image type (e.g., "image/jpeg" for JPEG)
      case None =>
        NotFound("Image not found")
    }
  }
<<<<<<< HEAD

  def deleteImage(id: Long): Action[AnyContent] = Action.async { implicit request =>
    imageDAO.deleteById(id).map { _ =>
      Redirect(routes.ImageController.listImages())
    }
  }

  def listImages(): Action[AnyContent] = authenticatedAction.async { implicit request =>
    imageDAO.listAll().map { images =>
      Ok(views.html.admin.listImages(images))
    }
  }
=======
>>>>>>> a7bc41d (fix image load and save, docker fixes)
}
