package agiligent.cheers {
package model {

import net.liftweb.common.{Full,Box,Empty,Failure}
import net.liftweb.sitemap.Loc._
import scala.xml.NodeSeq
import net.liftweb.mapper._

class Beer extends LongKeyedMapper[Beer]
  with IdPK
  with CreatedUpdated {

  def getSingleton = Beer

  // fields
  object name extends MappedString(this, 150)
  object description extends MappedText(this)

  // relationships
  object brewery extends LongMappedMapper(this, Brewery) {
    override def dbColumnName = "brewery_id"
    override def validSelectValues =
      Full(Brewery.findMap(OrderBy(Brewery.name, Ascending)) {
        case b: Brewery => Full(b.id.is -> b.name.is)
      })
  }

  // helper: get all the cheers for this beer
  def cheers = Cheers.findAll(By(Cheers.beer, this.id), OrderBy(Cheers.id, Descending))
}

object Beer extends Beer
  with LongKeyedMetaMapper[Beer]
  with CRUDify[Long, Beer] {

  override def dbTableName = "beers"
  override def fieldOrder = List(name, description)

  // crudify
  override def pageWrapper(body: NodeSeq) =
    <lift:surround with="admin" at="content">{body}</lift:surround>
  override def calcPrefix = List("admin",_dbTableNameLC)
  override def displayName = "Beer"
  override def showAllMenuLocParams = LocGroup("admin") :: Nil
  override def createMenuLocParams = LocGroup("admin") :: Nil
  override def viewMenuLocParams = LocGroup("admin") :: Nil
  override def editMenuLocParams = LocGroup("admin") :: Nil
  override def deleteMenuLocParams = LocGroup("admin") :: Nil

}

}}
