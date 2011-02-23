package agiligent.cheers {
package model {

import net.liftweb.common.{Full,Box,Empty,Failure}
import net.liftweb.sitemap.Loc._
import scala.xml.NodeSeq
import net.liftweb.mapper._

class Venue extends LongKeyedMapper[Venue]
  with IdPK
  with CreatedUpdated {

  def getSingleton = Venue
  object name extends MappedString(this, 255);
  object address extends MappedString(this, 255);
  object zip extends MappedString(this, 255);
  object latitude extends MappedDouble(this);
  object longitude extends MappedDouble(this);
  object woeid extends MappedLong(this);

  // helper: get all the cheers for this venue
  def cheers = Cheers.findAll(By(Cheers.venue, this.id), OrderBy(Cheers.id, Descending))

}

object Venue extends Venue
  with LongKeyedMetaMapper[Venue]
  with CRUDify[Long, Venue] {

  override def dbTableName = "venues"
  override def fieldOrder = List(name, address)

  // crudify
  override def pageWrapper(body: NodeSeq) =
    <lift:surround with="admin" at="content">{body}</lift:surround>
  override def calcPrefix = List("admin",_dbTableNameLC)
  override def displayName = "Venue"
  override def showAllMenuLocParams = LocGroup("admin") :: Nil
  override def createMenuLocParams = LocGroup("admin") :: Nil
  override def viewMenuLocParams = LocGroup("admin") :: Nil
  override def editMenuLocParams = LocGroup("admin") :: Nil
  override def deleteMenuLocParams = LocGroup("admin") :: Nil

  import net.liftweb.util.Helpers.tryo
  def unapply(id: String): Option[Venue] = tryo {
    find(By(Venue.id, id.toLong)).toOption
  } openOr None
}

}}