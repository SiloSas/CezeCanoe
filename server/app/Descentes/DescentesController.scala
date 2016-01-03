package Descentes

import java.io.{ByteArrayOutputStream, File}
import javax.imageio.ImageIO
import javax.inject.Inject

import play.Play
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc.{Action, _}
import upickle.default._

import scala.concurrent.ExecutionContext.Implicits.global

class DescentesController @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends Controller {

  def getImage(fileName: String) = Action {
    val imageFile = new File(Play.application().path().getPath + "/public/images/" + fileName)
    val image = ImageIO.read(imageFile)
    if (imageFile.length > 0) {

      val resourceType = fileName.substring(fileName.length()-3)
      val baos = new ByteArrayOutputStream()
      ImageIO.write(image, resourceType, baos)

      Ok(baos.toByteArray).as("image/" + resourceType)
      //resource type such as image+png, image+jpg
    } else {
      NotFound(fileName)
    }
  }

  def uploadImage = Action(parse.multipartFormData) { request =>
    request.body.file("picture").map { image =>
      image.contentType match {
        case Some(fileExtension)  =>

          println(image)
          val filename = image.filename
          image.ref.moveTo(new File(Play.application().path().getPath + "/public/images/" + filename), replace = true)

          Ok("images/" +filename)

        case _ =>
          Unauthorized("Wrong content type")
      }
    }.getOrElse { BadRequest }
  }
}