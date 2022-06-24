package rce.util

import java.io.File

import firrtl.stage.FirrtlStage
import freechips.rocketchip.system.RocketChipStage
import freechips.rocketchip.config.Config

import rce.example.config.EmptyConfig

object GeneratorUtil {
  def apply[C <: Config](
      module: Class[_],
      configs: Seq[Class[C]] = Seq(classOf[EmptyConfig]),
      logLevel: String = "warn"): Unit = {

    val moduleName = module.getName.split("\\.").last

    val dirName = s"build/$moduleName"

    val firrtlDirName = dirName + "/firrtl"
    val verilogDirName = dirName + "/verilog"

    val firrtlDir = new File(firrtlDirName)
    val verilogDir = new File(verilogDirName)

    if (!firrtlDir.exists()) firrtlDir.mkdirs()
    if (!verilogDir.exists()) verilogDir.mkdirs()

    new RocketChipStage().execute(
      args = Array(
        "--target-dir", firrtlDirName,
        "--log-level", logLevel,
        "--top-module", module.getName,
        "--configs", configs.map(_.getName).mkString(","),
        "--name", moduleName,
      ),
      annotations = Seq()
    )

    new FirrtlStage().execute(
      args = Array(
        "--target-dir", verilogDirName,
        "--log-level", logLevel,
        "--input-file", s"$firrtlDirName/$moduleName.fir",
        "--emit-modules", "verilog",
        "--annotation-file", s"$firrtlDirName/$moduleName.anno.json",
        "--allow-unrecognized-annotations",
      ),
      annotations = Seq()
    )
  }
}
