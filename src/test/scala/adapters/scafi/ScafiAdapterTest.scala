package adapters.scafi

import devices.DeviceFactory
import server.{ScalaExecution, ScalaSupport, Support, Topology}

case class BasicUsageProgram() extends ScafiIncarnation.AggregateProgram {
  //override def main(): Any = foldhood(0)(_+_){1} - 1
  //override def main(): Any = rep(0){_+1}
  //override def main(): Any = mid
  //override def main(): Any = sense[Int]("randomGenerator")
  //override def main(): Any = sense[Boolean]("customSensor")

  val empty: List[Int] = List.empty
  def isMe: Boolean = nbr[Int](mid())==mid()
  override def main(): Any = foldhood(empty)(_++_){ mux(isMe){empty}{ List(nbr(mid())) } }
}

class ScafiAdapterTest {
  ScalaExecution.setAdapter(ScafiAdapter(_, BasicUsageProgram()))

  //useful to not make adapter tests interact with each other
  ScalaSupport.devices.reset()

  for (_ <- 1 to 3) {
    val device = ScalaSupport.createAndAddDevice(DeviceFactory.virtual(_))
    device.getAdapter.asInstanceOf[ScafiAdapter].customSensors.put("customSensor", _.getId % 2 == 0)
  }

  ScalaSupport.devices.finalize(Topology.Line)

  @org.junit.jupiter.api.Test
  def executeCycles() {
    for(_ <- 1 to 5) {
      Support.INSTANCE.execute()
      println("------------")
    }
  }
}
