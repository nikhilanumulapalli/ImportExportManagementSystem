package Supplier

import User.User._
import scala.io.StdIn._

object SupplierSignup {

  /** sings up supplier */
  def sigUp(): Unit = {
    val name = readLine("Enter name\n")
    var userName = readLine("Enter user name\n")
    while (!checkForUserNames(userName, "s")){
      userName = readLine("username is taken, enter a different user name")
    }

    val password = readLine("Enter password\n")
    val address = readLine("Enter address\n")
    val bankName = readLine("Enter bank name\n")
    val merchant = readLine("Enter merchant name\n")

    newSupplier(Supplier(name, userName, password, address, bankName, 0, merchant))
    println("you're signed up successfully \n please login")
  }
}
