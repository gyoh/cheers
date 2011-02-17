package agiligent.cheers {
package model {

import net.liftweb.common.{Full,Box,Empty,Failure}
import net.liftweb.sitemap.Loc._
import net.liftweb.mapper._
import scala.xml.NodeSeq

class Brewery extends LongKeyedMapper[Brewery]
  with IdPK with OneToMany[Long, Brewery] with CreatedUpdated {

  def getSingleton = Brewery

  // fields
  object name extends MappedString(this, 150)
  object telephone extends MappedString(this, 30)
  object email extends MappedEmail(this, 200)
  object address extends MappedText(this)

  // relationships
  object beers extends MappedOneToMany(Beer, Beer.brewery,
    OrderBy(Beer.name, Ascending))
      with Owned[Beer]
      with Cascade[Beer]

}

object Brewery extends Brewery
  with LongKeyedMetaMapper[Brewery]
  with CRUDify[Long, Brewery] {

  override def dbTableName = "breweries"
  override def fieldOrder = name :: email :: address :: telephone :: Nil

  // crudify
  override def pageWrapper(body: NodeSeq) =
    <lift:surround with="admin" at="content">{body}</lift:surround>
  override def calcPrefix = List("admin",_dbTableNameLC)
  override def displayName = "Brewery"
  override def showAllMenuLocParams = LocGroup("admin") :: Nil
  override def createMenuLocParams = LocGroup("admin") :: Nil
  override def viewMenuLocParams = LocGroup("admin") :: Nil
  override def editMenuLocParams = LocGroup("admin") :: Nil
  override def deleteMenuLocParams = LocGroup("admin") :: Nil

}

}}
