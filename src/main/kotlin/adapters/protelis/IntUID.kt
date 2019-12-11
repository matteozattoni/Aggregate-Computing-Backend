package adapters.protelis

import org.protelis.lang.datatype.DeviceUID

data class IntUID(private val uid: Int) : DeviceUID, Comparable<IntUID> {
    override fun compareTo(other: IntUID) = uid.compareTo(other.uid)
    fun getUID() = uid
}