package rce.example.annotation

import firrtl.annotations.Target
import firrtl.annotations.SingleTargetAnnotation

/** An annotation that contains some string information */
case class InfoAnnotation(target: Target, info: String) extends SingleTargetAnnotation[Target] {
  def duplicate(newTarget: Target) = this.copy(target = newTarget)
}
