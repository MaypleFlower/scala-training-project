import sbt.*


object AppDependencies {

  val test = Seq(
    "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.2" % Test,
    "org.scalamock"          %% "scalamock"                   % "6.2.0",
    "org.scalatestplus" %% "scalacheck-1-18" % "3.2.19.0" % "test",
    "uk.gov.hmrc.mongo" %% "hmrc-mongo-test-play-30" % "2.9.0" % Test
  )

  val compile = Seq(
    "io.scalaland" %% "chimney" % "1.8.2",
    "uk.gov.hmrc.mongo" %% "hmrc-mongo-play-30" % "2.9.0"
  )
}