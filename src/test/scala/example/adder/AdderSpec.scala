package rce.example.adder

import org.scalatest.flatspec.AnyFlatSpec
import chiseltest._

import chisel3._
import freechips.rocketchip.config.Parameters
import freechips.rocketchip.diplomacy.LazyModule

class AdderSpec extends AnyFlatSpec with ChiselScalatestTester {
  val adder = LazyModule(new AdderTestHarness()(Parameters.empty))

  behavior of "Adder"

  it should "a + b = y" in {
    test (adder.module) { c =>
      for (n <- 0 until 100) {
        c.clock.step()
        c.io.error.expect(false.B)
      }
    }
  }
}
