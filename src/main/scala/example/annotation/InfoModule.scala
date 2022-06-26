package rce.example.annotation

import chisel3._
import chipsalliance.rocketchip.config.Parameters
import freechips.rocketchip.diplomacy._

class InfoModule()(implicit p: Parameters) extends LazyModule {
  lazy val module = new LazyModuleImp(this) {
    val io = IO(new Bundle {
      val in  = Input(Bool())
      val out = Output(Bool())
    })

    io.out := io.in

    InfoAnnotator.info(this, s"Module name is ${this.name}")
    InfoAnnotator.info(io, s"Module port ${this.io}")
  }
}
