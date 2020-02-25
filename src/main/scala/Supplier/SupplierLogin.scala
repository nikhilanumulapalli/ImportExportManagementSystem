package Supplier

import User.User.autheticateSupplier
import scala.io.StdIn._

object SupplierLogin {
  /** returns supplier if log in is successful
   *
   *  @return sequence of updated results
   */
  def login(): Supplier = {
    val userName = readLine("Enter user name\n")
    val password = readLine("Enter password\n")

    autheticateSupplier(userName, password)

  }
}
