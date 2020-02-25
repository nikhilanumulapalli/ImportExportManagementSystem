name := "ImportExportManagementSystem"

version := "0.1"

scalaVersion := "2.11.12"

resolvers += Resolver.bintrayRepo("ovotech", "maven")

libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.3.6"
libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "2.6.0"
libraryDependencies += "com.typesafe.akka" %% "akka-http-core-experimental" % "1.0-M2"
libraryDependencies += "org.reactivemongo" %% "reactivemongo" % "0.10.5.0.akka23"
libraryDependencies += "org.reactivemongo" %% "play2-reactivemongo" % "0.10.5.0.akka23"
libraryDependencies += "com.typesafe.play" % "play-json_2.11" % "2.4.0-M2"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.1.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.1"
libraryDependencies += "org.mockito" % "mockito-all" % "1.8.4"

libraryDependencies ++= Seq("org.slf4j" % "slf4j-api" % "1.7.5",
  "org.slf4j" % "slf4j-simple" % "1.7.5",
  "org.clapper" %% "grizzled-slf4j" % "1.0.2")

libraryDependencies += "org.apache.kafka" %% "kafka" % "2.1.0"

libraryDependencies ++= {
  val kafkaSerializationV = "0.3.16"
  Seq(
    "com.ovoenergy" %% "kafka-serialization-json4s" % kafkaSerializationV, // To provide Json4s JSON support
    "com.ovoenergy" %% "kafka-serialization-jsoniter-scala" % kafkaSerializationV // To provide Jsoniter Scala JSON support
  )
}

libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.10.2"
libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.10.2"