package controllers

import play.api.Configuration
import play.api.mvc._
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json.Json
import utils.JwtUtility
import play.api.i18n.I18nSupport

@Singleton
class AuthController @Inject() (cc: ControllerComponents, config: Configuration)(implicit
  ec: ExecutionContext
) extends AbstractController(cc) with I18nSupport {

  private val adminUsername = sys.env.getOrElse("ADMIN_USERNAME", "admin")
  private val adminPassword = sys.env.getOrElse("ADMIN_PASSWORD", "password")

  // Show login form
  def showLoginForm(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.admin.login(None))
  }

  // Handle login form submission
  def loginSubmit(): Action[AnyContent] = Action.async { implicit request =>
    val form     = request.body.asFormUrlEncoded
    val username = form.flatMap(_.get("username").flatMap(_.headOption)).getOrElse("")
    val password = form.flatMap(_.get("password").flatMap(_.headOption)).getOrElse("")

    if (username == adminUsername && password == adminPassword) {
      val token = JwtUtility.generateToken(username)(config)
      Future.successful(
        Redirect(routes.AdminController.index())
          .withSession("jwtToken" -> token) // Store JWT token in the session
          .flashing("success" -> "Login successful")
      )
    } else {
      Future.successful(
        Unauthorized(views.html.admin.login(Some("Invalid credentials. Please try again.")))
      )
    }
  }
}
