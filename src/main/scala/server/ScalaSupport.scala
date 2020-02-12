package server

import devices.interfaces.Device
import utils.ToKotlin

/**
 * Shortcuts to use Support from Scala
 */
object ScalaSupport {
  /**
   * shortcut to read Support.devices for Scala API
   * @return the DeviceManager
   */
  def devices: DeviceManager = Support.INSTANCE.getDevices

  /**
   * Creates a new Device and adds it to the network
   * @param builder the way to create the Device from its generated ID
   * @tparam D the type of the device to create
   * @return the created device
   */
  def createAndAddDevice[D <: Device](builder: Integer => D): D =
    Support.INSTANCE.getDevices.createAndAddDevice(ToKotlin.toKotlinFun(builder)).asInstanceOf[D]
}
