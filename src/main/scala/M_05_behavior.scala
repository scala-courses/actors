import akka.actor.{Actor, ActorSystem, Props}

import scala.concurrent.Await
import scala.concurrent.duration._

object M_05_behavior {
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
      def behavior(cnt: Int): Receive = {
        case Inc(i) => context.become(behavior(cnt+i))
        case Get => sender() ! Count(cnt)
      }

      def receive: Receive = behavior(0)
    }








    val counter = system.actorOf(Props(new Counter), "counter")

    counter ! Inc(5)
    counter ! Inc()
    counter ! Inc()
    counter ! Inc()
    counter ! Get

    Thread.sleep(1000)

    Await.result(system.terminate(), 1.minute)

  }
}