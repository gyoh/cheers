package agiligent.cheers {
package model {

import net.liftweb.common.{Full,Box,Empty,Failure}
import net.liftweb.mapper._
import net.liftweb.sitemap.Loc._
import scala.xml.{NodeSeq,Node}

object User extends User
  with KeyedMetaMapper[Long, User]
  with MetaMegaProtoUser[User] {

  override def dbTableName = "users" // define the DB table name
  // define the order fields will appear in forms and output
  override def fieldOrder = List(id, firstName, lastName, email,
    locale, timezone, password, textArea)

  override def screenWrap: Box[Node] =
    Full(
      <lift:surround with="default" at="content">
			  <lift:bind />
      </lift:surround>
    )

  // comment this line out to require email validations
  override def skipEmailValidation = true

  override def createUserMenuLocParams =
    LocGroup("public") :: super.createUserMenuLocParams
}

class User extends MegaProtoUser[User]
  with CreatedUpdated {

  def getSingleton = User // what's the "meta" server

  // define an additional field for a bio
  object textArea extends MappedTextarea(this, 2048) {
    override def textareaRows  = 10
    override def textareaCols = 50
    override def displayName = "Bio"
  }
}

}}
