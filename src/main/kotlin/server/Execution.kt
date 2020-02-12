package server

import adapters.Adapter
import adapters.DummyAdapter
import devices.interfaces.EmulatedDevice

object Execution {
    var adapter: (EmulatedDevice) -> Adapter = ::DummyAdapter
}