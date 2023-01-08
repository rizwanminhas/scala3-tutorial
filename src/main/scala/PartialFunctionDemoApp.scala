import scala.concurrent.{Await, Future}
import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Random, Success}
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

  def foo(i: Int): Future[String] = Future {
    Thread.sleep(Random.between(50, 100))
    "call " + i
  }

  val callsWithStartTimes: Seq[(Long, Future[String])] =
    for (i <- 0 until 50)
      yield (System.currentTimeMillis, foo(i))

  val futureOfCallsWithTimeTaken: Future[Seq[(Long,String)]] =
    Future.sequence(
      callsWithStartTimes
        .map(tuple =>
          tuple._2.map(futureResponse =>
            (System.currentTimeMillis - tuple._1, futureResponse)
          )
        )
    )

  futureOfCallsWithTimeTaken.onComplete({
    case Failure(exception) => println(exception)
    case Success(callsWithTime) =>
      val callsGroupedByTime = callsWithTime.groupBy(_._1).map(entry => (entry._1, entry._2.length))
      val sortedCallsGroupedByTime = callsGroupedByTime.toSeq.sortWith(_._1 < _._1).toMap
      println(sortedCallsGroupedByTime)
  })
  Thread.sleep(5000)
  //Await.result(futureOfCallsWithTimeTaken, 10.seconds)
}