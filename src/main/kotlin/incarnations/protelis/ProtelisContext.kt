package incarnations.protelis

import com.google.common.hash.Hashing
import org.protelis.lang.datatype.DeviceUID
import org.protelis.vm.NetworkManager
import org.protelis.vm.impl.AbstractExecutionContext
import org.protelis.vm.impl.HashingCodePathFactory
import org.protelis.vm.impl.SimpleExecutionEnvironment

abstract class ProtelisContext(private val deviceID: IntUID, networkManager: NetworkManager) :
    AbstractExecutionContext<ProtelisContext>(
        SimpleExecutionEnvironment(), networkManager,  HashingCodePathFactory(Hashing.sha256())) {

    override fun nextRandomDouble(): Double = Math.random()

    override fun getDeviceUID(): DeviceUID = deviceID

    override fun getCurrentTime(): Number =  System.currentTimeMillis()
}