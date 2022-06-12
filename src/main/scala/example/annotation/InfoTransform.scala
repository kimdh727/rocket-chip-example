package rce.example.annotation

import firrtl.Transform
import firrtl.DependencyAPIMigration
import firrtl.CircuitState
import firrtl.stage.Forms.HighForm
import firrtl.annotations._

class InfoTransform extends Transform with DependencyAPIMigration {

  override def prerequisites = HighForm

  override protected def execute(state: CircuitState): CircuitState = {
    println(s"Starting transform $this")

    val annotationsx = state.annotations.flatMap {
      case InfoAnnotation(t: GenericTarget, info) =>
        println(s"Generic - ${t.serialize} $info")
        None
      case InfoAnnotation(t: CircuitTarget, info) =>
        println(s"Circuit - ${t.serialize} $info")
        None
      case InfoAnnotation(t: ModuleTarget, info) =>
        println(s"Module - ${t.serialize} $info")
        None
      case InfoAnnotation(t: ReferenceTarget, info) =>
        println(s"Reference - ${t.serialize} $info")
        None
      case InfoAnnotation(t: InstanceTarget, info) =>
        println(s"Instance - ${t.serialize} $info")
        None
      case anno => Seq(anno)
    }

    state.copy(annotations = annotationsx)
  }


}
