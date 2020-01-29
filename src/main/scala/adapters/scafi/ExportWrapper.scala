package adapters.scafi

import ScafiIncarnation._
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import scala.collection.mutable

/**
 * Export Wrapper to provide custom serialization
 * @param export the export to serialize
 */
case class ExportWrapper(var export: EXPORT) extends Serializable {

  @throws[ClassNotFoundException]
  @throws[IOException]
  private def readObject(inputStream: ObjectInputStream): Unit = {
    export = new ExportImpl

    //read how many paths to create
    val pathsNumber = inputStream.readInt()

    for(_ <- 0 until pathsNumber) {
      var slots = mutable.MutableList[Slot]()

      //read the path size
      val pathsSize = inputStream.readInt()

      for(_ <- 0 until pathsSize) {
        //read the slots
        slots += inputStream.readObject().asInstanceOf[Slot]
      }

      //read the path value
      val value = inputStream.readObject()

      //put the path into the export
      export.put(new PathImpl(slots.toList), value)
    }
  }

  @throws[IOException]
  private def writeObject(outputStream: ObjectOutputStream): Unit = {
    //write the number of paths
    outputStream.writeInt(export.getAll.size)

    export.getAll.foreach { case (path : PathImpl, value) =>
      //write the path size
      outputStream.writeInt(path.path.size)

      path.path.foreach { slot =>
        //write slot by slot
        outputStream.writeObject(slot)
      }
      //write this path value
      outputStream.writeObject(value)
    }
  }
}
