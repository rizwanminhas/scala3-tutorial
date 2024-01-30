@main def demo(): Unit =
  val divide1 = (a: Int) => a/10.0
  val square1 = (a: Double) => a * a

  val chain1 = divide1 andThen square1
  println(chain1(5))

  val divide2: (Int, Int) => Double = (a: Int, b: Int) => a / b

  val chain2 = divide2.tupled andThen square1
  println(chain2(10,5))

  val divide3: (Int, Int) => Option[Double] = (a: Int, b: Int) => if (b == 0) None else Some(a / b)
  val square2: Double => Option[Double] = a => Some(a * a)

  val chain3 = divide3.tupled.unlift andThen square2
  println(chain3(10,1)) // println(chain3(10,0)) Will throw a runtime exception.

  val chain4 = divide3.tupled andThen(_.map(square2))
  println(chain4(10,0))
