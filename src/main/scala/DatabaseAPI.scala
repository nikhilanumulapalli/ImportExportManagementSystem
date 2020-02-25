import reactivemongo.api._
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.BSONDocument
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


object DatabaseAPI {

  val collection: BSONCollection = connect()

  /** Creates a connection with the database.
   *  @return BSONCollection
   */

  def connect(): BSONCollection = {

    val driver = new MongoDriver
    val connection = driver.connection(List("localhost"))

    val db = connection("sigmoidBuy")
    db.collection("products")
  }

  /** Selects all fields from the collection.
   *  @return future list of BSON document
   */

  def findAllProducts(): Future[List[BSONDocument]] = {
    val query = BSONDocument()
    val filter = BSONDocument("id" -> 1, "name" -> 1, "productPrice" -> 1, "sellingPrice" -> 1, "merchant" -> 1,
      "brand" -> 1, "categories" -> 1, "manufacturer" -> 1, "manufacturerNumber" -> 1, "primaryCategories" -> 1 )
    println("============")
    println(filter.toString())

    // which results in a Future[List[BSONDocument]]
    DatabaseAPI.collection
      .find(query, filter)
      .cursor[BSONDocument]
      .collect[List]()
  }

  /** Finds product by the given name in the URI.
   *  @param name the product name
   *  @return future BSON document
   */

  def findProduct(name: String) : Future[Option[BSONDocument]] = {
    val query = BSONDocument("name" -> name)

    DatabaseAPI.collection
      .find(query)
      .one
  }

}


