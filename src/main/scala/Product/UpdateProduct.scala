package Product

import org.mongodb.scala.bson.codecs.Macros._
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._
import System.Helpers._
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.result.UpdateResult


object UpdateProduct {
  val codecRegistry: CodecRegistry = fromRegistries(fromProviders(classOf[Product]), DEFAULT_CODEC_REGISTRY)
  val mongoClient: MongoClient = MongoClient()
  val database: MongoDatabase = mongoClient.getDatabase("sigmoidBuy").withCodecRegistry(codecRegistry)
  val collection: MongoCollection[Product] = database.getCollection("products")

  /** updates product availability
   *
   *  @param product the product in cart during checkout
   *  @return sequence of updated results
   */
  def updateProduct(product: Product): Seq[UpdateResult] ={
    val availability = collection.find(equal("_id", product._id)).headResult().availability
      collection.updateOne(equal("_id", product._id), set("availability", availability - 1)).results()
  }
}
