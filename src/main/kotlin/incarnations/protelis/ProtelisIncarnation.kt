package incarnations.protelis

import incarnations.Incarnation
import org.protelis.lang.ProtelisLoader
import org.protelis.vm.NetworkManager
import org.protelis.vm.ProtelisVM

class ProtelisIncarnation(deviceID: Int, moduleName: String) : Incarnation {

    private val vm: ProtelisVM
    val networkManager: ProtelisNetworkManager
    val context: ProtelisContext

    init {
        val program = ProtelisLoader.parse(moduleName)
        networkManager = ProtelisNetworkManager(IntUID(deviceID))
        context = ProtelisContext(deviceID, networkManager)
        vm = ProtelisVM(program, context)
    }

    override fun execute() = vm.runCycle()

    override fun readSensor(name: String): Any {
        TODO("not implemented")
    }
}