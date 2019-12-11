package incarnations.protelis

import devices.Device
import incarnations.Incarnation
import org.protelis.lang.ProtelisLoader
import org.protelis.vm.NetworkManager
import org.protelis.vm.ProtelisVM

class ProtelisIncarnation(device: Device,
                          moduleName: String,
                          networkManagerBuilder: (Device) -> NetworkManager,
                          contextBuilder: (Device, NetworkManager) -> ProtelisContext = ::SimpleProtelisContext) : Incarnation {

    private val vm: ProtelisVM
    private val networkManager: NetworkManager
    val context: ProtelisContext

    init {
        val program = ProtelisLoader.parse(moduleName)
        networkManager = networkManagerBuilder(device)
        context = contextBuilder(device, networkManager)
        vm = ProtelisVM(program, context)
    }

    override fun execute() = vm.runCycle()

    override fun readSensor(name: String): Any {
        return 0
    }
}