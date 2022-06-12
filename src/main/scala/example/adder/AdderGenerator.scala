package rce.example.adder

import rce.util.GeneratorUtil

object AdderGenerator extends App {
  GeneratorUtil(module = "example.adder.AdderTestHarness", logLevel = "trace")
}
