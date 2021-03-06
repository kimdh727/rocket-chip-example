package rce.util

import org.scalatest.flatspec.AnyFlatSpec

class GeneratorUtilSpec extends AnyFlatSpec {
  it should "pass" in {
    GeneratorUtil(
      module = classOf[freechips.rocketchip.system.TestHarness],
      configs = Seq(classOf[freechips.rocketchip.system.DefaultConfig]),
    )
  }
}
