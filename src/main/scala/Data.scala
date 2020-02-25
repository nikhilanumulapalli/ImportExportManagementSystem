
import com.github.tototoshi.csv.CSVReader
import org.mongodb.scala.bson.codecs.Macros._
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY

import scala.util.Random._
import System.Helpers._
import Product.Product
import Customer.Customer
import Supplier.Supplier
import grizzled.slf4j.Logging

object Data extends App with Logging {

  override def main(args: Array[String]): Unit = {
    productData()
    customerData()
    supplierData()
  }

  /** inserts dummy products data to database */
  def productData(): Unit ={
    val codecRegistry = fromRegistries(fromProviders(classOf[Product]), DEFAULT_CODEC_REGISTRY)
    val mongoClient: MongoClient = MongoClient()
    val database: MongoDatabase = mongoClient.getDatabase("sigmoidBuy").withCodecRegistry(codecRegistry)
    val collection: MongoCollection[Product] = database.getCollection("products")

    //drop collection if exists
    collection.drop().results()

    val reader = CSVReader.open("./Data/products.csv")

    for (col <- reader.all()) {
      val product = Product(col(7), col(0).toDouble, col(1).toDouble, col(2), col(4).split(",").toList,
        col(6), nextInt(5))

      collection.insertOne(product).results()
    }

    info("Products data inserted")
    reader.close
  }

  /** inserts dummy customers data to database */
  def customerData(): Unit ={
    val codecRegistry = fromRegistries(fromProviders(classOf[Customer]), DEFAULT_CODEC_REGISTRY)
    val mongoClient: MongoClient = MongoClient()
    val database: MongoDatabase = mongoClient.getDatabase("sigmoidBuy").withCodecRegistry(codecRegistry)
    val collection: MongoCollection[Customer] = database.getCollection("customers")

    val customers = Seq(
      Customer("John", "john", "john", "1234, doctors dr", "BofA", nextFloat()),
      Customer("George", "george17", "apple", "1515, independence blvd", "Chase", nextFloat()),
      Customer("Grace", "grace1", "texas", "9234, Coit rd", "Chase", nextFloat())
    )

    collection.drop().results()
    collection.insertMany(customers).printResults()
    info("Customers data inserted")
  }

  /** inserts dummy supplier data to database */
  def supplierData(): Unit ={
    val codecRegistry = fromRegistries(fromProviders(classOf[Supplier]), DEFAULT_CODEC_REGISTRY)
    val mongoClient: MongoClient = MongoClient()
    val database: MongoDatabase = mongoClient.getDatabase("sigmoidBuy").withCodecRegistry(codecRegistry)
    val collection: MongoCollection[Supplier] = database.getCollection("customers")

    val customers = Seq(
      Supplier("nikhil", "nikhil", "nikhil", "1234, doctors dr", "BofA", 0, "Bestbuy.com"),
      Supplier("john", "john", "john", "1515, independence blvd", "Chase", 0, "Wayfair - Walmart.com")
    )

    collection.drop().results()
    collection.insertMany(customers).printResults()
    info("Customers data inserted")
  }
}
