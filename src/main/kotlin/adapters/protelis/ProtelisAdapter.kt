package adapters.protelis

import devices.interfaces.Device
import adapters.Adapter
import org.protelis.lang.ProtelisLoader
import org.protelis.vm.NetworkManager
import org.protelis.vm.ProtelisVM
import server.Support

class ProtelisAdapter(override val device: Device,
                      moduleName: String,
                      contextBuilder: (Device, NetworkManager) -> ProtelisContext = ::SimpleProtelisContext,
                      server: Device = Support) : Adapter {

    private val vm: ProtelisVM
    private val networkManager: NetworkManager
    val context: ProtelisContext

    init {
        val program = ProtelisLoader.parse(moduleName)
        networkManager = ProtelisNetworkManager(device, server)
        context = contextBuilder(device, networkManager)
        vm = ProtelisVM(program, context)
    }

    override fun execute() = vm.runCycle()
}