package rce.transations.amba.apb

import chiseltest._

import chisel3._

import freechips.rocketchip.amba.apb._

object APBTransactions {

  def write(addr: BigInt, data: BigInt, strb: BigInt)(bundle: APBBundle, clock: Clock): Unit = {
    bundle.psel.poke(true.B)

    bundle.pwrite.poke(true.B)
    bundle.paddr.poke(addr.U)
    bundle.pprot.poke(0.U)
    bundle.pwdata.poke(data.U)
    bundle.pstrb.poke(strb.U)

    clock.step()

    bundle.penable.poke(true.B)

    clock.step()

    while (fire(bundle)) {
      clock.step()
    }

    clear(bundle)
  }

  def write(addr: BigInt, data: BigInt)(bundle: APBBundle, clock: Clock): Unit = {
    write(addr, data, ((1 << bundle.pstrb.getWidth) - 1))(bundle, clock)
  }

  def read(addr: BigInt, data: Option[BigInt] = None)(bundle: APBBundle, clock: Clock): Unit = {
    bundle.psel.poke(true.B)

    bundle.pwrite.poke(false.B)
    bundle.paddr.poke(addr.U)
    bundle.pprot.poke(0.U)
    bundle.pwdata.poke(0.U)
    bundle.pstrb.poke(0.U)

    clock.step()

    bundle.penable.poke(true.B)

    clock.step()

    while (fire(bundle)) {
      clock.step()
    }

    data.foreach(d => bundle.prdata.expect(d.U))

    clear(bundle)
  }

  def read(addr: BigInt, data: BigInt)(bundle: APBBundle, clock: Clock): Unit = {
    read(addr, Some(data))(bundle, clock)
  }

  private def fire(bundle: APBBundle) = {
    (bundle.psel.peek() == true.B) && (bundle.pready.peek() == true.B)
  }

  private def clear(bundle: APBBundle) = {
    bundle.psel.poke(false.B)
    bundle.penable.poke(false.B)

    bundle.pwrite.poke(false.B)
    bundle.paddr.poke(0.U)
    bundle.pprot.poke(0.U)
    bundle.pwdata.poke(0.U)
    bundle.pstrb.poke(0.U)
  }
}
