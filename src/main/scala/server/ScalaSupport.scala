package server

import adapters.Adapter
import devices.{Device, EmulatedDevice}
import kotlin.jvm.functions.Function1

object ScalaSupport {
  /**
   * shortcut for Scala API
   * @return the DeviceManager
   */
  def devices: DeviceManager = Support.INSTANCE.getDevices

  private def toKotlinFun[A,B](builder: (A) => B): Function1[A,B] =
    new Function1[A,B] {
      override def invoke(p1: A): B = builder(p1)
    }

  /**
   * Creates a new Device, with the given Adapter, and adds it to the network
   * @param builder the way to create the Device from its generated ID
   * @param adapterBuilder the way to create the Adapter from the device
   * @tparam D the type of the device to create
   * @return the created device
   */
  def createAndAddDevice[D <: EmulatedDevice, A <: Adapter](builder: (Integer) => D, adapterBuilder:(Device) => A): (D,A) = {
    val device = createAndAddDevice(builder)
    val adapter = adapterBuilder(device)
    device.setAdapter(adapter)
    (device, adapter)
  }

  /**
   * Creates a new Device and adds it to the network
   * @param builder the way to create the Device from its generated ID
   * @tparam D the type of the device to create
   * @return the created device
   */
  def createAndAddDevice[D <: Device](builder: (Integer) => D): D =
    Support.INSTANCE.getDevices.createAndAddDevice(toKotlinFun(builder)).asInstanceOf[D]
}
