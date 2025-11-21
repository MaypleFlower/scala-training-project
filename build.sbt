val name = """scala-training-project"""
organization := "com.netcompany.todo"

version := "1.0-SNAPSHOT"
scalaVersion := "2.13.17"

lazy val root = Project(name, file("."))
  .enablePlugins(PlayScala)
  .settings(libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test :+ guice)

lazy val it = (project in file("it"))
  .enablePlugins(PlayScala)
  .dependsOn(root)
  .settings(
    libraryDependencies ++= AppDependencies.test,
    scalaVersion := "2.13.17"
  )

resolvers += "HMRC-open-artefacts-maven2" at "https://open.artefacts.tax.service.gov.uk/maven2"

// ******* scoverage *******
coverageExcludedPackages := Seq(
  // Play routes and reverse routers
  "<empty>",
  "router\\..*",
  "controllers\\.routes.*",
  "controllers\\.Reverse.*",
  "controllers\\.todo\\.Reverse.*",
  "controllers\\.javascript\\..*",
  "views\\.html\\..*",
  "modules\\.Reverse.*"
).mkString(";")

coverageMinimumStmtTotal := 90
coverageFailOnMinimum := false
coverageHighlighting := true
// ******* scoverage *******

Test / fork := false
it / fork := false
// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.netcompany.todo.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.netcompany.todo.binders._"