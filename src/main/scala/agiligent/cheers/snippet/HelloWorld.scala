package agiligent.cheers {
package snippet {

import xml.NodeSeq
import net.liftweb.util.Helpers._

/**
 * Created by IntelliJ IDEA.
 * User: gyo
 * Date: 2/7/11
 * Time: 10:57 PM
 * To change this template use File | Settings | File Templates.
 */

class HelloWorld {
  def howdy(xhtml: NodeSeq): NodeSeq =
    bind("b", xhtml,
      "time" -> (new _root_.java.util.Date).toString,
      "hello" -> "Hello World!")
}

}}