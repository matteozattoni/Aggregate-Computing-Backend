package utils

import kotlin.jvm.functions.Function1

object ToKotlin {
  /**
   * Creates kotlin functions from Scala ones
   */

  def toKotlinFun[A,B](builder: (A) => B): Function1[A,B] =
    new Function1[A,B] {
      override def invoke(p1: A): B = builder(p1)
    }
}
