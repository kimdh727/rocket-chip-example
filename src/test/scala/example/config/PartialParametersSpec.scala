package example.config

import org.scalatest.flatspec.AnyFlatSpec
import freechips.rocketchip.config._

class PartialParametersSpec extends AnyFlatSpec {
  case object Key0 extends Field[Int]
  case object Key1 extends Field[Int]
  case object Key2 extends Field[Int]

  val config = new Config((site, here, up) => {
    case Key1 => 1
    case Key2 => 2
  })

  "Key0" should "have no default" in {
    assertThrows[IllegalArgumentException](config(Key0))
  }

  "Key1" should "find 1" in {
    assert(config(Key1) == 1)
  }

  "Key2" should "find 2" in {
    assert(config(Key2) == 2)
  }
}
