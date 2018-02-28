import akka.actor.{Actor, ActorSystem, Props}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import akka.pattern.ask
import akka.util.Timeout

object M_04_pipeTo {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("system")
    import system.dispatcher

    object Service {
      sealed trait Message
      case object Get extends Message

      case class Result(value: String)
    }

    import Service._

    class Service extends Actor {
      def asyncAction(): Future[Result] = {
        import akka.pattern.after
        after(100.millis, system.scheduler)(Future.successful(Result("res")))
      }

      def receive: Receive = {
        case Get =>
//          val replyTo = sender()
//          asyncAction().foreach(res => replyTo ! res)
          import akka.pattern.pipe
          asyncAction().pipeTo(sender())
      }
    }








    val service = system.actorOf(Props(new Service), "service")

    implicit val timeout: Timeout = 1.minute

    val result = (service ? Get).mapTo[Result]

    result.foreach(r => println(s"Result: $r"))

    Await.result(result, 1.minute)

    Await.result(system.terminate(), 1.minute)

  }
}