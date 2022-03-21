inThisBuild(
  List(
    scalaVersion := "2.12.15",
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    scalacOptions += "-Ywarn-unused-import"
  )
)

lazy val rocketchipExample = (project in file("."))
  .dependsOn(rocketchip)

lazy val rocketchip = (project in file("rocket-chip"))
