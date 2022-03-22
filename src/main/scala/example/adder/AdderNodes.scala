package example.adder

import chisel3._
import chisel3.internal.sourceinfo.SourceInfo
import freechips.rocketchip.config.Parameters
import freechips.rocketchip.diplomacy._

// PARAMETER TYPES:                       D              U            E          B
object AdderNodeImp extends SimpleNodeImp[DownwardParam, UpwardParam, EdgeParam, UInt] {
  def edge(pd: DownwardParam, pu: UpwardParam, p: Parameters, sourceInfo: SourceInfo) = {
    val paramInfo = s"PD: ${pd.paramInfo} PU: ${pu.paramInfo}"
    if (pd.width < pu.width) EdgeParam(pd.width, paramInfo) else EdgeParam(pu.width, paramInfo)
  }
  def bundle(e: EdgeParam) = UInt(e.width.W)
  def render(e: EdgeParam) = RenderedEdge("blue", s"width = ${e.width}, info = ${e.paramInfo}")
}

/** node for [[AdderDriver]] (source) */
class AdderDriverNode(widths: Seq[DownwardParam])(implicit valName: ValName)
  extends SourceNode(AdderNodeImp)(widths)

/** node for [[AdderMonitor]] (sink) */
class AdderMonitorNode(width: UpwardParam)(implicit valName: ValName)
  extends SinkNode(AdderNodeImp)(Seq(width))

/** node for [[Adder]] (nexus) */
class AdderNode(
    dFn: Seq[DownwardParam] => DownwardParam,
    uFn: Seq[UpwardParam] => UpwardParam)(
    implicit valName: ValName)
  extends NexusNode(AdderNodeImp)(dFn, uFn)
