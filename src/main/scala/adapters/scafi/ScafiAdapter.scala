package adapters.scafi

import java.util.Date
import java.util.function.Consumer

import adapters.Adapter
import devices.{Device, EmulatedDevice}
import ScafiIncarnation._
import communication.{Message, MessageType}
import devices.client.Client
import server.{ScalaSupport, Support}

import scala.collection.mutable
import scala.util.Random

case class ScafiAdapter(device: Device, program: AggregateProgram, server: Device = null) extends Adapter {
  private val defaultSensors: Map[LSNS, Device => Any] = Map(
    LSNS_RANDOM -> (_ => new Random().nextDouble()),
    LSNS_TIME -> (_ => System.currentTimeMillis()),
    LSNS_TIMESTAMP -> (_ => new Date().getTime)
  )
  var customSensors: mutable.Map[LSNS, Device => Any] = mutable.Map()

  private def obtainSensorValues(sensors: Map[LSNS, Device => Any]): Map[LSNS, Any] =
    sensors.map { case (name, value) => (name, value(device)) }

  override def execute() {
    import collection.JavaConverters._

    val exports = device.getStatus.asScala
      .filter(m => m.getType == MessageType.Result && m.getContent.isInstanceOf[EXPORT])
      .map(m => m.getSenderUid -> m.getContent.asInstanceOf[EXPORT])
      .toMap

    val sensors = obtainSensorValues(defaultSensors) ++ obtainSensorValues(customSensors.toMap)

    val result = program(factory.context(device.getId, exports, sensors))

    device.showResult(s"$device: " + result.root())

    //tell my export to my neighbours (myself included)
    val toSend = new Message(device.getId, MessageType.Result, result)

    if (!device.isInstanceOf[Client]) {
      //if executing in the server, neighbours are known
      for (d <- ScalaSupport.devices.getNeighbours(device, true).asScala)
        d.tell(toSend)
    } else
      //otherwise I send the message to the server, who will send it to my neighbours
      server.tell(new Message(device.getId, MessageType.SendToNeighbours, toSend))
  }

  override def getDevice: Device = device
}
