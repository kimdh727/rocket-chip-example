package rce.example.adder

import rce.util.GeneratorUtil

object AdderGenerator extends App {
  GeneratorUtil(module = classOf[AdderTestHarness].getName(), logLevel = "trace")
}
