package devices

import adapters.Adapter
import communication.Message

/**
 * Emulated Device model, it needs an Adapter to be able to execute
 */
abstract class EmulatedDevice(id: Int, name: String, adapterBuilder: (EmulatedDevice) -> Adapter): AbstractDevice(id, name) {
    @Suppress("LeakingThis")
    var adapter: Adapter = adapterBuilder(this)

    override fun execute() = adapter.execute()

    override fun showResult(result: String) {
        println(result)
    }
}