/*
This demo shows how to use dependency injection (DI) by using the "given" and "using" in Scala 3.

Here I have a trait for a DAO and two, mongo and mysql, implementations of this DAO.

Then I have a trait for a service with its implementation that needs the DAO trait to call the database.

In the main app, I import the desired dao impl and then call the service and it automatically picks my desired (imported) dao impl with the dao trait.
 */
object GivensAndUsingDemo:
  @main def GivensDemo(): Unit =
    import UserDaoImpl.mysql // import ".mongo" to use the mongo db dao
    given service: UserService = UserServiceImpl.instance
    println(service.getUserById("1"))

case class User(id: String, name: String, age: Int)

trait UserDao:
  def findUserById(id: String): Option[User]

object UserDaoImpl:
  given mongo: UserDao with
    override def findUserById(id: String): Option[User] =
      println("Mongo called")
      None

  given mysql: UserDao with
    override def findUserById(id: String): Option[User] =
      println("MySql called")
      Some(User(id,"test",10))

trait UserService:
  def getUserById(id: String)(using UserDao): Option[User]

object UserServiceImpl:
  given instance: UserService with
    override def getUserById(id: String)(using dao: UserDao): Option[User] =
      dao.findUserById(id)