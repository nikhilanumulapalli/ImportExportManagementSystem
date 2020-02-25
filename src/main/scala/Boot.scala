import akka.actor.ActorSystem
import akka.http.Http
import akka.http.model.HttpMethods._
import akka.http.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.stream.FlowMaterializer
import akka.stream.scaladsl.Flow
import play.api.libs.json.Json
import play.modules.reactivemongo.json.BSONFormats
import reactivemongo.bson.BSONDocument
import akka.http.model._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

//The Actor system to use. Required for flowmaterializer and HTTP.
//Passed in implicit



object Boot extends App {
  implicit val system: ActorSystem = ActorSystem("Streams")
  implicit val materializer: FlowMaterializer = FlowMaterializer()

  //start server on speciied interface and port
  val serverBinding2 = Http().bind(interface = "localhost", port = 8091)
  serverBinding2.connections.foreach {connection => connection.handleWith(Flow[HttpRequest].mapAsync(asyncHandler)) }

  /** With an async handler, we use futures.
   *
   *  @param request the http request
   *  @return future
   */
  def asyncHandler(request: HttpRequest): Future[HttpResponse] = {
    request match {

      // matches specific path
      case HttpRequest(GET, Uri.Path("/getAllProducts"), _, _, _) => {

        // make a db call, which returns a future.
        // use for comprehension to flatmap this into
        // a Future[HttpResponse]
        for {
          input <- DatabaseAPI.findAllProducts()
        } yield {
          HttpResponse(entity = convertToString(input))
        }
      }

      // match GET pat. Return a single ticker
      case HttpRequest(GET, Uri.Path("/get"), _, _, _)
      => {

        // next we match on the query paramter
        request.uri.query.get("ticker") match {

          // if we find the query parameter
          case Some(queryParameter) => {

            // query the database
            val ticker = DatabaseAPI.findProduct(queryParameter)

            // use a simple for comprehension, to make
            // working with futures easier.
            for {
              t <- ticker
            } yield {
              t match {
                case Some(bson) => HttpResponse(entity = convertToString(bson))
                case None => HttpResponse(status = StatusCodes.OK)
              }
            }
          }
          // if the query parameter isn't there
          case None => Future(HttpResponse(status = StatusCodes.OK))
        }
      }

      case HttpRequest(_, _, _, _, _)
      => {
        Future[HttpResponse] {
          HttpResponse(status = StatusCodes.NotFound)
        }
      }
    }
  }

  /** Converts List of BSON documents to string.
   *
   *  @param input list of BSON documents
   *  @return string
   */

  def convertToString(input: List[BSONDocument]): String = {
    input
      .map(f => convertToString(f))
      .mkString("[", ",", "]")
  }

  /** Helper method to convert BSON to JSON.
   *
   *  @param input BSON document
   *  @return string
   */

  def convertToString(input: BSONDocument): String = {
    Json.stringify(BSONFormats.toJSON(input))
  }
}






