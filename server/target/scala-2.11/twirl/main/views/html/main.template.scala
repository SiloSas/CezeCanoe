
package views.html

import play.twirl.api._
import play.twirl.api.TemplateMagic._


     object main_Scope0 {
import models._
import controllers._
import play.api.i18n._
import views.html._
import play.api.templates.PlayMagic._
import play.api.mvc._
import play.api.data._

class main extends BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with play.twirl.api.Template2[String,Html,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(title: String)(content: Html):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.32*/("""

"""),format.raw/*3.1*/("""<!DOCTYPE html>

<html>
  <head>
    <title>"""),_display_(/*7.13*/title),format.raw/*7.18*/("""</title>
    <link rel="stylesheet" media="screen" href=""""),_display_(/*8.50*/routes/*8.56*/.Assets.at("stylesheets/font-awesome.css")),format.raw/*8.98*/("""">
    <link rel="stylesheet" media="screen" href=""""),_display_(/*9.50*/routes/*9.56*/.Assets.at("stylesheets/main.css")),format.raw/*9.90*/("""">
    <link rel="stylesheet" media="screen" href=""""),_display_(/*10.50*/routes/*10.56*/.Assets.at("stylesheets/angular-material.css")),format.raw/*10.102*/("""">
    <link rel="stylesheet" media="screen" href=""""),_display_(/*11.50*/routes/*11.56*/.Assets.at("stylesheets/animate.min.css")),format.raw/*11.97*/("""">
    <link rel="stylesheet" media="screen" href=""""),_display_(/*12.50*/routes/*12.56*/.Assets.at("stylesheets/tools.css")),format.raw/*12.91*/("""">
    <link rel="shortcut icon" type="image/png" href=""""),_display_(/*13.55*/routes/*13.61*/.Assets.at("images/favicon.png")),format.raw/*13.93*/("""">
    <script src=""""),_display_(/*14.19*/routes/*14.25*/.Assets.at("lib/jquery/jquery.min.js")),format.raw/*14.63*/("""" type="text/javascript"></script>
    <script src=""""),_display_(/*15.19*/routes/*15.25*/.Assets.at("plugins/smartcrop.js")),format.raw/*15.59*/("""" type="text/javascript"></script>
    <script src=""""),_display_(/*16.19*/routes/*16.25*/.Assets.at("scripts/angular.js")),format.raw/*16.57*/("""" type="text/javascript"></script>
    <script src=""""),_display_(/*17.19*/routes/*17.25*/.Assets.at("plugins/smartCropAngular.js")),format.raw/*17.66*/("""" type="text/javascript"></script>
    <script src=""""),_display_(/*18.19*/routes/*18.25*/.Assets.at("plugins/angular-animate.min.js")),format.raw/*18.69*/(""""></script>
    <script src=""""),_display_(/*19.19*/routes/*19.25*/.Assets.at("plugins/angular-aria.min.js")),format.raw/*19.66*/(""""></script>
    <script src=""""),_display_(/*20.19*/routes/*20.25*/.Assets.at("plugins/angular-material.js")),format.raw/*20.66*/(""""></script>
    <script src=""""),_display_(/*21.19*/routes/*21.25*/.Assets.at("plugins/mm-foundation.js")),format.raw/*21.63*/(""""></script>
    <script src=""""),_display_(/*22.19*/routes/*22.25*/.Assets.at("plugins/angular-route.min.js")),format.raw/*22.67*/(""""></script>
    <script src=""""),_display_(/*23.19*/routes/*23.25*/.Assets.at("plugins/ng-map.min.js")),format.raw/*23.60*/(""""></script>
  </head>
  <body ng-app="app">
   """),_display_(/*26.5*/content),format.raw/*26.12*/("""
   """),_display_(/*27.5*/playscalajs/*27.16*/.html.scripts("client")),format.raw/*27.39*/("""
  """),format.raw/*28.3*/("""</body>
</html>
"""))
      }
    }
  }

  def render(title:String,content:Html): play.twirl.api.HtmlFormat.Appendable = apply(title)(content)

  def f:((String) => (Html) => play.twirl.api.HtmlFormat.Appendable) = (title) => (content) => apply(title)(content)

  def ref: this.type = this

}


}

/**/
object main extends main_Scope0.main
              /*
                  -- GENERATED --
                  DATE: Tue Dec 15 01:45:40 CET 2015
                  SOURCE: /home/loann/Desktop/CezeCanoe/server/app/views/main.scala.html
                  HASH: 17c824c7888989ae45eabc3d936d6daf10aebd3e
                  MATRIX: 530->1|655->31|683->33|754->78|779->83|863->141|877->147|939->189|1017->241|1031->247|1085->281|1164->333|1179->339|1247->385|1326->437|1341->443|1403->484|1482->536|1497->542|1553->577|1637->634|1652->640|1705->672|1753->693|1768->699|1827->737|1907->790|1922->796|1977->830|2057->883|2072->889|2125->921|2205->974|2220->980|2282->1021|2362->1074|2377->1080|2442->1124|2499->1154|2514->1160|2576->1201|2633->1231|2648->1237|2710->1278|2767->1308|2782->1314|2841->1352|2898->1382|2913->1388|2976->1430|3033->1460|3048->1466|3104->1501|3178->1549|3206->1556|3237->1561|3257->1572|3301->1595|3331->1598
                  LINES: 20->1|25->1|27->3|31->7|31->7|32->8|32->8|32->8|33->9|33->9|33->9|34->10|34->10|34->10|35->11|35->11|35->11|36->12|36->12|36->12|37->13|37->13|37->13|38->14|38->14|38->14|39->15|39->15|39->15|40->16|40->16|40->16|41->17|41->17|41->17|42->18|42->18|42->18|43->19|43->19|43->19|44->20|44->20|44->20|45->21|45->21|45->21|46->22|46->22|46->22|47->23|47->23|47->23|50->26|50->26|51->27|51->27|51->27|52->28
                  -- GENERATED --
              */
          