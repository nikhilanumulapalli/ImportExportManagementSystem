package Customer

import User.User.autheticateCustomer
import scala.io.StdIn._

object CustomerLogin {

  /** returns customer if login is successful
   *
   *  @return customer
   */
  def login(): Customer = {
    val userName = readLine("Enter user name\n")
    val password = readLine("Enter password\n")

    autheticateCustomer(userName, password)
  }

}
