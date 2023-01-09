import scala.concurrent.Future
import concurrent.ExecutionContext.Implicits.global
import scala.collection.immutable.{AbstractSeq, LinearSeq, ListMap}
import scala.util.{Failure, Random, Success}

@main def futurePerformanceDemo(): Unit =
  val callsWithStartTimes: Seq[(Long, Future[String])] =
    for (i <- 0 until 50)
      yield (System.currentTimeMillis, foo(i))

  val futureOfCallsWithTimeTaken: Future[Seq[(Long, String)]] =
    Future.sequence(
      callsWithStartTimes
        .map(tuple => tuple._2.map(futureResponse => (System.currentTimeMillis - tuple._1, futureResponse)))
    )

  futureOfCallsWithTimeTaken.onComplete({
    case Failure(exception) => println(exception)
    case Success(callsWithTime) =>
      val callsGroupedByTime = callsWithTime.groupBy(_._1).map(entry => (entry._1, entry._2.length))
      val callsSortedByTimeTakenAscending = callsGroupedByTime.toSeq.sortWith(_._1 < _._1)
      val callsSortedByCountDescending = callsGroupedByTime.toSeq.sortBy(t => (-t._2, t._1)) // first sort by call count in descending order and then by time taken in ascending order
      println(callsSortedByTimeTakenAscending)
      println(callsSortedByCountDescending)
  })

  Thread.sleep(2000)

def foo(i: Int): Future[String] = Future {
  Thread.sleep(Random.between(50, 60))
  "call " + i
}