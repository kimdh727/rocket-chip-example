package util

import org.scalatest.flatspec.AnyFlatSpec

class GeneratorUtilSpec extends AnyFlatSpec {
  it should "pass" in {
    GeneratorUtil(
      module = "freechips.rocketchip.system.TestHarness",
      configs = Seq("freechips.rocketchip.system.DefaultConfig"),
    )
  }
}
