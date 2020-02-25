package System

import Customer._
import Supplier._
import Cart._
import grizzled.slf4j.Logging
import scala.util.control.Breaks._

import scala.io.StdIn._

object System extends App with Logging {
  override def main(args: Array[String]): Unit = {
    var newUser = readLine("New User: n\nLogin: l\n")
    while (newUser != "n" && newUser != "l") {
    newUser = readLine("New User: n\nLogin: l\n")
  }
  if (newUser == "n") {
    //sign-up
    var userType = readLine("Customer:c\nsupplier: s\n")
    while (userType != "c" && userType != "s") {
      userType = readLine("Customer:c\nsupplier: s\n")
    }
    if (userType == "c") {
      CustomerSignup.sigUp()
    }
    else if (userType == "s") {
      SupplierSignup.sigUp()
    }
  }
  else if (newUser == "l") {
    //log-in
    var userType = readLine("Customer: c\nsupplier: s\n")
    while (userType != "c" && userType != "s") {
      userType = readLine("Customer: c\nsupplier: s\n")
    }
    if (userType == "c") {
      val customer = CustomerLogin.login()
      if (customer != null) {
        println("welcome ", customer.name)
        info("customer log in successful, username: " + customer.name)
        customerShopping(customer)
      } else {
        println("invalid credentials")
        info("invalid credentials")
      }
    } else if (userType == "s") {
        val supplier = SupplierLogin.login()

        if (supplier != null) {
          println("welcome ", supplier.name)
          info("supplier log in successful, username: " + supplier.name)
          supplierTool(supplier)
        } else {
          println("invalid credentials")
          info("invalid credentials")
        }
      }
    }
  }


  /** customer shopping function
   *
   *  @param customer the customer
   */
  def customerShopping(customer: Customer): Unit ={
    println("Please find the products list below. To search for more please hit e")
    var searchProducts = new SearchProducts(" ")
    var products = searchProducts.nextSet()
    val cart = new Cart()
    var choice = readLine(
      """
        |search: e
        |filter: f
        |sort: s
        |add balance: b
        |view account: i
        |quit: q
        |""".stripMargin)

    while (choice != "q") {
      if (choice == "e") {
        searchProducts = new SearchProducts(readLine("enter product name"))
        products = searchProducts.nextSet()
      }
      else if (choice == "n") products = searchProducts.nextSet()
      else if (choice == "s") {
        searchProducts.sortBy()
        products = searchProducts.nextSet()
      }
      else if (choice == "f") {
        searchProducts.filterBy()
        products = searchProducts.nextSet()
      }
      else if (choice == "a"){
        println("enter product number")
        cart.addProduct(products(readInt()-1))
      } else if (choice == "v")  cart.viewCart()
      else if (choice == "c") cart.checkOut(customer)
      else if (choice == "b"){
        println("enter amount to add")
        val credit = readInt()
        customer.addCredit(credit)
      } else if (choice == "i") customer.viewProfile()

      choice = readLine(
        """
          |next page: n
          |search: e
          |filter: f
          |sort: s
          |add balance: b
          |view account: i
          |add to cart: a
          |view cart: v
          |checkout: c
          |quit: q
          |""".stripMargin)
    }
  }


  /** supplier tool function
   *
   *  @param supplier the supplier
   */
  def supplierTool(supplier: Supplier): Unit ={
    var choice = readLine(
      """
        |view/update products: v
        |add product: a
        |add multiple products: m
        |quit: q
        |""".stripMargin)

    while (choice != "q"){
      if (choice == "v") ViewProducts.viewProducts(supplier.merchant)
      else if (choice == "a") AddSupplierProducts.addSupplierProduct(supplier)
      else if (choice == "m") AddSupplierProducts.addSupplierProducts(supplier)

      choice = readLine(
        """
          |view/update products: v
          |add products: a
          |add multiple products: m
          |quit: q
          |""".stripMargin)
    }
  }
}
