package adapters.protelis

import com.google.common.hash.Hashing
import devices.interfaces.Device
import org.protelis.lang.datatype.DeviceUID
import org.protelis.vm.NetworkManager
import org.protelis.vm.impl.AbstractExecutionContext
import org.protelis.vm.impl.HashingCodePathFactory
import org.protelis.vm.impl.SimpleExecutionEnvironment

abstract class ProtelisContext(private val device: Device, networkManager: NetworkManager) :
    AbstractExecutionContext<ProtelisContext>(
        SimpleExecutionEnvironment(), networkManager,  HashingCodePathFactory(Hashing.sha256())) {

    override fun nextRandomDouble(): Double = Math.random()

    override fun getDeviceUID(): DeviceUID = IntUID(device.id)

    override fun getCurrentTime(): Number =  System.currentTimeMillis()
}