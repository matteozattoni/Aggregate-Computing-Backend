package incarnations.protelis

import incarnations.Incarnation
import org.protelis.lang.ProtelisLoader
import org.protelis.vm.NetworkManager
import org.protelis.vm.ProtelisVM

class ProtelisIncarnation(deviceID: IntUID,
                          moduleName: String,
                          networkManagerBuilder: (IntUID) -> NetworkManager,
                          contextBuilder: (IntUID, NetworkManager) -> ProtelisContext = ::SimpleProtelisContext) : Incarnation {

    private val vm: ProtelisVM
    val networkManager: NetworkManager
    val context: ProtelisContext

    init {
        val program = ProtelisLoader.parse(moduleName)
        networkManager = networkManagerBuilder(deviceID)
        context = contextBuilder(deviceID, networkManager)
        vm = ProtelisVM(program, context)
    }

    override fun execute() = vm.runCycle()

    override fun readSensor(name: String): Any {
        return 0
    }
}