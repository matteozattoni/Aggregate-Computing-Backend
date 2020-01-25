package devices

import adapters.Adapter
import devices.implementations.VirtualDevice
import devices.interfaces.EmulatedDevice
import utils.ToKotlin

/**
 * Factory to simplify the creation of Devices from Scala
 */
object DeviceFactory {
  def virtual(id: Int, name: String, adapter: EmulatedDevice => Adapter): VirtualDevice = {
    new VirtualDevice(id, name, ToKotlin.toKotlinFun(adapter), ToKotlin.toKotlinFun(s => println(s)))
  }
}
