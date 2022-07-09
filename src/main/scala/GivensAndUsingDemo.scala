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