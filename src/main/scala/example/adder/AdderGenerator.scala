package example.adder

import chisel3.stage.ChiselStage
import freechips.rocketchip.config.Parameters
import freechips.rocketchip.diplomacy._

object AdderGenerator extends App {
  val verilog = (new ChiselStage).emitVerilog(
    LazyModule(new AdderTestHarness()(Parameters.empty)).module
  )
}
