package smartcrop

import com.greencatsoft.angularjs.injectable
import org.scalajs.dom.html.{Image, Element, Html}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

//@JSExport
/*trait Window extends org.scalajs.dom.Window*/
@injectable("$smartCrop")
trait SmartCropService extends js.Object {
  def crop(image: Image, size: CropSize, callback: js.Function): Unit = js.native
}

trait CropSize extends js.Object {
  var width: Int = js.native
  var height: Int = js.native
}


trait CropResult extends js.Object {
  var topCrop: CropTop = js.native
}

trait CropTop extends js.Object {
  var x: Int = js.native
  var y: Int = js.native
  var width: Int = js.native
  var height: Int = js.native
}

//SmartCrop.crop(image, {width: 100, height: 100}, function(result){console.log(result);});

