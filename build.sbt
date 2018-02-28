name := "actors"
scalaVersion := "2.12.4"

val akkaVersion = "2.5.9"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion
)

libraryDependencies += "com.typesafe" % "config" % "1.3.1"


libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"