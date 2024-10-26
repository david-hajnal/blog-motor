package controllers

import dao.BlogPostDAO
import dao.SettingsDAO
import dao.ImageDAO
import forms.SettingsForm
import models.BlogPost
import models.Image
import javax.inject._
import play.api.data._
import play.api.data.Forms._
import play.api.i18n.I18nSupport
import play.api.mvc._
import play.api.libs.Files.TemporaryFile
import java.time.OffsetDateTime
import scala.concurrent.{ExecutionContext, Future}
import java.text.Normalizer

@Singleton
class AdminController @Inject() (
  cc: ControllerComponents,
  blogPostDAO: BlogPostDAO,
  imageDAO: ImageDAO,
  settingsDAO: SettingsDAO
)(implicit
  ec: ExecutionContext
) extends AbstractController(cc)
    with I18nSupport {

  private val tinyMceApiKey: String = sys.env.getOrElse("TINYMCE_API_KEY", "no-api-key")

  val blogPostForm: Form[BlogPost] = Form(
    mapping(
      "id"           -> optional(longNumber),
      "title"        -> nonEmptyText,
      "content"      -> nonEmptyText,
      "slug"         -> nonEmptyText,
      "category"     -> nonEmptyText,
      "date"         -> ignored(OffsetDateTime.now),
      "thumbnailUrl" -> nonEmptyText
    )(BlogPost.apply)(BlogPost.unapply)
  )

  def index(): Action[AnyContent] = Action.async { implicit request =>
    settingsDAO.getSetting("headerTitle").map { headerTitleOpt =>
      Ok(views.html.admin.index(headerTitleOpt.getOrElse("")))
    }
  }

  def listPosts(): Action[AnyContent] = Action.async { implicit request =>
    blogPostDAO.listAll().map { posts =>
      Ok(views.html.admin.listPosts(posts))
    }
  }

  def newPost(): Action[AnyContent] = Action.async { implicit request =>
    imageDAO.listAll().map { images =>
      Ok(views.html.admin.newPost(blogPostForm, tinyMceApiKey, images))
    }
  }

  private val empty: Seq[Image] = Seq.empty[Image]

  def createPost(): Action[AnyContent] = Action.async { implicit request =>
    blogPostForm.bindFromRequest.fold(
      formWithErrors =>
        Future.successful(
          BadRequest(views.html.admin.newPost(formWithErrors, tinyMceApiKey, empty))
        ),
      blogPostData => {
        val slug    = SlugUtils.generateSlug(blogPostData.title)
        val content = HtmlEntityReplacer.replaceHtmlEntities(blogPostData.content)
        val newPost = blogPostData.copy(slug = slug, content = content, date = OffsetDateTime.now)
        blogPostDAO.create(newPost).map { _ =>
          Redirect(routes.AdminController.listPosts()).flashing("success" -> "Blog post created")
        }
      }
    )
  }

  def deletePost(id: Long): Action[AnyContent] = Action.async { implicit request =>
    blogPostDAO.deleteById(id).map { result =>
      if (result > 0) {
        Redirect(routes.AdminController.listPosts())
          .flashing("success" -> "Post deleted successfully")
      } else {
        Redirect(routes.AdminController.listPosts()).flashing("error" -> "Post not found")
      }
    }
  }

  def editPost(id: Long): Action[AnyContent] = Action.async { implicit request =>
    for {
      postOpt <- blogPostDAO.findById(id)
      images  <- imageDAO.listAll()
    } yield postOpt match {
      case Some(post) =>
        Ok(views.html.admin.editPost(id, blogPostForm.fill(post), tinyMceApiKey, images))
      case None => NotFound("Blog post not found")
    }
  }

  def updatePost(id: Long): Action[AnyContent] = Action.async { implicit request =>
    blogPostForm.bindFromRequest.fold(
      formWithErrors =>
        Future.successful(
          BadRequest(views.html.admin.editPost(id, formWithErrors, tinyMceApiKey, empty))
        ),
      blogPostData => {
        val slug        = SlugUtils.generateSlug(blogPostData.title)
        val content     = HtmlEntityReplacer.replaceHtmlEntities(blogPostData.content)
        val updatedPost = blogPostData.copy(slug = slug, content = content)
        blogPostDAO.update(id, updatedPost).map { _ =>
          Redirect(routes.AdminController.listPosts()).flashing("success" -> "Blog post updated")
        }
      }
    )
  }

  def viewPost(id: Long): Action[AnyContent] = Action.async { implicit request =>
    blogPostDAO.findById(id).map {
      case Some(post) => Ok(views.html.admin.viewPost(post))
      case None       => NotFound("Blog post not found")
    }
  }

  def showUploadImageForm(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.admin.uploadImage())
  }

  def uploadImage(): Action[MultipartFormData[TemporaryFile]] =
    Action.async(parse.multipartFormData) { implicit request =>
      request.body
        .file("image")
        .map { imageFile =>
          val imageName = imageFile.filename
          val mimeType  = imageFile.contentType.getOrElse("application/octet-stream")
          val imageData = java.nio.file.Files.readAllBytes(imageFile.ref.path)

          val image = Image(None, imageName, mimeType, imageData)
          imageDAO.create(image).map { _ =>
            Redirect(routes.AdminController.listPosts())
              .flashing("success" -> "Image uploaded successfully")
          }
        }
        .getOrElse {
          Future.successful(BadRequest("Missing file"))
        }
    }

  def editSettings(): Action[AnyContent] = Action.async { implicit request =>
    settingsDAO.getSetting("headerTitle").map { headerTitleOpt =>
      val settingsForm =
        SettingsForm.form.fill(SettingsForm(headerTitleOpt.getOrElse("Default Title")))
      Ok(views.html.admin.editSettings(settingsForm))
    }
  }

  // Handle settings form submission
  def updateSettings(): Action[AnyContent] = Action.async { implicit request =>
    SettingsForm.form.bindFromRequest.fold(
      formWithErrors =>
        Future.successful(BadRequest(views.html.admin.editSettings(formWithErrors))),
      settingsData =>
        settingsDAO.updateSetting("headerTitle", settingsData.headerTitle).map { _ =>
          Redirect(routes.AdminController.editSettings())
            .flashing("success" -> "Settings updated successfully")
        }
    )
  }

  object SlugUtils {
    def generateSlug(title: String): String =
      Normalizer
        .normalize(title, Normalizer.Form.NFD)
        .replaceAll("[^\\p{ASCII}]", "") // Remove diacritics
        .toLowerCase
        .replaceAll("[^a-z0-9\\s-]", "") // Remove special characters
        .trim
        .replaceAll("\\s+", "-") // Replace spaces with hyphens
  }

  object HtmlEntityReplacer {

    def replaceHtmlEntities(html: String): String =
      html
        .replaceAll("&lt;", "<")
        .replaceAll("&gt;", ">")
        .replaceAll("&amp;", "&")
        .replaceAll("&quot;", "\"")
        .replaceAll("&#39;", "'")
  }

}
