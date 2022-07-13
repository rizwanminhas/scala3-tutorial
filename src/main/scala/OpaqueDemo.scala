import scala.compiletime.{requireConst, error}

object OpaqueDemo extends App {
  val s: NonEmptyString = NonEmptyString("test") // just writing NonEmptyString("") will cause a compile time error.
  println(s)
}