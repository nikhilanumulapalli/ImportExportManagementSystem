package Supplier

import org.mongodb.scala.bson.ObjectId

object Supplier {
  /** Creates a supplier with a given parameters.
   *
   *  @param name the supplier name
   *  @param userName the username of the supplier
   *  @param password the password of the supplier
   *  @param address the address of the supplier
   *  @param bankName the bankName of the supplier
   *  @param bankBalance the bankBalance of the supplier
   *  @param merchant the merchant name
   */
  def apply(name: String, userName: String, password: String, address: String, bankName: String,
            bankBalance: Double, merchant: String): Supplier =
    Supplier(new ObjectId(), name: String, userName: String, password: String, address: String, bankName: String,
      bankBalance: Double, merchant: String)
}

/** A supplier who uses application to sell.
 *
 *  @param _id the supplier name
 *  @param name the supplier name
 *  @param userName the username of the supplier
 *  @param password the password of the supplier
 *  @param address the address of the supplier
 *  @param bankName the bankName of the supplier
 *  @param bankBalance the bankBalance of the supplier
 *  @param merchant the merchant name
 */
case class Supplier(_id: ObjectId, name: String, userName: String, password: String, address: String, bankName: String,
                    bankBalance: Double, merchant: String)
