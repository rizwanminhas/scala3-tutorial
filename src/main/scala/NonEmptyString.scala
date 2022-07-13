import scala.compiletime.{error, requireConst}

opaque type NonEmptyString <:String = String

object NonEmptyString:
  inline def apply(inline s: String): NonEmptyString =
    requireConst(s)
    inline if s == "" then error("empty string.") else s

  given Conversion[NonEmptyString, String] with
    inline def apply(s: NonEmptyString): String = s