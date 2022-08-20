package rce.util

import org.scalatest.flatspec.AnyFlatSpec

class GraphMLUtilSpec extends AnyFlatSpec {
  ignore should "pass" in {
    GraphMLUtil(
      module = classOf[freechips.rocketchip.system.ExampleRocketSystem],
      configs = Seq(classOf[freechips.rocketchip.system.DefaultConfig]),
    )
  }
}
