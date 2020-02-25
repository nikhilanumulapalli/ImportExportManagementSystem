package Supplier

import java.util.{Collections, Properties}
import System.JsonUtil._

import System.ProducerApp

import scala.io.StdIn._
import Product.Product
import org.mongodb.scala.bson.codecs.Macros._
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}
import System.Helpers._
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.bson.codecs.configuration.CodecRegistry

import scala.collection.JavaConverters._

object AddSupplierProducts{
  val codecRegistry: CodecRegistry = fromRegistries(fromProviders(classOf[Product]), DEFAULT_CODEC_REGISTRY)
  val mongoClient: MongoClient = MongoClient()
  val database: MongoDatabase = mongoClient.getDatabase("sigmoidBuy").withCodecRegistry(codecRegistry)
  val collection: MongoCollection[Product] = database.getCollection("products")

  /** adds product into database
   *
   *  @param supplier the supplier
   *  @return product added
   */
  def addSupplierProduct(supplier: Supplier): Unit = {
    val name = readLine("enter product name")
    println("enter product price")
    val productPrice = readFloat()
    println("enter selling price")
    val sellingPrice = readFloat()
    val merchant = supplier.merchant
    val categories = readLine("enter categories with comma separated").split(",").toList
    println("enter availability")
    val availability = readInt()
    val merchantNumber = readLine("enter merchant number")

    val product = Product(name, productPrice, sellingPrice, merchant, categories, merchantNumber, availability)
    collection.insertOne(product).results()
    println("product inserted successfully")
  }

  def addSupplierProducts(supplier: Supplier): Unit = {
    val TOPIC = "products"
    var flag = true

    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")

    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("group.id", "something")

    val consumer = new KafkaConsumer[String, String](props)

    consumer.subscribe(Collections.singletonList(TOPIC))

    val readType = readLine("from file: f\nrandom data: d\n")

    if (readType == "f") {

      val filePath = "./Data/"
      val fileName = readLine("enter file name from the data folder")

      var products = Seq[Product]()
      ProducerApp.fromFile(Array(filePath + fileName))
      while (flag) {
        val records = consumer.poll(100)
        for (record <- records.asScala) {
          if (record.key == "end") flag = false
          else if (record.key == "data") {
            val product = fromJson[Product](record.value).copy(merchant = supplier.merchant)
            products = products :+ product
          }
        }
        if (products.nonEmpty) collection.insertMany(products).results()
        products = Seq[Product]()
      }
    }
    else if (readType == "d") {
      print("enter number of products")
      val numberOfRecords = readInt()
      ProducerApp.randomData(numberOfRecords)

      var products = Seq[Product]()
      while (flag) {
        val records = consumer.poll(100)
        for (record <- records.asScala) {
          if (record.key == "end") flag = false
          else if (record.key == "data") {
            val product = fromJson[Product](record.value).copy(merchant = supplier.merchant)
            products = products :+ product
          }
        }
        if (products.nonEmpty) collection.insertMany(products).results()
        products = Seq[Product]()
      }
    }
    consumer.close()
    println("products inserted successfully")
  }
}
