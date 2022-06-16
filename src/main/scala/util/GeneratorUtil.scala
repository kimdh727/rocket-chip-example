package rce.util

import java.io.File
import firrtl.stage.FirrtlStage
import freechips.rocketchip.system.RocketChipStage
import rce.example.config.EmptyConfig

object GeneratorUtil {
  def apply(
      module: String,
      configs: Seq[String] = Seq(classOf[EmptyConfig].getName()),
      logLevel: String = "warn"): Unit = {

    val moduleName = module.split("\\.").last

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
        "--top-module", module,
        "--configs", configs.mkString(","),
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
