package Product

import org.mongodb.scala.bson.ObjectId

object Product {
  /** Creates a customer with a given parameters.
   *
   *  @param name their name
   *  @param productPrice the price of the product
   *  @param sellingPrice the password of the product
   *  @param merchant the address of the product
   *  @param categories the bankName of the product
   *  @param manufacturerNumber the bankBalance of the product
   *  @param availability the bankBalance of the product
   */
  def apply(name: String, productPrice: Double, sellingPrice: Double, merchant: String, categories: List[String],
            manufacturerNumber: String, availability: Int): Product =
    Product(new ObjectId(), name: String, productPrice: Double, sellingPrice: Double, merchant: String,
      categories: List[String], manufacturerNumber: String, availability: Int)
}

/** Creates a customer with a given parameters.
 *
 *  @param _id the product id
 *  @param name the product name
 *  @param productPrice the price of the product
 *  @param sellingPrice the password of the product
 *  @param merchant the address of the product
 *  @param categories the bankName of the product
 *  @param manufacturerNumber the bankBalance of the product
 *  @param availability the bankBalance of the product
 */
case class Product(_id: ObjectId, name: String, productPrice: Double, sellingPrice: Double, merchant: String,
                   categories: List[String], manufacturerNumber: String, availability: Int)

//trait Object extends ObjectId