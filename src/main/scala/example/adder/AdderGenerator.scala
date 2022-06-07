package example.adder

import util.GeneratorUtil

object AdderGenerator extends App {
  GeneratorUtil(module = "example.adder.AdderTestHarness", logLevel = "trace")
}
