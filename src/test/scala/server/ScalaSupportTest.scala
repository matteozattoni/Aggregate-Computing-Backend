package server

import devices.implementations.DummyDevice
import org.junit.jupiter.api.Assertions._

class ScalaSupportTest {
  //useful to not make tests interact with each other
  ScalaSupport.devices.reset()

  for (_ <- 1 to 5)
    ScalaSupport.createAndAddDevice(new DummyDevice(_))

  ScalaSupport.devices.finalize(Topology.Line)

  @org.junit.jupiter.api.Test
  def executeCycles(): Unit = {
    assertEquals(ScalaSupport.devices.getDevices.size(), 5)
    assertEquals(ScalaSupport.devices.getNeighbours(0, false).size(), 1)
    assertEquals(ScalaSupport.devices.getNeighbours(1, false).size(), 2)

  }
}