package rce.example.adder

import chisel3._
import freechips.rocketchip.config.Parameters
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.util.ElaborationArtefacts

/** top-level connector */
class AdderTestHarness()(implicit p: Parameters) extends LazyModule {
  val numOperands = 2

  val adder = LazyModule(new Adder)

  // 8 will be the downward-traveling widths from our drivers
  val drivers = Seq.fill(numOperands) { LazyModule(new AdderDriver(width = 8, numOutputs = 2)) }

  // 4 will be the upward-traveling width from our monitor
  val monitor: AdderMonitor = LazyModule(new AdderMonitor(width = 4, numOperands = numOperands))

  // create edges via binding operators between nodes in order to define a complete graph
  drivers.foreach { driver => adder.node := driver.node }
  drivers.zip(monitor.nodeSeq).foreach { case (driver, monitorNode) => monitorNode := driver.node }
  monitor.nodeSum := adder.node

  ElaborationArtefacts.add("graphml", graphML)

  class AdderTestHarnessImp extends LazyModuleImp(this) {
    val io = IO(new Bundle {
      val error = Output(Bool())
    })

    io.error := monitor.module.io.error

    when(monitor.module.io.error) {
      printf("something went wrong\n")
    }
  }

  lazy val module = new AdderTestHarnessImp

  override lazy val desiredName = "AdderTestHarness"
}
