import akka.actor.{Actor, ActorRef, ActorSystem, Props}

import scala.concurrent.Await
import scala.concurrent.duration._

object M_01_messages {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("system")
    import system.dispatcher

    class Counter extends Actor {
      var cnt = 0

      def receive: Receive = {
        case "inc" => cnt+=1
        case "get" => sender() ! cnt
      }
    }

    class Worker(counter: ActorRef) extends Actor {
      override def preStart(): Unit = {
        super.preStart()

        counter.tell("inc", self)
        counter ! "inc"
        counter ! "inc"
        counter ! "inc"
        counter ! "inc"
        counter ! "get"
      }

      def receive: Receive = {
        case m => println(s"Got message $m")
      }
    }

    val counter = system.actorOf(Props(new Counter))
    val worker = system.actorOf(Props(new Worker(counter)))

    Thread.sleep(1000)

    Await.result(system.terminate(), 1.minute)
  }

}
