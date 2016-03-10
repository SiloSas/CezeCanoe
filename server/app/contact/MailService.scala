package contact

import javax.inject.Inject

import play.api.libs.json.JsObject
import play.api.libs.mailer.{Email, MailerClient}
import play.api.mvc.{Action, Controller}


class MailService @Inject() (mailerClient: MailerClient, recipient: String) extends Controller {

  val destinationMail = Seq("<romeo.cezecanoes@gmail.com >")

  def sendNotificationMail() = Action(parse.json) { request =>
    val content = request.body.as[JsObject].toString()
      .replaceAll("\",\"", "<br/>")
      .replaceAll("\":\"", " : ")
      .replaceAll("\n", "<br/>")
      .replaceFirst("{\"", "")
    val email = Email(
      subject = "CezeCanoe : vous avez une notification",
      from = "romeo.cezecanoes@gmail.com",
      to = destinationMail,
      bodyHtml = Some(
        s"""<html><body><p>$content</body></html>""".stripMargin)
    )
    mailerClient.send(email)
    Ok("ok")
  }
}