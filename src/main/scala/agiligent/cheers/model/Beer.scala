package agiligent.cheers {
package model {

import net.liftweb.mapper._

class Beer extends LongKeyedMapper[Beer] with IdPK with CreatedUpdated {

  def getSingleton = Beer
  object name extends MappedString(this, 150)
  object description extends MappedTextarea(this)

}

object Beer extends Beer with LongKeyedMetaMapper[Beer]

}}