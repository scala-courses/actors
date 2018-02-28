import akka.actor.{Actor, ActorSystem, Props}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import akka.pattern.{after, ask, pipe}
import akka.util.Timeout

object M_06_stash {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("system")
    import system.dispatcher

    object Counter {
      sealed trait Message
      case class Inc(i: Int = 1) extends Message
      case object Get extends Message

      private case class SetToken(token: String)

      case class Count(cnt: Int, token: String)
    }

    import Counter._

    class Counter extends Actor {
      import context.dispatcher

      def getToken(): Future[String] =
        after(100.millis, system.scheduler)(Future.successful("token"))

      override def preStart(): Unit = {
        super.preStart()
        getToken()
          .map(SetToken)
          .pipeTo(self)
      }


      def behavior(cnt: Int, token: Option[String]): Receive = {
        case Inc(i) => context.become(behavior(cnt+i, token))
        case Get =>
          token match {
            case Some(t) => sender() ! Count(cnt, t)
            case None => // TODO save message and sender
          }
        case SetToken(t) if token.isEmpty => // TODO simplify
          context.become(behavior(cnt, Some(t)))
      }

      def receive: Receive = behavior(0, None)
    }








    val counter = system.actorOf(Props(new Counter), "counter")

    counter ! Inc(5)
    counter ! Inc()
    counter ! Inc()
    counter ! Inc()

    Thread.sleep(1000) // TODO remove
    implicit val timeout: Timeout = 1.minute
    val result = counter ? Get
    result foreach println
    Await.result(result, 1.minute)


    Await.result(system.terminate(), 1.minute)

  }
}