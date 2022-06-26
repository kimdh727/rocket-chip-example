package rce.example.adder

import chisel3.util.random.FibonacciLFSR
import freechips.rocketchip.config.Parameters
import freechips.rocketchip.diplomacy._

/** driver (source) drives one random number on multiple outputs
  */
class AdderDriver(width: Int, numOutputs: Int)(implicit p: Parameters) extends LazyModule {
  val paramInfo = Seq.tabulate(numOutputs)(n => s"Driver $n")

  val node = new AdderDriverNode(Seq.tabulate(numOutputs)(n => DownwardParam(width, paramInfo(n))))

  class AdderDriverImp extends LazyModuleImp(this) {
    // check that node parameters converge after negotiation
    val negotiatedWidths = node.edges.out.map(_.width)
    require(
      negotiatedWidths.forall(_ == negotiatedWidths.head),
      "outputs must all have agreed on same width"
    )
    val finalWidth = negotiatedWidths.head

    // generate random addend (notice the use of the negotiated width)
    val randomAddend = FibonacciLFSR.maxPeriod(finalWidth)

    // drive signals
    node.out.foreach { case (addend, _) => addend := randomAddend }
  }

  lazy val module = new AdderDriverImp

  override lazy val desiredName = "AdderDriver"
}
