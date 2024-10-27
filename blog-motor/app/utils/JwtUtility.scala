package utils

import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim}
import scala.util.{Failure, Success, Try}
import java.time.Clock
import play.api.Configuration

object JwtUtility {

  implicit val clock: Clock = Clock.systemUTC() // Provide a default Clock

  // Generates a JWT token
  def generateToken(username: String)(implicit config: Configuration): String = {
    //val secretKey = config.get[String]("jwt.secret")
    val secretKey = "faszom"
    val claim = JwtClaim()
      .issuedNow(Clock.systemUTC)
      .expiresIn(3600) // Token expires in 1 hour
      .+("username", username)
    Jwt.encode(claim, secretKey, JwtAlgorithm.HS256)
  }

  // Verifies and decodes a JWT token
  def verifyToken(token: String)(implicit config: Configuration): Try[JwtClaim] = {
    //val secretKey = config.get[String]("jwt.secret")
    val secretKey = "faszom"
    Jwt.decode(token, secretKey, Seq(JwtAlgorithm.HS256))
  }
}
