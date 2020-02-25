package Supplier

import Product.Product
import org.mongodb.scala.bson.codecs.Macros._
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._
import System.Helpers._
import org.bson.codecs.configuration.CodecRegistry

import scala.io.StdIn._

object ViewProducts {

  val codecRegistry: CodecRegistry = fromRegistries(fromProviders(classOf[Product]), DEFAULT_CODEC_REGISTRY)
  val mongoClient: MongoClient = MongoClient()
  val database: MongoDatabase = mongoClient.getDatabase("sigmoidBuy").withCodecRegistry(codecRegistry)
  val collectionProduct: MongoCollection[Product] = database.getCollection("products")

  /** prints products information
   *
   *  @param merchant the merchant number
   */
  def viewProducts(merchant: String): Unit ={

    val products = collectionProduct.find(equal("merchant", merchant)).results()

    if(products.isEmpty){
     println("You have no products in your inventory, please add products!")
    }
    println("\n\n")
    for (i <- products.indices){
      val product = products(i)
      printf("%d- name: %s, actual price: %f, selling price: %f, availability: %d\n",
        i + 1, product.name, product.productPrice, product.sellingPrice, product.availability)
    }

    while (readLine("\nDo you want to edit products: y or n\n") == "y"){
      println("enter product number to be edited")
      val product = products(readInt() - 1)
      editProduct(product)
    }
  }

  def editProduct(product: Product): Unit ={
    if (readLine("\nDo you want to update selling price: y or n\n") == "y"){
      printf("current seeling price is %f, enter new selling price\n", product.sellingPrice)
      val newSellingprice = readFloat()
      collectionProduct.updateOne(equal("_id", product._id), set("sellingPrice", newSellingprice)).results()
    }

    if (readLine("\nDo you want to update availability: y or n\n") == "y"){
      println("how many more available")
      val newNumber = readInt()
      collectionProduct.updateOne(equal("_id", product._id),
        set("availability", newNumber + product.availability)).results()
    }
    val newProduct = collectionProduct.find(equal("_id", product._id)).headResult()
    printf("updated product\nname: %s, actual price: %f, selling price: %f, availability: %d\n",
      newProduct.name, newProduct.productPrice, newProduct.sellingPrice, newProduct.availability)
  }
}
