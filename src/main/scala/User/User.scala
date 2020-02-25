package User

import Connection.Connection._
import Customer.Customer
import Supplier.Supplier
import org.mongodb.scala.bson.codecs.Macros._
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.{Completed, MongoClient, MongoCollection, MongoDatabase}
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.model.Filters._
import System.Helpers._
import org.bson.codecs.configuration.CodecRegistry

object User extends LoginService{
  val codecRegistry: CodecRegistry = fromRegistries(fromProviders(classOf[Customer], classOf[Supplier]), DEFAULT_CODEC_REGISTRY)
  val mongoClient: MongoClient = MongoClient()

  val database: MongoDatabase = mongoClient.getDatabase("sigmoidBuy").withCodecRegistry(codecRegistry)

  val collectionCustomer: MongoCollection[Customer] = database.getCollection("customers")
  val collectionSupplier: MongoCollection[Supplier] = database.getCollection("supplier")

  /** returns customer if username and password pair exists
   *
   *  @param userName the username
   *  @param password the password
   *  @return customer with username and password
   */
  def autheticateCustomer(userName: String, password: String): Customer = {
    if (!isConnected(mongoClient)){
      println("check connection with DB")
      return null
    }
    collectionCustomer.find(and(equal("userName", userName),
      equal("password", password))).headResult()
    }

  /** returns customer if username and password pair exists
   *
   *  @param userName the username
   *  @param password the password
   *  @return customer with username and password
   */
  def autheticateSupplier(userName: String, password: String): Supplier = {
    if (!isConnected(mongoClient)){
      println("check connection with DB")
      return null
    }
    collectionSupplier.find(and(equal("userName", userName),
      equal("password", password))).headResult()
  }

  /** returns customer if username and password pair exists
   *
   *  @param userName the username
   *  @param userType the type of user
   *  @return customer with username and password
   */
  def checkForUserNames(userName: String, userType: String): Boolean = {
    if (userType == "c") {
      collectionCustomer.countDocuments(equal("userName", userName)).headResult() == 0
    } else if (userType == "s"){
      collectionSupplier.countDocuments(equal("userName", userName)).headResult() == 0
    } else {
      println("Invalid user type")
      false
    }
  }

  /** returns customer after inserting
   *
   *  @param customer the customer
   *  @return a seq with customer inserted
   */
  def newCustomer(customer: Customer): Seq[Completed] = {
    collectionCustomer.insertOne(customer).results()
  }

  /** returns supplier after inserting
   *
   *  @param supplier the supplier
   *  @return a seq with supplier inserted
   */
  def newSupplier(supplier: Supplier): Seq[Completed] = {
    collectionSupplier.insertOne(supplier).results()
  }
}

trait LoginService{
  def autheticateCustomer(userName: String, password: String) : Customer
  def autheticateSupplier(userName: String, password: String): Supplier
  def checkForUserNames(userName: String, userType: String): Boolean
}

