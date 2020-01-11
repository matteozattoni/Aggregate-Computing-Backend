package adapters.scafi

import adapters.Adapter
import devices.Device
import ScafiIncarnation._

case class ScafiAdapter(device: Device, program: AggregateProgram) extends Adapter {

    private var context: CONTEXT = emptyContext(device.getId)

    override def execute() {
        val result = program(context)
        println(result)
        context = factory.context(device.getId, Map(device.getId -> result))
    }

    override def getDevice: Device = device

    override def readSensor(name: String): AnyRef = 0.asInstanceOf[AnyRef]
}
