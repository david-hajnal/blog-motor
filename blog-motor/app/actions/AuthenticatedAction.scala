package actions

import javax.inject.Inject
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}
import utils.JwtUtility
import scala.util.{Failure, Success}
import play.api.Configuration
import javax.inject.Inject
import play.api.mvc._
import play.api.Configuration
import scala.concurrent.{ExecutionContext, Future}
import utils.JwtUtility
import scala.util.{Failure, Success}
import controllers.routes // Import this to access routes

class AuthenticatedAction @Inject() (parser: BodyParsers.Default, config: Configuration)(implicit
  ec: ExecutionContext
) extends ActionBuilderImpl(parser) {

  override def invokeBlock[A](
    request: Request[A],
    block: Request[A] => Future[Result]
  ): Future[Result] =
    request.session
      .get("jwtToken")
      .map { token =>
        JwtUtility.verifyToken(token)(config) match {
          case Success(_) => block(request)
          case Failure(_) => Future.successful(Results.Unauthorized("Invalid or expired token"))
        }
      }
      .getOrElse {
        Future.successful(
          Results
            .Redirect(routes.AuthController.showLoginForm())
            .flashing("error" -> "Please log in")
        )
      }
}
