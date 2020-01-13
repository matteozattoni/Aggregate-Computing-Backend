package adapters.scafi

import java.util.function.Consumer

import adapters.Adapter
import devices.Device
import ScafiIncarnation._
import communication.{Message, MessageType}
import server.Support

case class ScafiAdapter(device: Device, program: AggregateProgram) extends Adapter {
    override def execute() {
        import collection.JavaConverters._
        val neighbours = Support.INSTANCE.getDevices.getNeighbours(device, true).asScala
        val nbrData = neighbours.flatMap(d => d.selfStatus().asScala)
        val exports = nbrData
          .filter(m => m.component2() == MessageType.Result && m.component3().isInstanceOf[EXPORT])
          .map(m => m.component1() -> m.component3().asInstanceOf[EXPORT])
          .toMap
        //println(s"neighbours of ${device.getId}: ${neighbours.map(_.getId)} - ${nbrData.map(_.component1())}")
        //println("exports: " + exports)
        val result = program(factory.context(device.getId, exports))
        println(s"result of ${device.getId}: "+ result.root())

        //tell my export to my neighbours
        //for (d <- Support.INSTANCE.getDevices.getNeighbours(device, true).asScala) {
        //    d.tell(new Message(device.getId, MessageType.Result, result))
        //}
        device.tell(new Message(device.getId, MessageType.Result, result))
    }

    override def getDevice: Device = device
}
