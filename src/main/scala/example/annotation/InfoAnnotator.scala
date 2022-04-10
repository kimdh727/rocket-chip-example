package example.annotation

import chisel3.internal.InstanceId
import chisel3.experimental.annotate
import chisel3.experimental.ChiselAnnotation
import chisel3.experimental.RunFirrtlTransform

object InfoAnnotator {
  def info(component: InstanceId, info: String): Unit = {
    annotate(new ChiselAnnotation with RunFirrtlTransform {
      override def toFirrtl = InfoAnnotation(component.toTarget, info)
      override def transformClass = classOf[InfoTransform]
    })
  }
}
