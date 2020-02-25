package System

import java.io.{FileNotFoundException, IOException}
import java.util.Properties

import com.github.tototoshi.csv.CSVReader
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import Product.Product
import scala.util.Random._
import MarshallableImplicits._

object ProducerApp{
  def fromFile(args: Array[String]): Unit = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")

    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](props)

    val TOPIC = "products"

    try {
      val reader = CSVReader.open(args(0))
      for (row <- reader.all()) {
        try {
          if (row.head.isEmpty) println("product name is empty")
          else {
            val product = Product(row(0), row(1).toDouble, row(2).toDouble, "", row(3).split(",").toList,
              row(4), row(5).toInt)
            val record = new ProducerRecord(TOPIC, "data", product.toJson)
            producer.send(record)
          }
        } catch {
          case e: NumberFormatException => println("invalid inputs encountered," +
            " please check your file for missing entries")
        }
      }
    } catch {
      case e: FileNotFoundException => {
        println("couldn't find the file")
      }
      case e: IOException => {
        println("Had an IOException trying to read that file")
      }
    }
    val record = new ProducerRecord(TOPIC, "end", "")
    producer.send(record)

    producer.close()
  }

  def randomData(numberOfRecords: Int): Unit ={
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")

    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](props)
    val TOPIC = "products"

    for (i <- 0 until numberOfRecords){
      val name = s"random$i"
      val productPrice = nextDouble()*100
      val sellingPrice = Math.min(productPrice, nextDouble()*100)
      val categories = "categories"
      val manufacturerNumber = "mn"
      val availability = nextInt(10)

      val product = Product(name, productPrice, sellingPrice, "", categories.split(",").toList, manufacturerNumber, availability)
      val record = new ProducerRecord(TOPIC, "data", product.toJson)
      producer.send(record)
    }
    val record = new ProducerRecord(TOPIC, "end", "")
    producer.send(record)

    producer.close()
  }
}
