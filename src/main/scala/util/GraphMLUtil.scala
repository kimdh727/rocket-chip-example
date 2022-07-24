package rce.util

import java.io.File

import freechips.rocketchip.diplomacy._
import freechips.rocketchip.config._
import freechips.rocketchip.util.HasRocketChipStageUtils

object GraphMLUtil extends HasRocketChipStageUtils {

  private[this] def dir(path: String) = {
    val dir = new File(path)
    if (!dir.exists()) dir.mkdirs()
    dir.getPath()
  }

  private[this] def getGraphML(module: LazyModule): String = {
    import scala.collection.mutable.ArrayBuffer
    val indexBuf: ArrayBuffer[LazyModule] = ArrayBuffer.empty
    def index(module: LazyModule): Int = {
      if (!indexBuf.contains(module))
        indexBuf += module
      indexBuf.indexOf(module) + 1
    }

    def getNodesGraphML(module: LazyModule, buf: StringBuilder, pad: String): Unit = {
      buf ++= s"""$pad<node id=\"${index(module)}\">\n"""
      buf ++= s"""$pad  <data key=\"n\"><y:ShapeNode><y:NodeLabel modelName=\"sides\" modelPosition=\"w\" rotationAngle=\"270.0\">${module.name}</y:NodeLabel><y:BorderStyle type=\"${if (module.shouldBeInlined) "dotted" else "line"}\"/></y:ShapeNode></data>\n"""
      buf ++= s"""$pad  <data key=\"d\">${module.name} (${module.name})</data>\n"""
      buf ++= s"""$pad  <graph id=\"${index(module)}::\" edgedefault=\"directed\">\n"""
      module.getNodes.filter(!_.omitGraphML).foreach { n =>
        buf ++= s"""$pad    <node id=\"${index{module}}::${n.index}\">\n"""
        buf ++= s"""$pad      <data key=\"n\"><y:ShapeNode><y:Shape type="ellipse"/><y:Fill color="#FFCC00" transparent=\"${n.circuitIdentity}\"/></y:ShapeNode></data>\n"""
        buf ++= s"""$pad      <data key=\"d\">${n.formatNode}, \n${n.nodedebugstring}</data>\n"""
        buf ++= s"""$pad    </node>\n"""
      }
      module.getChildren.filter(!_.omitGraphML).foreach(c => getNodesGraphML(c, buf, pad + "    "))
      buf ++= s"""$pad  </graph>\n"""
      buf ++= s"""$pad</node>\n"""
    }

    def getEdgesGraphML(module: LazyModule, buf: StringBuilder, pad: String): Unit = {
      module.getNodes.filter(!_.omitGraphML) foreach { n =>
        n.outputs.filter(!_._1.omitGraphML).foreach { case (o, edge) =>
          val RenderedEdge(colour, label, flipped) = edge
          buf ++= pad
          buf ++= "<edge"
          if (flipped) {
            buf ++= s""" target=\"${index(module)}::${n.index}\""""
            buf ++= s""" source=\"${index(o.lazyModule)}::${o.index}\">"""
          } else {
            buf ++= s""" source=\"${index(module)}::${n.index}\""""
            buf ++= s""" target=\"${index(o.lazyModule)}::${o.index}\">"""
          }
          buf ++= s"""<data key=\"e\"><y:PolyLineEdge>"""
          if (flipped) {
            buf ++= s"""<y:Arrows source=\"standard\" target=\"none\"/>"""
          } else {
            buf ++= s"""<y:Arrows source=\"none\" target=\"standard\"/>"""
          }
          buf ++= s"""<y:LineStyle color=\"$colour\" type=\"line\" width=\"1.0\"/>"""
          buf ++= s"""<y:EdgeLabel modelName=\"centered\" rotationAngle=\"270.0\">$label</y:EdgeLabel>"""
          buf ++= s"""</y:PolyLineEdge></data></edge>\n"""
        }
      }
      module.getChildren.filter(!_.omitGraphML).foreach(c => getEdgesGraphML(c, buf, pad))
    }

    val buf = new StringBuilder
    buf ++= "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
    buf ++= "<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\" xmlns:y=\"http://www.yworks.com/xml/graphml\">\n"
    buf ++= "  <key for=\"node\" id=\"n\" yfiles.type=\"nodegraphics\"/>\n"
    buf ++= "  <key for=\"edge\" id=\"e\" yfiles.type=\"edgegraphics\"/>\n"
    buf ++= "  <key for=\"node\" id=\"d\" attr.name=\"Description\" attr.type=\"string\"/>\n"
    buf ++= "  <graph id=\"G\" edgedefault=\"directed\">\n"
    getNodesGraphML(module, buf, "    ")
    getEdgesGraphML(module, buf, "    ")
    buf ++= "  </graph>\n"
    buf ++= "</graphml>\n"
    buf.toString
  }

  def apply[C <: Config](
    module: Class[_],
    configs: Seq[Class[C]] = Seq(classOf[rce.example.config.EmptyConfig]),
  ): Unit = {

    val moduleName = module.getName.split("\\.").last
    val moduleInstance: LazyModule = module
      .getConstructor(classOf[Parameters])
      .newInstance(getConfig(configs.map(_.getName))) match {
        case a: LazyModule => a
        case _ => throw new Exception("Support LazyModule only")
      }

    writeOutputFile(
      targetDir = dir(s"build/$moduleName/firrtl"),
      fname = s"$moduleName.graphml",
      contents = getGraphML(moduleInstance))
  }
}
