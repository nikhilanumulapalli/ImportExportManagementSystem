package Cart

import Customer.Customer

import scala.collection.mutable.ListBuffer
import Product.{Product, UpdateProduct}
import grizzled.slf4j.Logging

/** Cart used for shopping.
 *
 *  @constructor create a new cart with a empty cart and zero total.
 *  @param products the person's name
 *  @param total the person's age in years
 */
class Cart(products: ListBuffer[Product] = ListBuffer[Product](), private var total: Double = 0) extends App with Logging {

  /** retruns true if the product is added to cart
   *
   *  @param product the product
   *  @return boolean
   */
  def addProduct(product: Product): Boolean = {
    val availability = product.availability
    if(availability <= 0){
      println("product out of stock")
      false
    } else {
      products += product
      total += product.sellingPrice
      true
    }
  }

  /** prints products added to the cart*/
  def viewCart(): Unit ={
    val updatedProducts = products
    if (updatedProducts.isEmpty) println("Your cart is empty")
    else {
      for (i <- updatedProducts.indices) {
        println(i + 1, updatedProducts(i).name, updatedProducts(i).sellingPrice)
      }
    }
  }

  /** deducts the balance from customer
   * updates the database with product availability
   *
   *  @param customer the customer
   */
  def checkOut(customer: Customer): Unit ={
    if (customer.updateBalance(this.total)) {
      val updatedProducts = products
      var infoMessage = customer.name + " bought "
      println("You've bought:")
      for (i <- updatedProducts.indices) {
        println(i+1 + ":" + updatedProducts(i).name)
        infoMessage += updatedProducts(i).name + " "
        UpdateProduct.updateProduct(updatedProducts(i))
      }
      println("and " + this.total + " has been deducted from your account")
      infoMessage += ("and " + this.total + "and has been deducted from customer account")
      info(infoMessage)
    }
    this.products.clear()
  }

  /** returns the total of cart
   *
   *  @return total
   */
  def getTotal: Double = total

  /** returns the number of products in the cart
   *
   *  @return cart size
   */
  def cartSize: Int = this.products.length
}
