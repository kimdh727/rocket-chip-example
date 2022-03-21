inThisBuild(
  List(
    scalaVersion := "2.12.15",
    addCompilerPlugin("edu.berkeley.cs" % "chisel3-plugin" % "3.5.0" cross CrossVersion.full),
    libraryDependencies += "edu.berkeley.cs" %% "chisel3" % "3.5.0",
    libraryDependencies += "edu.berkeley.cs" %% "chiseltest" % "0.5.0" % "test",
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    scalacOptions += "-Ywarn-unused-import",
    resolvers ++= Seq(
      Resolver.sonatypeRepo("snapshots"),
      Resolver.sonatypeRepo("releases"),
      Resolver.mavenLocal
    ),
  )
)

lazy val rocketchipExample = (project in file("."))
  .dependsOn(rocketchip)

lazy val rocketchip = (project in file("rocket-chip"))
