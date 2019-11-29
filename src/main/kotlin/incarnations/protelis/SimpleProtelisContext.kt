package incarnations.protelis

import org.protelis.vm.NetworkManager

class SimpleProtelisContext(private val id: IntUID, networkManager: NetworkManager) : ProtelisContext(id, networkManager) {
    override fun instance(): ProtelisContext = SimpleProtelisContext(id, networkManager)
}