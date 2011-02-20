package bootstrap.liftweb

// framework imports
import net.liftweb.common._
import net.liftweb.util._
import net.liftweb.util.Helpers._
import net.liftweb.http._
import net.liftweb.sitemap._
import net.liftweb.sitemap.Loc._
import net.liftweb.mapper.{DB,Schemifier,DefaultConnectionIdentifier,StandardDBVendor,MapperRules}

// app imports
import agiligent.cheers.model.{Beer,Brewery,User,Venue}
// import agiligent.cheers.lib.{Helpers}


class Boot extends Loggable {
  def boot {

    // where to search snippet
    LiftRules.addToPackages("agiligent.cheers")

    MapperRules.columnName = (_, name) => StringHelpers.snakify(name)
    MapperRules.tableName = (_, name) => StringHelpers.snakify(name)

    // set the JNDI name that we'll be using
    DefaultConnectionIdentifier.jndiName = "jdbc/cheers"

    // handle JNDI not being avalible
    if (!DB.jndiJdbcConnAvailable_?) {
      logger.error("No JNDI configured - using the default in-memory database.")
      DB.defineConnectionManager(DefaultConnectionIdentifier, Database)
      // make sure cyote unloads database connections before shutting down
      LiftRules.unloadHooks.append(() => Database.closeAllConnections_!())
    }

    // automatically create the tables
    Schemifier.schemify(true, Schemifier.infoF _,
      Beer, Brewery, User, Venue)

    // set Lift to full HTML5 support
    //LiftRules.htmlProperties.default.set((r: Req) =>new Html5Properties(r.userAgent))

    // build sitemap
//    val entries = List(Menu("Home") / "index") :::
//                  Nil
    val entries = List(
      Menu("Home") / "index" >> LocGroup("public"),
//      Menu("Search") / "search" >> LocGroup("public"),
//      Menu("History") / "history" >> LocGroup("public"),
      Menu("Beers") / "beers" >> LocGroup("public"),
      Menu("Beer Detail") / "beer" >> LocGroup("public") >> Hidden,
      Menu("Admin") / "admin" / "index" >> LocGroup("admin"),
      Menu("Breweries") / "admin" / "breweries" >> LocGroup("admin") submenus(Brewery.menus : _*),
      Menu("Beer Admin") / "admin" / "beers" >> LocGroup("admin") submenus(Beer.menus : _*),
      Menu("Venue Admin") / "admin" / "venues" >> LocGroup("admin") submenus(Venue.menus : _*)
    ) ::: User.menus

    LiftRules.uriNotFound.prepend(NamedPF("404handler"){
      case (req,failure) => NotFoundAsTemplate(
        ParsePath(List("exceptions","404"),"html",false,false))
    })
    
    LiftRules.setSiteMap(SiteMap(entries:_*))

    // setup the load pattern
    S.addAround(DB.buildLoanWrapper)
    
    // set character encoding
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

  }

  object Database extends StandardDBVendor(
    Props.get("db.class").openOr("org.h2.Driver"),
    Props.get("db.url").openOr("jdbc:h2:database/cheers;DB_CLOSE_DELAY=-1"),
    Props.get("db.user"),
    Props.get("db.pass"))

}