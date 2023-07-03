/*
Duck typing is known as structural typing in scala.
 */
import reflect.Selectable.reflectiveSelectable
import scala.util.{Failure, Success, Try}

object DuckTypingStructuralTypingDemoApp {
  @main def duckTypingStructuralTyping(): Unit =
    inheritanceDemo()
    duckTypingDemo()
    structuralTypingDemo()

  def inheritanceDemo() =
    def letsFly(f: Flyable) = f.fly()

    trait Flyable:
      def fly(): Unit

    case class Bird(name: String, color: String) extends Flyable:
      override def fly(): Unit = println(s"[OOP] A bird `$name` is flying! ")

    case class Aeroplane(make: String, airline: String) extends Flyable:
      override def fly(): Unit = println(s"[OOP] $airline is flying $make aeroplane")

    val bird = Bird("parrot", "green")
    val aeroplane = Aeroplane("Boeing", "JetBlue")
    letsFly(bird)
    letsFly(aeroplane)

  def duckTypingDemo() =
    def letsFly[T <: { def fly(): Unit }](obj: T): Unit = obj.fly()

    case class Bird(name: String, color: String):
      def fly(): Unit = println(s"A bird `$name` is flying! ")

    case class Aeroplane(make: String, airline: String):
      def fly(): Unit = println(s"$airline is flying $make aeroplane")

    val bird = Bird("Eagle", "Grey")
    val aeroplane = Aeroplane("Airbus", "Lufthansa")
    letsFly(bird)
    letsFly(aeroplane)

  def structuralTypingDemo() =
    class Obj(elems: (String, Any)*) extends Selectable:
      private val fields = elems.toMap
      def selectDynamic(name: String): Any = fields(name)

    type Person = Obj {
      val name: String
      val age: Int
      val baz: Double
      val foo: String
    }

    val person = Obj(
      "name" -> "Rizwan",
      "age" -> 10,
      "foo" -> "bar",
      "baz" -> 1.23,
    ).asInstanceOf[Person]

    println(s"${person.name} is ${person.age} years old. ${person.foo}, ${person.baz}")
}
