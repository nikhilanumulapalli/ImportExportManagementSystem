package Customer

import Product.Product
import org.mongodb.scala.bson.codecs.Macros._
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.model.Filters.{and, equal, gt, lt, regex}
import System.Helpers._
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Sorts.{ascending, descending}
import org.mongodb.scala.model.{Filters, Indexes}

import scala.io.StdIn._

/** Search application.
 *
 *  @constructor create a product sequence.
 *  @param productName the person's name
 */
class SearchProducts(productName: String) {

  private val codecRegistry: CodecRegistry = fromRegistries(fromProviders(classOf[Product]), DEFAULT_CODEC_REGISTRY)
  private val mongoClient: MongoClient = MongoClient()
  private val database: MongoDatabase = mongoClient.getDatabase("sigmoidBuy").withCodecRegistry(codecRegistry)
  private val collectionProduct: MongoCollection[Product] = database.getCollection("products")

  private var products: Seq[Product] = search()
  private var iterator = 0

  /** prints product information from list
   *
   *  @param products the products in sequence
   */
  def describeProducts(products: Seq[Product]): Unit ={
    println("S. No. Name \t\t price discounted price\t\t")
    for (i <- products.indices){
      println(i+1, products(i).name, products(i).productPrice, products(i).sellingPrice, products(i).availability)
    }
  }

  /** returns product list with productName in the name
   *
   *  @return sequence of products
   */
  def search(): Seq[Product] = {
    val productResult = collectionProduct.find(regex("name", productName)).results()
    productResult
  }

  /** returns set of product list with current query
   *
   *  @return sequence of products
   */
  def nextSet(): Seq[Product] = {
    val productsSet = products.slice(iterator*10, (iterator + 1)*10)
    iterator += 1
    describeProducts(productsSet)
    productsSet
  }

  /** updates products sorting the existing search
   */
  def sortBy(): Unit = {
    var fieldName = ""
    val field= readLine("Name: n\nprice: p\ndiscounted price: d\n")
    if (field == "n") fieldName = "name"
    else if (field == "p") fieldName = "productPrice"
    if (field == "d") fieldName = "sellingPrice"
    if (readLine(
      """
        |ascending: a
        |descending: d
        |""".stripMargin) == "a") {
      products = collectionProduct.find(regex("name", productName)).sort(ascending(fieldName)).results()
    } else {
      products = collectionProduct.find(regex("name", productName)).sort(descending(fieldName)).results()
    }
    iterator = 0
  }

  /** updates products filtering the existing search
   */
  def filterBy(): Unit = {
    var fieldName = ""
    val field= readLine("price: p\ndiscounted price: d\ncategories: c\n")
    if (field == "c"){
      val fieldName = "categories"
      val fieldValue = readLine("enter category")

      collectionProduct.createIndex(Indexes.text(fieldName)).printResults()
      products = collectionProduct.find(Filters.text(fieldValue)).results()
      return
    }
    if (field == "p") fieldName = "productPrice"
    if (field == "d") fieldName = "sellingPrice"

    val filteringMethod = readLine(
      """
        |equals: e
        |lessthan: l
        |greaterthan: g
        |""".stripMargin)
    print("enter value")
    val fieldValue = readInt()

    if (filteringMethod == "e"){
      products = collectionProduct.find(and(equal(fieldName, fieldValue), regex("name", productName))).results()
    } else if (filteringMethod == "l"){
      products = collectionProduct.find(and(lt(fieldName, fieldValue), regex("name", productName))).results()
    } else if (filteringMethod == "g") {
      products = collectionProduct.find(and(gt(fieldName, fieldValue), regex("name", productName))).results()
    }
    iterator = 0
  }
}

trait SearchProduct{
  def search(productName: String): Seq[Product]
}
