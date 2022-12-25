package rce.example.regmapper

import chisel3._

import freechips.rocketchip.config._
import freechips.rocketchip.amba.apb._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.regmapper._

class APBRegister(base: BigInt, regmap: => Seq[RegField.Map])(implicit p: Parameters)
  extends LazyModule {

  val node = APBRegisterNode(AddressSet(base, x"FFF"))

  lazy val module = new LazyModuleImp(this) {
    withReset(reset.asAsyncReset) {
      node.regmap(regmap: _*)
    }
  }
}
