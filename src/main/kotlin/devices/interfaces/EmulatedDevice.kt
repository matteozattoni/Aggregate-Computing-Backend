package devices.interfaces

import adapters.Adapter
import org.protelis.lang.datatype.DeviceUID
import java.io.Serializable

/**
 * Emulated Device model, it needs an Adapter to be able to execute
 */
abstract class EmulatedDevice(id: DeviceUID, name: String,
                              adapterBuilder: (EmulatedDevice) -> Adapter,
                              onResult: (Serializable) -> Any): AbstractDevice(id, name, onResult) {
    @Suppress("LeakingThis")
    var adapter: Adapter = adapterBuilder(this)

    override fun execute() = adapter.execute()
}