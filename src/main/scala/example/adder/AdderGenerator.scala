package rce.example.adder

import rce.util.GeneratorUtil

object AdderGenerator extends App {
  GeneratorUtil(module = "rce.example.adder.AdderTestHarness", logLevel = "trace")
}
