package server

import adapters.Adapter
import adapters.DummyAdapter
import devices.interfaces.EmulatedDevice

/**
 * Things that need to be known Server-wide regarding the current experiment
 */
internal object Execution {
    /**
     * The adapter to be used when needed
     */
    var adapter: (EmulatedDevice) -> Adapter = ::DummyAdapter
}