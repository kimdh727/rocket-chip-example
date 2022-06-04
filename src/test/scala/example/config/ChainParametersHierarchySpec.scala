package example.config

import org.scalatest.flatspec.AnyFlatSpec
import freechips.rocketchip.config._

class ChainParametersHierarchySpec extends AnyFlatSpec {
  case object Key extends Field[String]

  class ConfigA extends Config((site, here, up) => { case Key => "A" })
  class ConfigB extends Config((site, here, up) => { case Key => "B" })
  class ConfigC extends Config((site, here, up) => { case Key => "C" })
  class ConfigD extends Config((site, here, up) => { case Key => "D" })

  "chain parameters with ++ method" should "A" in {
    /**
      *    (c)
      *    / \
      *  (c)  C
      *  / \
      * A   B
      */
    val p = new Config(new ConfigA ++ new ConfigB ++ new ConfigC)
    assert(p(Key) == "A")
  }

  "chain parameters with orElse method" should "A" in {
    /**
      *    (c)
      *    / \
      *  (c)  C
      *  / \
      * A   B
      */
    val p = new Config(new ConfigA orElse new ConfigB orElse new ConfigC)
    assert(p(Key) == "A")
  }

  "chain parameters with alter methos" should "C" in {
    /**
      *    (c)
      *    / \
      *   C  (c)
      *      / \
      *     B   A
      */
    val p = new Config(new ConfigA alter new ConfigB alter new ConfigC)
    assert(p(Key) == "C")
  }

  "chain parameters" should "A" in {
    /**
      *     (c)
      *     / \
      *  (c)   (c)
      *  / \   / \
      * A   B C   D
      */
    val p = new Config((new ConfigA ++ new ConfigB) ++ (new ConfigC ++ new ConfigD))
    assert(p(Key) == "A")
  }
}
