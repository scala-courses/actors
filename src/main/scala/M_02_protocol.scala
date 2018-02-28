import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.concurrent.Await
import scala.concurrent.duration._

object M_02_protocol {
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






    class Worker(counter: ActorRef) extends Actor {
      override def preStart(): Unit = {
        super.preStart()

        counter ! Inc(5)
        counter ! Inc()
        counter ! Inc()
        counter ! Inc()
        counter ! Get
      }

      def receive: Receive = {
        case m => println(s"Got message $m")
      }
    }

    val counter = system.actorOf(Props(new Counter), "counter")
    val worker = system.actorOf(Props(new Worker(counter)), "worker")

    Thread.sleep(1000)

    Await.result(system.terminate(), 1.minute)

  }
}