package example.config

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers._
import freechips.rocketchip.config._

class SiteHereMapSpec extends AnyFlatSpec {
  case object Key0 extends Field[Int]
  case object Key1 extends Field[Int](1)
  case object Key2 extends Field[Int](2)
  case object Key3 extends Field[Int](3)

  it should "site here map operation" in {
    class ConfigA extends Config((site, here, up) => {
      case Key0 => 1
    })

    class ConfigB extends Config((site, here, up) => {
      case Key0 => 2
      case Key1 => site(Key0)
      case Key2 => here(Key0)
      case Key3 => up  (Key0)
    })

    class ConfigC extends Config((site, here, up) => {
      case Key0 => 3
    })

    /**
      * |         | Key0 |    Key1    |    Key2    |    Key3    |
      * | ------- | ---- | ---------- | ---------- | ---------- |
      * | default |      | 1          | 2          | 3          |
      * | ConfigA | 1    |            |            |            |
      * | ConfigB | 2    | site(Key0) | here(Key0) | up(Key0)   |
      * | ConfigC | 3    |            |            |            |
      */
    val config = new Config(
      new ConfigC ++
      new ConfigB ++
      new ConfigA)

    config(Key0) should be (3)
    config(Key1) should be (3)
    config(Key2) should be (2)
    config(Key3) should be (1)
  }

  it should "Stack Overflow Error" in {
    /**
      * |        |    Key0    |    Key1    |    Key2    |    Key3    |
      * | ------ | ---------- | ---------- | ---------- | ---------- |
      * | Config | site(Key0) | here(Key1) | here(Key3) | here(Key2) |
      */
    val config = new Config((site, here, up) => {
      case Key0 => site(Key0)
      case Key1 => here(Key1)
      case Key2 => here(Key3)
      case Key3 => here(Key2)
    })

    the [StackOverflowError] thrownBy config(Key0)
    the [StackOverflowError] thrownBy config(Key1)
    the [StackOverflowError] thrownBy config(Key2)
    the [StackOverflowError] thrownBy config(Key3)
  }

  it should "default value" in {
    /**
      * |         |   Key0   |   Key1   |   Key2   |   Key3   |
      * | ------- | -------- | -------- | -------- | -------- |
      * | default |          | 1        | 2        | 3        |
      * | Config  | up(Key0) | up(Key1) | up(Key3) | up(Key2) |
      */
    val config = new Config((site, here, up) => {
      case Key0 => up(Key0)
      case Key1 => up(Key1)
      case Key2 => up(Key2)
      case Key3 => up(Key3)
    })

    the [IllegalArgumentException] thrownBy config(Key0)
    config(Key1) should be (1)
    config(Key2) should be (2)
    config(Key3) should be (3)
  }


}
