import scala.quoted.{Expr, Quotes}

object InlineDemo {

  @main def InlineDemo(): Unit =
    assert(true, "rizwan says...")

  // inline meta programming
  inline def assert(cond: Boolean, msg: => String): Unit =
    if !cond then throw new AssertionError(msg)

  // this is how you define a macro.
  inline def assert2(c: Boolean, inline m: String): Int =
    ${ code('c, 'm) }

  def code(c: Expr[Boolean], m: Expr[String])(using Quotes): Expr[Int] =
    c.value match
      case Some(value) => if !value then Expr(0) else Expr(1)
      case None => throw new UnsupportedOperationException()
}
