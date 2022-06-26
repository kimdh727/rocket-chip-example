package rce.util

import java.io.File

import firrtl.stage._
import firrtl.options._
import firrtl.VerilogEmitter
import logger.{LogLevel, LogLevelAnnotation}
import freechips.rocketchip.stage._
import freechips.rocketchip.system.RocketChipStage
import freechips.rocketchip.config.Config

object GeneratorUtil {

  private[this] def dir(path: String) = {
    val dir = new File(path)
    if (!dir.exists()) dir.mkdirs()
    dir.getPath()
  }

  def apply[C <: Config](
      module: Class[_],
      configs: Seq[Class[C]] = Seq(classOf[rce.example.config.EmptyConfig]),
      logLevel: String = "warn",
  ): Unit = {

    val moduleName = module.getName.split("\\.").last

    val dirName = s"build/$moduleName"

    val firrtlDir  = dir(dirName + "/firrtl")
    val verilogDir = dir(dirName + "/verilog")

    val commonAnnotations = Seq(
      LogLevelAnnotation(LogLevel(logLevel))
    )

    val rocketChipStageAnnotations = Seq(
      TargetDirAnnotation(firrtlDir),
      new TopModuleAnnotation(module),
      new ConfigsAnnotation(configs.map(_.getName())),
      new OutputBaseNameAnnotation(moduleName)
    )

    val firrtlStageAnnotations = Seq(
      TargetDirAnnotation(verilogDir),
      FirrtlFileAnnotation(s"$firrtlDir/$moduleName.fir"),
      InputAnnotationFileAnnotation(s"$firrtlDir/$moduleName.anno.json"),
      RunFirrtlTransformAnnotation(new VerilogEmitter),
      AllowUnrecognizedAnnotations
    )

    new RocketChipStage().execute(
      args = Array(),
      annotations = commonAnnotations ++ rocketChipStageAnnotations
    )

    new FirrtlStage().execute(
      args = Array(),
      annotations = commonAnnotations ++ firrtlStageAnnotations
    )
  }
}
