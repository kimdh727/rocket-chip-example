package rce.example.annotation

import chisel3._
import chisel3.stage._

class InfoModule extends Module {
  val io = IO(new Bundle{
    val in = Input(Bool())
    val out = Output(Bool())
  })

  io.out := io.in

  InfoAnnotator.info(this, s"Module name is ${this.name}")
  InfoAnnotator.info(io, s"Module port ${this.io}")
}

object InfoAnnotationTester extends App {
  val targetDir = "build/InfoModule"

  val logLevel = "warn"

  val targetDirAnno = Array("--target-dir", targetDir)
  val logLevelAnno = Array("--log-level", logLevel)
  val chiselStageAnno = Array(
    "--module", "rce.example.annotation.InfoModule",
    "--emit-modules", "verilog")

  (new ChiselStage).execute(targetDirAnno ++ logLevelAnno ++ chiselStageAnno, Seq())
}
