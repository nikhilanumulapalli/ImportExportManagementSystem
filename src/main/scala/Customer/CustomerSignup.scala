package Customer

import User.User._
import scala.util.Random.nextFloat
import scala.io.StdIn._

object CustomerSignup {

  /** sigups a customer */
  def sigUp(): Unit = {
    val name = readLine("Enter name\n")
    var userName = readLine("Enter user name\n")
    while (!checkForUserNames(userName, "c")){
      userName = readLine("username is taken, enter a different user name")
    }

    val password = readLine("Enter password\n")
    val address = readLine("Enter address\n")
    val bankName = readLine("Enter bank name\n")

    newCustomer(Customer(name, userName, password, address, bankName, nextFloat()*1000))
    println("you're signed up successfully \n please login")
  }
}
