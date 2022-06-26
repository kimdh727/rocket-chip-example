inThisBuild(
  List(
    version := "0.1.0-SNAPSHOT",
    scalaVersion := "2.12.15",
    scalacOptions ++= Seq(
      "-feature",
      "-language:reflectiveCalls",
      "-Xfatal-warnings",
      "-Xlint",
    ),
    addCompilerPlugin("edu.berkeley.cs" % "chisel3-plugin" % "3.5.0" cross CrossVersion.full),
    libraryDependencies += "edu.berkeley.cs" %% "rocketchip" % "1.5-SNAPSHOT",
    libraryDependencies += "org.scalatest"   %% "scalatest"  % "3.2.0" % "test",
    libraryDependencies += "edu.berkeley.cs" %% "chiseltest" % "0.5.0" % "test",
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    resolvers ++= Seq(
      Resolver.sonatypeRepo("snapshots"),
      Resolver.sonatypeRepo("releases"),
    ),
  )
)

lazy val rocketchipExample = (project in file("."))
  .settings(name := "rce")
