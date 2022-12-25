package rce.example.regmapper

import org.scalatest.flatspec.AnyFlatSpec
import chiseltest._

import chisel3._

import freechips.rocketchip.config._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.regmapper._
import freechips.rocketchip.amba.apb._

import rce.transations.amba.apb.APBTransactions._

class APBRegisterSpec extends AnyFlatSpec with ChiselScalatestTester {

  import RegFieldMaps._

  "simple 32 x 100 regmap" should "register mapped" in {

    val beatBytes = 32 / 8
    val width     = 32
    val num       = 100

    lazy val regmap = simple(beatBytes, width, num)

    val harness = LazyModule(new APBRegisterTestHarness(regmap)(Parameters.empty))

    test(harness.module).withAnnotations(Seq(WriteVcdAnnotation)) { c =>
      require(c.io.params.dataBits == beatBytes * 8)

      c.clock.step()

      for (n <- 0 until num) {
        val addr = n * beatBytes
        val data = n

        write(addr, data)(c.io, c.clock)
        read(addr, data)(c.io, c.clock)
      }
    }
  }

  "bit field 1 x 100 regmap" should "register mapped" in {

    val beatBytes = 32 / 8
    val num       = 100

    lazy val regmap = bits(beatBytes, num)

    val harness = LazyModule(new APBRegisterTestHarness(regmap)(Parameters.empty))

    test(harness.module).withAnnotations(Seq(WriteVcdAnnotation)) { c =>
      require(c.io.params.dataBits == beatBytes * 8)

      c.clock.step()

      for (n <- 0 until num) {
        val addr = n / (beatBytes * 8) * beatBytes
        val data = x"1" << (n % (beatBytes * 8))

        write(addr, data)(c.io, c.clock)
        read(addr, data)(c.io, c.clock)
      }
    }
  }

  "mixed 32 x 4 regmap" should "register mapped" in {

    val beatBytes = 32 / 8
    val maps      = (0 until 4).map { i => Seq.tabulate(i + 1) { j => j + 1 } }

    lazy val regmap = mixed(beatBytes, maps)

    val harness = LazyModule(new APBRegisterTestHarness(regmap)(Parameters.empty))

    test(harness.module).withAnnotations(Seq(WriteVcdAnnotation)) { c =>
      require(c.io.params.dataBits == beatBytes * 8)

      c.clock.step()

      for (map <- maps.zipWithIndex) {
        val bits  = map._1.scanLeft(0) { _ + _ }
        val lows  = bits.init
        val highs = bits.tail

        val addr = map._2 * beatBytes
        val datas = highs.zip(lows).map { case (high, low) =>
          (x"1" << high) - (x"1" << low)
        }

        for (data <- datas) {
          write(addr, data)(c.io, c.clock)
          read(addr, data)(c.io, c.clock)
        }
      }
    }
  }
}

object RegFieldMaps {

  type RegFieldMap = Seq[RegField.Map]

  /** Generate simple register field map
    *
    * @example
    *   {{{
    *   simple(1, 8, 4): Seq[RegField.Map]
    *
    *   0x0: |-------------- 8 --------------|
    *   0x1: |-------------- 8 --------------|
    *   0x2: |-------------- 8 --------------|
    *   0x3: |-------------- 8 --------------|
    *   }}}
    */
  def simple(beatBytes: Int, width: Int, num: Int): RegFieldMap = {
    val regs = Seq.fill(num)(RegInit(0.U(width.W)))

    regs.zipWithIndex.map { case (reg, idx) =>
      idx * beatBytes -> Seq(RegField(reg.getWidth, reg))
    }
  }

  /** Generate bit register field map
    *
    * @example
    *   {{{
    *   bits(1, 16): Seq[RegField.Map]
    *
    *   0x0: | 1 | 1 | 1 | 1 | 1 | 1 | 1 | 1 |
    *   0x1: | 1 | 1 | 1 | 1 | 1 | 1 | 1 | 1 |
    *   }}}
    */
  def bits(beatBytes: Int, num: Int): RegFieldMap = {
    val regs = Seq.fill(num)(RegInit(0.U(1.W)))

    regs.grouped(beatBytes * 8).zipWithIndex.map { case (reg, idx) =>
      idx * beatBytes -> reg.map(r => RegField(r.getWidth, r))
    }.toSeq
  }

  /** Generate mixed register field map
    *
    * @example
    *   {{{
    *   mixed(1, Seq(
    *       Seq(1, 1, 1, 1, 1, 1, 1, 1),
    *       Seq(2,    2,    2,    2   ),
    *       Seq(3,       5            ),
    *       Seq(8                     )
    *   ))
    *
    *   0x0: | 1 | 1 | 1 | 1 | 1 | 1 | 1 | 1 |
    *   0x1: |-- 2 --|-- 2 --|-- 2 --|-- 2 --|
    *   0x2: |---- 3 ----|-------- 5 --------|
    *   0x3: |-------------- 8 --------------|
    *   }}}
    */
  def mixed(beatBytes: Int, maps: Seq[Seq[Int]]): RegFieldMap = {
    val regs = maps.map(_.map(width => RegInit(0.U(width.W))))

    regs.zipWithIndex.map { case (reg, idx) =>
      idx * beatBytes -> reg.map(r => RegField(r.getWidth, r))
    }
  }
}

class APBRegisterTestHarness(regmap: => Seq[RegField.Map])(implicit p: Parameters)
  extends LazyModule {

  val srcs = APBMasterNode(Seq(
    APBMasterPortParameters(Seq(
      APBMasterParameters("apb")
    ))
  ))

  val ctrl = LazyModule(new APBRegister(0, regmap))

  ctrl.node := srcs

  lazy val module = new APBRegisterTestHarnessImp

  class APBRegisterTestHarnessImp extends LazyModuleImp(this) {
    val io = srcs.makeIOs()(ValName("apb")).head
  }
}
