package rce.example.adder

import chisel3._
import freechips.rocketchip.config.Parameters
import freechips.rocketchip.diplomacy._

/** monitor (sink) */
class AdderMonitor(width: Int, numOperands: Int)(implicit p: Parameters) extends LazyModule {
  val paramInfo = Seq.tabulate(numOperands)(n => s"Monitor $n")
  val nodeSeq = Seq.tabulate(numOperands)(n => new AdderMonitorNode(UpwardParam(width, paramInfo(n))))
  val nodeSum = new AdderMonitorNode(UpwardParam(width, "Monitor Sum"))

  class AdderMonitorImp extends LazyModuleImp(this) {
    val io = IO(new Bundle {
      val error = Output(Bool())
    })

    // print operation
    printf(nodeSeq.map(node => p"${node.in.head._1}").reduce(_ + p" + " + _) + p" = ${nodeSum.in.head._1}\n")

    // basic correctness checking
    io.error := nodeSum.in.head._1 =/= nodeSeq.map(_.in.head._1).reduce(_ + _)
  }

  lazy val module = new AdderMonitorImp

  override lazy val desiredName = "AdderMonitor"
}
