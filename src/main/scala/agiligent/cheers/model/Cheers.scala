package agiligent.cheers {
package model {

  import net.liftweb.common.{Full,Box,Empty,Failure}
  import net.liftweb.mapper._

  class Cheers extends LongKeyedMapper[Cheers]
    with IdPK
    with CreatedUpdated {

    def getSingleton = Cheers

    // fields
    object mumble extends MappedText(this)

    // relationship
    object user extends LongMappedMapper(this, User) {
      override def dbColumnName = "user_id"
    }
    object beer extends LongMappedMapper(this, Beer) {
      override def dbColumnName = "beer_id"
    }
    object venue extends LongMappedMapper(this, Venue) {
      override def dbColumnName = "venue_id"
    }

  }

  object Cheers
    extends Cheers
    with LongKeyedMetaMapper[Cheers]{

    override def dbTableName = "cheers"

  }

}}