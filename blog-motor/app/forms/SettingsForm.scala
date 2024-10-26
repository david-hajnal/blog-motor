package forms

import play.api.data._
import play.api.data.Forms._

case class SettingsForm(headerTitle: String)

object SettingsForm {
  val form: Form[SettingsForm] = Form(
    mapping(
      "headerTitle" -> nonEmptyText
    )(SettingsForm.apply)(SettingsForm.unapply)
  )
}
