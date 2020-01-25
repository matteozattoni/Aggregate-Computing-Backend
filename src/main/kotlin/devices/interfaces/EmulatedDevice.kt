package devices.interfaces

import adapters.Adapter

/**
 * Emulated Device model, it needs an Adapter to be able to execute
 */
abstract class EmulatedDevice(id: Int, name: String,
                              adapterBuilder: (EmulatedDevice) -> Adapter,
                              onResult: (String) -> Any): AbstractDevice(id, name, onResult) {
    @Suppress("LeakingThis")
    var adapter: Adapter = adapterBuilder(this)

    override fun execute() = adapter.execute()
}