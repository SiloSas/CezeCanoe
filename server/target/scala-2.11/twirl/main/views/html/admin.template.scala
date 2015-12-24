
package views.html

import play.twirl.api._
import play.twirl.api.TemplateMagic._


     object admin_Scope0 {
import models._
import controllers._
import play.api.i18n._
import views.html._
import play.api.templates.PlayMagic._
import play.api.mvc._
import play.api.data._

class admin extends BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with play.twirl.api.Template1[String,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(message: String):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.19*/("""

"""),_display_(/*3.2*/main("Admin")/*3.15*/ {_display_(Seq[Any](format.raw/*3.17*/("""

    """),format.raw/*5.157*/("""
        """),format.raw/*6.48*/("""
    """),format.raw/*7.22*/("""
    """),format.raw/*8.91*/("""


        """),format.raw/*11.138*/("""
            """),format.raw/*12.30*/("""
            """),format.raw/*13.68*/("""
        """),format.raw/*14.26*/("""

    """),format.raw/*16.15*/("""
    """),format.raw/*17.5*/("""<section layout="row" flex class="height100p" ng-cloak data-ng-controller="adminController">
        <md-sidenav class="md-sidenav-left md-whiteframe-z2" md-component-id="left" md-is-locked-open="$mdMedia('gt-md')">
            <md-toolbar class="md-theme-indigo" style="background: #B4D5B8;">
                <h1 class="md-toolbar-tools">Admin</h1>
            </md-toolbar>
            <md-content layout-padding >
                <md-button ng-click="close()" class="md-primary" hide-gt-md>
                    Close Sidenav Left
                </md-button>
                <h4 data-ng-click="show = 'descentes'">Descentes <i class="float-right fa fa-eye"></i> </h4>
                <div data-ng-show="show == 'descentes'">
                    <div class="column md-whiteframe-z1" data-ng-repeat="descente in descentes">
                        <h5>"""),format.raw/*29.29*/("""{"""),format.raw/*29.30*/("""{"""),format.raw/*29.31*/("""descente.name"""),format.raw/*29.44*/("""}"""),format.raw/*29.45*/("""}"""),format.raw/*29.46*/("""</h5>
                            <md-button class="md-fab md-mini float-right" ng-click="setDescente(descente)"><i class="fa fa-pencil"></i></md-button>
                            """),format.raw/*31.118*/("""
                    """),format.raw/*32.21*/("""</div>
                    """),format.raw/*33.96*/("""
                """),format.raw/*34.17*/("""</div>
            </md-content>
        </md-sidenav>
        <md-content style="overflow: hidden; height: inherit" flex>
            <md-toolbar  style="background: #B4D5B8;">
            <div class="md-toolbar-tools" >
            <md-button aria-label="menu" class="md-fab md-primary md-mini" ng-click="directive.toggleLeft()" hide-gt-md>
                <md-icon md-svg-src="assets/images/menu.svg"></md-icon>
            </md-button>
            <object data="assets/images/title.svg"
            class="column medium-3 large-2 small-8 animated fadeIn"></object>
                <div ng-init="$root.lang = '[lang_Fr]'">
                    <div data-ng-click="$root.lang = '[lang_Fr]'"> <h2>Fr</h2></div>
                    <div data-ng-click="$root.lang = '[lang_En]'"> <h2>En</h2></div>
                </div>
                </div>
            </md-toolbar>
            <md-content layout="column" layout-fill layout-align="top center">
                <div ng-include="formTemplate" class="column"></div>
                <div class="column large-12 paddingBottom50 marginBottom30"></div>
            </md-content>
        </md-content>
    </section>

""")))}),format.raw/*58.2*/("""
"""))
      }
    }
  }

  def render(message:String): play.twirl.api.HtmlFormat.Appendable = apply(message)

  def f:((String) => play.twirl.api.HtmlFormat.Appendable) = (message) => apply(message)

  def ref: this.type = this

}


}

/**/
object admin extends admin_Scope0.admin
              /*
                  -- GENERATED --
                  DATE: Thu Dec 24 03:24:20 CET 2015
                  SOURCE: /home/loann/Desktop/CezeCanoe/server/app/views/admin.scala.html
                  HASH: 06e0b714f5e7bd696cf5e2f23821de808f6132f7
                  MATRIX: 527->1|639->18|667->21|688->34|727->36|761->194|797->242|829->264|861->355|901->495|942->525|983->593|1020->619|1054->635|1086->640|1967->1493|1996->1494|2025->1495|2066->1508|2095->1509|2124->1510|2335->1781|2384->1802|2439->1904|2484->1921|3678->3085
                  LINES: 20->1|25->1|27->3|27->3|27->3|29->5|30->6|31->7|32->8|35->11|36->12|37->13|38->14|40->16|41->17|53->29|53->29|53->29|53->29|53->29|53->29|55->31|56->32|57->33|58->34|82->58
                  -- GENERATED --
              */
          