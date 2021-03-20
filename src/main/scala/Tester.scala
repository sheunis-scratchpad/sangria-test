import io.circe.Json
import objects.schema
import repo.ProductRepo
import sangria.execution._
import sangria.macros._
import sangria.marshalling.circe._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object Tester {
  def main(args: Array[String]): Unit = {
    val query =
      graphql"""
    query MyProduct {
      product(id: "2") {
        name
        description

        picture(size: 500) {
          width, height, url
        }
      }

      products {
        name
      }
    }
  """

    val result: Future[Json] =
      Executor.execute(schema, query, new ProductRepo)

    println(Await.result(result, 10.seconds))
  }
}
