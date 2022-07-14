object CustomTypeDemoApp:
  @main def customTypeDemo(): Unit =
    val s: NonEmptyString = NonEmptyString("test") // just writing NonEmptyString("") will cause a compile time error.
    println(s)