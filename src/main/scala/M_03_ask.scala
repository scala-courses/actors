import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.util.Timeout

import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.duration._

object M_03_ask {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("system")

    object Counter {
      sealed trait Message
      case class Inc(i: Int = 1) extends Message
      case object Get extends Message

      case class Count(cnt: Int)
    }

    import Counter._

    class Counter extends Actor {
      var cnt = 0

      def receive: Receive = {
        case Inc(i) => cnt += i
        case Get => sender() ! Count(cnt)
      }
    }








    val counter = system.actorOf(Props(new Counter), "counter")

    counter ! Inc(5)
    counter ! Inc()
    counter ! Inc()
    counter ! Inc()

    import akka.pattern.ask
    implicit val timeout: Timeout = 1.second

    val result: Future[Count] = (counter ? Get).mapTo[Count]

    println(s"Result: ${Await.result(result, 1.second)}")

    Await.result(system.terminate(), 1.minute)

  }
}