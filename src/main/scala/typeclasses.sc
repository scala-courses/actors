import scala.reflect.ClassTag

trait Writer[T] {
  def write(t: T): String
}

object Writer {
  def apply[T: Writer] = implicitly[Writer[T]]
}

trait Reader[T] {
  def read(s: String): T
}


implicit def listWriter[T: Writer]: Writer[List[T]] = ???
//implicit def listWriter[T](implicit ev: Writer[T]): Writer[List[T]] = ???

implicit object StringFormat extends Format[String] {
  def read(s: String) = s

  def write(t: String) = t
}

trait Format[T] extends Reader[T] with Writer[T]

def write[T: Writer](t: T): String = Writer[T].write(t)

def read[T](s: String)(implicit reader: Reader[T]): T = reader.read(s)

def x = write(List("abc"))
//write(List(1))

implicit class IsJelper(a: Any) {
  def is[T: ClassTag]: Boolean = a match {
    case _: T => true
    case _ => false
  }
}

("abc": Any).is[String]



