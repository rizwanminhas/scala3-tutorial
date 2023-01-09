import scala.concurrent.Future
import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}
import scala.concurrent.duration.*

@main def partialFunctionDemo(): Unit = {
  val getFromCache: PartialFunction[String, String] = {
    case s if s == "test1" =>
      println(s"getting from cache1 $s")
      s.toUpperCase
  }

  def getFromDb: PartialFunction[String, String] = {
    case s if s.length > 1 =>
      println(s"getting from database2 $s")
      s.toLowerCase
  }

  def publishToKafka: PartialFunction[String, String] = {
    case s if s.nonEmpty =>
      println(s"publishing to kafka3 $s")
      s
  }

  val chain = (getFromCache orElse getFromDb) andThen publishToKafka

  chain("test1")
  chain("whatever")
}