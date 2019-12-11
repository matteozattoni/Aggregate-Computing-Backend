package adapters.protelis

import devices.Device
import org.protelis.vm.NetworkManager

class SimpleProtelisContext(private val device: Device, networkManager: NetworkManager) : ProtelisContext(device, networkManager) {
    override fun instance(): ProtelisContext = SimpleProtelisContext(device, networkManager)
}