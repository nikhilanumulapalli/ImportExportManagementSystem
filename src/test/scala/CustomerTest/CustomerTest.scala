package CustomerTest

import Cart.Cart
import User._
import Customer.Customer
import Supplier.Supplier
import Product.Product
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar


class CustomerTest extends FunSuite with BeforeAndAfter with MockitoSugar{

  test ("test login customer"){
    val service = mock[LoginService]
    when(service.autheticateCustomer("nikhil", "nikhil")).thenReturn(
      Customer("nikhil", "nikhil", "nikhil", "123, gd", "bofa", 123.4))
    when(service.autheticateCustomer("john", "jcdjc")).thenReturn(null)

    val nikhil = service.autheticateCustomer("nikhil", "nikhil")
    val john = service.autheticateCustomer("john", "jcdjc")

    assert(nikhil.name == "nikhil")
    assert(john == null)
  }

  test ("test login supplier"){
    val service = mock[LoginService]
    when(service.autheticateSupplier("nikhil", "nikhil")).thenReturn(
      Supplier("nikhil", "nikhil", "nikhil", "123, gd", "bofa", 123.4, "abcd"))
    when(service.autheticateCustomer("john", "jcdjc")).thenReturn(null)

    val nikhil = service.autheticateSupplier("nikhil", "nikhil")
    val john = service.autheticateSupplier("john", "jcdjc")

    assert(nikhil.name == "nikhil")
    assert(john == null)
  }

  test("check for dupliacte usernames for customer"){
    val service = mock[LoginService]
    when(service.checkForUserNames("nikhil", "c")).thenReturn(false)
    when(service.checkForUserNames("joe goldberg", "c")).thenReturn(true)

    val usedUsername = service.checkForUserNames("nikhil", "c")
    val newUsername = service.checkForUserNames("joe goldberg", "c")

    assert(newUsername)
    assert(!usedUsername)
  }

  test("check for dupliacte usernames for supplier"){
    val service = mock[LoginService]
    when(service.checkForUserNames("nikhil", "s")).thenReturn(false)
    when(service.checkForUserNames("Naruto", "s")).thenReturn(true)

    val usedUsername = service.checkForUserNames("nikhil", "s")
    val newUsername = service.checkForUserNames("Naruto", "s")

    assert(newUsername)
    assert(!usedUsername)
  }

  test("adding to the cart") {
    val product = Product("tv", 120.0, 100.0, "walmart.com", List[String](), "ABCD", 10)
    val cart = new Cart()
    cart.addProduct(product)
    assert(cart.cartSize == 1)
    assert(cart.getTotal > 0)
  }

  test("checking availability") {
    val product = Product("tv", 120.0, 100.0, "walmart.com", List[String](), "ABCD", 0)
    val cart = new Cart()
    assert(!cart.addProduct(product))
  }

  test("balance at checkout") {
    val customer = Customer("asd", "zxcv", "qwe", "123 st", "bofa", 76)
    val product = Product("tv", 120.0, 100.0, "walmart.com", List[String](), "ABCD", 10)
    val cart = new Cart()
    cart.addProduct(product)
    assert(!customer.updateBalance(cart.getTotal))
  }

  test("adding negative balance"){
    val customer = Customer("asd", "zxcv", "qwe", "123 st", "bofa", 76)
    assert(!customer.addCredit(-5))
  }

}
