package config

import org.scalatest.flatspec.AnyFlatSpec
import freechips.rocketchip.config._

import example.config._

case object Key0 extends Field[String]
case object Key1 extends Field[String]("Key1")
case object Key2 extends Field[String]("Key2")

class EmptyConfigSpec extends AnyFlatSpec {
  val config = EmptyConfig

  "Key0" should "have no default" in {
    assertThrows[IllegalArgumentException](config(Key0))
  }

  "Key1" should "have default" in {
    assert(config(Key1) == "Key1")
  }

  "Key2" should "have default" in {
    assert(config(Key2) == "Key2")
  }
}
