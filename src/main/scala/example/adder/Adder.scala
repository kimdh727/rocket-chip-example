package rce.example.adder

import freechips.rocketchip.config.Parameters
import freechips.rocketchip.diplomacy._

/** adder DUT (nexus) */
class Adder(implicit p: Parameters) extends LazyModule {
  val node = new AdderNode (
    { case dps: Seq[DownwardParam] =>
      require(dps.forall(dp => dp.width == dps.head.width), "inward, downward adder widths must be equivalent")
      dps.head.copy(paramInfo = "Adder")
    },
    { case ups: Seq[UpwardParam] =>
      require(ups.forall(up => up.width == ups.head.width), "outward, upward adder widths must be equivalent")
      ups.head.copy(paramInfo = "Adder")
    }
  )

  class AdderImp extends LazyModuleImp(this) {
    require(node.in.size >= 2)
    node.out.head._1 := node.in.unzip._1.reduce(_ + _)
  }

  lazy val module = new AdderImp

  override lazy val desiredName = "Adder"
}
