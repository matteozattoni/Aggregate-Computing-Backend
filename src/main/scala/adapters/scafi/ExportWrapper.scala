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
        //read slot by slot
        val slotType = inputStream.readUTF()

        val nbrType = classOf[Nbr[_]].toString
        val repType = classOf[Rep[_]].toString
        val scopeType = classOf[Scope[_]].toString
        val foldType = classOf[FoldHood[_]].toString
        val funType = classOf[FunCall[_]].toString

        slotType match {
          case `nbrType` =>
            val index = inputStream.readInt()
            slots += Nbr[Int](index)
          case `repType` =>
            val index = inputStream.readInt()
            slots += Rep[Int](index)
          case `scopeType` =>
            val key = inputStream.readObject()
            slots += Scope(key)
          case `foldType` =>
            val index = inputStream.readInt()
            slots += FoldHood[Int](index)
          case `funType` =>
            val index = inputStream.readInt()
            val funId = inputStream.readObject()
            slots += FunCall[Int](index, funId)
        }
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
        outputStream.writeUTF(slot.getClass.toString)
        slot match {
          case nbr: Nbr[_] => outputStream.writeInt(nbr.index)
          case rep: Rep[_] => outputStream.writeInt(rep.index)
          case scope: Scope[_] => outputStream.writeObject(scope.key)
          case fold: FoldHood[_] => outputStream.writeInt(fold.index)
          case fun: FunCall[_] =>
            outputStream.writeInt(fun.index)
            outputStream.writeObject(fun.funId)
        }
      }
      //write this path value
      outputStream.writeObject(value)
    }
  }
}
