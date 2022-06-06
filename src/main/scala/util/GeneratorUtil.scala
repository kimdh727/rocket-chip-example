package util

import java.io.File
import firrtl.stage.FirrtlStage
import freechips.rocketchip.system.RocketChipStage
import firrtl.options.TargetDirAnnotation
import freechips.rocketchip.stage._
import firrtl.stage._
import firrtl._
import logger._

object GeneratorUtil {
  def apply(
      module: String,
      configs: Seq[String] = Seq("example.config.EmptyConfig"),
      logLevel: String = "warn"): Unit = {

    val moduleName = module.split("\\.").last

    val dirName = s"build/$moduleName"

    val firrtlDirName = dirName + "/firrtl"
    val verilogDirName = dirName + "/verilog"

    val firrtlDir = new File(firrtlDirName)
    val verilogDir = new File(verilogDirName)

    if (!firrtlDir.exists()) firrtlDir.mkdirs()
    if (!verilogDir.exists()) verilogDir.mkdirs()

    new RocketChipStage().execute(Array(), Seq(
      TargetDirAnnotation(firrtlDirName),
      new TopModuleAnnotation(Class.forName(s"$module")),
      new ConfigsAnnotation(configs),
      new OutputBaseNameAnnotation(moduleName),
      LogLevelAnnotation(LogLevel(logLevel))
    ))

    new FirrtlStage().execute(Array(), Seq(
      TargetDirAnnotation(verilogDirName),
      FirrtlFileAnnotation(s"$firrtlDirName/$moduleName.fir"),
      RunFirrtlTransformAnnotation(new VerilogEmitter),
      EmitAllModulesAnnotation(classOf[VerilogEmitter]),
      LogLevelAnnotation(LogLevel(logLevel))
    ))
  }
}
