package adapters

import adapters.scafi.{ScafiAdapter, ScafiIncarnation}
import devices.VirtualDevice
import server.{Support, Topology}

class ScafiAdapterTest {
  class BasicUsageProgram extends ScafiIncarnation.AggregateProgram {
    //override def main(): Any = foldhood(0)(_+_){1} - 1
    //override def main(): Any = rep(0){_+1}
    //override def main(): Any = mid

    val empty: List[Int] = List.empty
    def isMe: Boolean = nbr[Int](mid())==mid()
    override def main(): Any = foldhood(empty)(_++_){ mux(isMe){empty}{ List(nbr(mid())) } }
  }
  //useful to not make adapter tests interact with each other
  Support.INSTANCE.getDevices.reset()

  val device1 = new VirtualDevice(1)
  device1.setAdapter(ScafiAdapter(device1, new BasicUsageProgram()))

  val device2 = new VirtualDevice(2)
  device2.setAdapter(ScafiAdapter(device2, new BasicUsageProgram()))

  val device3 = new VirtualDevice(3)
  device3.setAdapter(ScafiAdapter(device3, new BasicUsageProgram()))

  Support.INSTANCE.getDevices.plusAssign(device1)
  Support.INSTANCE.getDevices.plusAssign(device2)
  Support.INSTANCE.getDevices.plusAssign(device3)
  Support.INSTANCE.getDevices.finalize(Topology.Line)

  @org.junit.jupiter.api.Test
  def executeCycles() {
    for(_ <- 1 to 5) {
      Support.INSTANCE.execute()
      println("------------")
    }
  }
}
