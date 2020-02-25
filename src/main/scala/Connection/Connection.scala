package Connection

import org.mongodb.scala.MongoClient
import System.Helpers._

object Connection {

  def isConnected(mongoClient: MongoClient): Boolean ={
    mongoClient.listDatabaseNames.results.nonEmpty
  }
}
