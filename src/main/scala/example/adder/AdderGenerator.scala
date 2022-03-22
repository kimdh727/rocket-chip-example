package example.adder

import java.io.File
import firrtl.stage.FirrtlStage
import freechips.rocketchip.system.RocketChipStage

object AdderGenerator extends App {
  val adderPackage = "example.adder"
  val adderName = "AdderTestHarness"
  val adderConfig = "AdderTestHarnessConfig"

  val targetDir = "build"
  val dir = new File(targetDir)
  if (!dir.exists()) dir.mkdirs()

  val logLevel = "trace"

  val targetDirAnno = Array("--target-dir", targetDir)
  val logLevelAnno = Array("--log-level", logLevel)
  val rocketchipStageAnno = Array(
    "--top-module", cp(adderPackage, adderName),
    "--configs", cp(adderPackage, adderConfig),
    "--name", adderName)
  val firrtlStageAnno = Array(
    "--input-file", targetDir + "/" + adderName + ".fir",
    "--emit-modules", "verilog")

  val rocketchipExecute = (new RocketChipStage).execute(
    targetDirAnno ++ logLevelAnno ++ rocketchipStageAnno, Seq.empty)

  val firrtlExecute = (new FirrtlStage).execute(
    targetDirAnno ++ logLevelAnno ++ firrtlStageAnno, Seq.empty)

  def cp(packageName: String, className: String): String = packageName + "." + className
}
