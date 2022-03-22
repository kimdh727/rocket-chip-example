inThisBuild(
  List(
    scalaVersion := "2.12.15",
    scalacOptions ++= Seq("-feature", "-language:reflectiveCalls"),
    libraryDependencies ++= Seq("org.scalatest" %% "scalatest" % "3.2.0" % "test"),
    addCompilerPlugin("edu.berkeley.cs" % "chisel3-plugin" % "3.5.0" cross CrossVersion.full),
    libraryDependencies += "edu.berkeley.cs" %% "rocketchip" % "1.5-SNAPSHOT",
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    scalacOptions += "-Ywarn-unused-import",
    resolvers ++= Seq(
      Resolver.sonatypeRepo("snapshots"),
      Resolver.sonatypeRepo("releases"),
    ),
  )
)

lazy val rocketchipExample = (project in file("."))
