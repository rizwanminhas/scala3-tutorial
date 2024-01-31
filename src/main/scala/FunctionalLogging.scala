@main def functionalLogging(): Unit =

  case class User(id: Int, name: String)

  val getUserById: Int => (Option[User], String) =
    id =>
      if id < 10 then (None, s"No user found for id=$id")
      else (Some(User(id, "Rizwan")), s"Found user for id=$id")


  val capitalizeUserName: User => String = _.name.toUpperCase

  def log[A]: PartialFunction[(A, String), (A, String)] =
    case t =>
      if t._2.nonEmpty then println(s"${t._2}")
      t

  def logAndRemoveLog[A]: PartialFunction[(A, String), A] =
    case tuple if tuple._2.nonEmpty =>
      println(s"${tuple._2}")
      tuple._1
    case rest => rest._1

  def removeLogContext[A]: PartialFunction[(A, String), A] = _._1


  val firstCall = (getUserById andThen logAndRemoveLog)(5) // logs and removes the log string from the tuple.
  println(s"firstCall:$firstCall")

  val secondCall = (getUserById andThen log)(15) // logs and keeps the log string in the tuple.
  println(s"secondCall:$secondCall")

  val thirdCall = getUserById andThen removeLogContext // won't log and remove the log from the tuple.
  println(s"thirdCall:${thirdCall(25)}")

  val fourthCall = (getUserById andThen logAndRemoveLog andThen(_.map(capitalizeUserName)))(25) // calls 2 functions in a pipeline and logs the output of the first function as well.
  println(s"fourthCall:$fourthCall")
