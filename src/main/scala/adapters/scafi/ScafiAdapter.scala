package adapters.scafi

import java.util.Date
import java.util.function.Consumer

import adapters.Adapter
import devices.Device
import ScafiIncarnation._
import communication.{Message, MessageType}
import server.Support

import scala.collection.mutable
import scala.util.Random

case class ScafiAdapter(device: Device, program: AggregateProgram) extends Adapter {
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
    val neighbours = Support.INSTANCE.getDevices.getNeighbours(device, true).asScala
    val nbrData = neighbours.flatMap(d => d.selfStatus().asScala)
    val exports = nbrData
      .filter(m => m.component2() == MessageType.Result && m.component3().isInstanceOf[EXPORT])
      .map(m => m.component1() -> m.component3().asInstanceOf[EXPORT])
      .toMap

    val sensors = obtainSensorValues(defaultSensors) ++ obtainSensorValues(customSensors.toMap)

    val result = program(factory.context(device.getId, exports, sensors))

    device.showResult(s"$device: " + result.root())

    //tell my export to my neighbours
    //for (d <- Support.INSTANCE.getDevices.getNeighbours(device, true).asScala) {
    //    d.tell(new Message(device.getId, MessageType.Result, result))
    //}
    device.tell(new Message(device.getId, MessageType.Result, result))
  }

  override def getDevice: Device = device
}
