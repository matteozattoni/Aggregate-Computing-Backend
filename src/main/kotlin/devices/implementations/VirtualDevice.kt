package devices.implementations

import adapters.Adapter
import devices.interfaces.EmulatedDevice
import org.protelis.lang.datatype.DeviceUID
import server.Execution
import java.io.Serializable

/**
 * Fully emulated Device
 */
class VirtualDevice(id: DeviceUID, name: String,
                    adapterBuilder: (EmulatedDevice) -> Adapter = Execution.adapter,
                    onResult: (Serializable) -> Any = ::println) : EmulatedDevice(id, name, adapterBuilder, onResult) {

    /**
     * Useful to speed up the creation of devices using the :: notation
     */
    constructor(id: DeviceUID) : this(id, "", Execution.adapter)
}