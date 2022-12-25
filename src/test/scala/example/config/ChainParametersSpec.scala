package rce.example.config

import org.scalatest.flatspec.AnyFlatSpec
import freechips.rocketchip.config._

class ChainParametersSpec extends AnyFlatSpec {
  case object Key0 extends Field[Int]
  case object Key1 extends Field[Int]
  case object Key2 extends Field[Int]

  class Config0 extends Config((site, here, up) => {
      case Key0 => 0
      case Key1 => 1
      case Key2 => 2
    })

  class Config1 extends Config((site, here, up) => {
      case Key1 => 10
      case Key2 => 20
    })

  class Config2 extends Config((site, here, up) => {
      case Key2 => 200
    })

  /*
   * |         | Key0 | Key1 | Key2 |
   * |:--------|:-----|:-----|:-----|
   * | Config0 | 0    | 1    | 2    |
   * | Config1 |      | 10   | 20   |
   * | Config2 |      |      | 200  |
   */
  val config = new Config2 ++
    new Config1 ++
    new Config0

  "Key0" should "find 0" in {
    assert(config(Key0) == 0)
  }

  "Key1" should "find 10" in {
    assert(config(Key1) == 10)
  }

  "Key2" should "find 200" in {
    assert(config(Key2) == 200)
  }
}
