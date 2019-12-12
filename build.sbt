name := "actors"
scalaVersion := "2.13.1"

val akkaVersion = "2.6.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion
)

libraryDependencies += "com.typesafe" % "config" % "1.3.4"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
