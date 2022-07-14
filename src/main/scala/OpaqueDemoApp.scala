object OpaqueDemoApp:
  @main def opaqueDemo(): Unit =
    object MyDomain {
      opaque type Name = String

      // inside MyDomain Name is equal to String, it has the same API

      // but for the outside world we still need to define the API for Name type, there are 2 ways to define the API

      //1. companion object
      object Name {
        def fromString(s: String): Option[Name] =
          if s.isEmpty then None else Some(s)
      }

      //2. extension methods
      extension (n: Name) {
        def length: Int = n.length // I can still call the length of the string class because here Name == String.
      }
    }

    import MyDomain.Name
    val name: Option[Name] = Name.fromString("rizwan")
    println(name)
    //println(name.get.toUpperCase) // won't compile, because in the outside world Name doesn't inherit the String API