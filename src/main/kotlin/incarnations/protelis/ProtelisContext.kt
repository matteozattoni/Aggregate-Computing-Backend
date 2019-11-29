package incarnations.protelis

import com.google.common.hash.Hashing
import org.protelis.lang.datatype.DeviceUID
import org.protelis.vm.NetworkManager
import org.protelis.vm.impl.AbstractExecutionContext
import org.protelis.vm.impl.HashingCodePathFactory
import org.protelis.vm.impl.SimpleExecutionEnvironment

class ProtelisContext(private val deviceID: Int, networkManager: NetworkManager) : AbstractExecutionContext<ProtelisContext>(
        SimpleExecutionEnvironment(), networkManager,  HashingCodePathFactory(Hashing.sha256())) {

    fun announce(something: String) = println("$deviceID - $something")

    override fun nextRandomDouble(): Double = Math.random()

    override fun getDeviceUID(): DeviceUID = IntUID(deviceID)

    override fun getCurrentTime(): Number =  System.currentTimeMillis()

    override fun instance(): ProtelisContext = ProtelisContext(deviceID, networkManager)
}