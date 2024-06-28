import scala.collection.mutable

/*
Typing systems:
  Static (compile time)
  Dynamic (run time)
  Nominal (type's identity)
  Structural (type's properties)

Generally speaking:
  Scala = Static and Nominal
  Groovy = Dynamic and Nominal
  Python = Dynamic and Structural
  TypeScript = Static and Structural
 */

class Duck:
  def quack(): Unit = println("quack duck")

class QuackingFrog:
  def quack(): Unit = println("quack frog")

class Cat

// static nominal
//def isDuck(x: Duck): true = true
//def isDuck(x: Any): false = false

// dynamic nominal
//def isDuck(x: Any): Boolean =
//  x.isInstanceOf[Duck]

// static structural
//@annotation.targetName("isDuckStructural")
//def isDuck(x: { def quack(): Unit }): true = true
//def isDuck(x: Any): false                  = false

// dynamic structural
//def isDuck(x: Any): Boolean =
//  try
//    x.getClass.getMethod("quack")
//    true
//  catch case _: NoSuchMethodException => false

@main def structuralTypingMain(): Unit =
  import scala.language.dynamics
  class Dyn(members: Map[String, Any]) extends Dynamic:
    val foo        = "abc"
    var newMembers = mutable.Map.empty[String, Any]
    // let's you read dynamically added properties by name
    def selectDynamic(name: String): Any =
      try members(name)
      catch
        case _: NoSuchElementException =>
          newMembers(name)

    // let's you set dynamically added properties by name
    def updateDynamic(name: String)(value: Any): Unit = newMembers(name) = value

    // let's you call dynamically added methods
    def applyDynamic(name: String)(args: Any*): Seq[(String, Any)] =
      args.map(arg => name -> arg)

    // let's you call dynamically added methods
    def applyDynamicNamed(name: String)(args: (String, Any)*): String =
      val attributes = args.map { case (k, v) => s"""$k = ${v.toString}""" }.mkString(" ")
      s"<$name $attributes></$name>"

  val dyn = Dyn(Map("bar" -> 123, "baz" -> false))

  println(dyn.bar)
  dyn.rizwan = "minhas"
  println(dyn.rizwan)

  println(dyn.myFunction(99, -5, false))
  println(dyn.myFucntion("qwerty").length)

  println(dyn.onTheFlyFunctionName(a = 1, b = true))
