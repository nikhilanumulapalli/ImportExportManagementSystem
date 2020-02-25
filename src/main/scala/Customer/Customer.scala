package Customer

import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.model.Updates._
import org.mongodb.scala.model.Filters._
import System.Helpers._
import org.bson.codecs.configuration.CodecRegistry

object Customer {
  /** Creates a customer with a given parameters.
   *
   *  @param name customer name
   *  @param userName the username of the customer
   *  @param password the password of the customer
   *  @param address the address of the customer
   *  @param bankName the bankName of the customer
   *  @param bankBalance the bankBalance of the customer
   */
  def apply(name: String, userName: String, password: String, address: String, bankName: String,
            bankBalance: Double): Customer =
    Customer(new ObjectId(), name: String, userName: String, password: String, address: String, bankName: String,
      bankBalance: Double)
}

/** A customer who uses application to buy.
 *
 *  @param _id the id of the customer
 *  @param name the customer name
 *  @param userName the username of the customer
 *  @param password the password of the customer
 *  @param address the address of the customer
 *  @param bankName the bankName of the customer
 *  @param bankBalance the bankBalance of the customer
 */
case class Customer(_id: ObjectId, name: String, userName: String, password: String, address: String, bankName: String,
                    var bankBalance: Double){
  val codecRegistry: CodecRegistry = fromRegistries(fromProviders(classOf[Customer]), DEFAULT_CODEC_REGISTRY)
  val mongoClient: MongoClient = MongoClient()
  val database: MongoDatabase = mongoClient.getDatabase("sigmoidBuy").withCodecRegistry(codecRegistry)
  val collection: MongoCollection[Customer] = database.getCollection("customers")

  /** returns true if the balance is available in wallet
   *
   *  @param cartTotal the total for the cart
   *  @return if sufficient balance
   */
  def updateBalance(cartTotal : Double): Boolean ={
    val balance = this.bankBalance
    val amountRemaining = balance - cartTotal

    if(amountRemaining <= 0){
      println("You have insufficient balance")
    }
    else
    {
      collection.updateOne(equal("_id", _id ), set("bankBalance", amountRemaining)).results()
      this.bankBalance = amountRemaining
      println("Order successfully placed")
      return true
    }
    false
  }

  /** returns true if credit is added wallet
   *
   *  @param credit the total for the cart
   *  @return if credit is added
   */
  def addCredit(credit : Double): Boolean = {
    val balance = this.bankBalance
    if(credit > 0){
      collection.updateOne(equal("_id", _id ), set("bankBalance", credit + balance)).results()
      println(credit + " successfully added to wallet")
      this.bankBalance = credit+balance
      true
    }
    else{
      println("Please enter a valid amount")
      false
    }
  }

  /** prints details of profile */
  def viewProfile(): Unit ={
    println("name: " + this.name)
    println("username: " + this.userName)
    println("address: " + this.address)
    println("balance: " + this.bankBalance)
  }

}