
package views.html

import play.twirl.api._
import play.twirl.api.TemplateMagic._


     object index_Scope0 {
import models._
import controllers._
import play.api.i18n._
import views.html._
import play.api.templates.PlayMagic._
import play.api.mvc._
import play.api.data._

class index extends BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with play.twirl.api.Template1[String,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(message: String):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.19*/("""

"""),_display_(/*3.2*/main("Cèze canoës")/*3.21*/ {_display_(Seq[Any](format.raw/*3.23*/("""

  """),format.raw/*5.3*/("""<md-toolbar md-scroll-shrink ng-if="true" class="top-bar ng-hide" style="background: #B4D5B8;">
    <div class="md-toolbar-tools" >
      <md-button aria-label="menu" class="md-fab md-primary md-mini" ng-click="directive.toggleLeft()" hide-gt-md>
       <md-icon md-svg-src="assets/images/menu.svg"></md-icon>
      </md-button>
      <object data="assets/images/title.svg"
      class="column medium-3 large-2 small-8 animated fadeIn"></object>
      <top-bar-content class="column large-10" hide-sm hide-md></top-bar-content>
      <lang></lang>
    </div>
  </md-toolbar>
  <md-sidenav class="md-sidenav-left md-whiteframe-z2" ng-click="directive.toggleLeft()" md-component-id="left">
      <top-bar-content></top-bar-content>
  </md-sidenav>

  <object data="assets/images/title.svg"
  class="column large-3 medium-6 animated bounceInLeft absolute left0"
  id="title"
  style="z-index: 9999; animation-delay: 0.5s"></object>
  <md-content class="height100p column large-12 padding0" id="mainContent" style="overflow-x: hidden; background: transparent">
    <lang class="absolute right"></lang>
    <slider images="assets/images/img1.jpg, assets/images/img2.jpg, assets/images/img3.jpg" class="width100p"></slider>
    <h1 style="z-index: 99999; top:50%" class="column medium-6 medium-offset-3 animated bounceInLeft absolute">
      <b class=" textColorWhite" >
        La Cèze et ses Gorges<br/>
        en Canoë-kayak !
      </b>
    </h1>
    <md-toolbar ng-if="true" id="middleBar" class="transparent md-sticky" style="top: -32px; z-index: 9999;" hide-md hide-sm>
      <div class="md-toolbar-tools">
        <top-bar-content class="width100p"></top-bar-content>
      </div>
    </md-toolbar>
    <div data-ng-view=""></div>
    <div class="column large-12 paddingBottom50"></div>
   """),format.raw/*45.13*/("""
  """),format.raw/*46.3*/("""</md-content>
""")))}),format.raw/*47.2*/("""
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
object index extends index_Scope0.index
              /*
                  -- GENERATED --
                  DATE: Thu Dec 24 03:50:32 CET 2015
                  SOURCE: /home/loann/Desktop/CezeCanoe/server/app/views/index.scala.html
                  HASH: f341255f963086e1e27119f160ad7e192fe75e3e
                  MATRIX: 527->1|639->18|667->21|694->40|733->42|763->46|2584->2346|2614->2349|2659->2364
                  LINES: 20->1|25->1|27->3|27->3|27->3|29->5|64->45|65->46|66->47
                  -- GENERATED --
              */
          