package server

import adapters.Adapter
import devices.interfaces.EmulatedDevice
import utils.FromKotlin.def1
import utils.ToKotlin

/**
 * Shortcut to use the Execution object from Scala
 */
object ScalaExecution {
  def getAdapter: EmulatedDevice => Adapter = {
    def1(Execution.INSTANCE.getAdapter)
  }
  def setAdapter(adapter: EmulatedDevice => Adapter): Unit = {
    Execution.INSTANCE.setAdapter(ToKotlin.toKotlinFun(adapter))
  }
}
