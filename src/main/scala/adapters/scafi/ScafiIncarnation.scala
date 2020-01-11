package adapters.scafi

import it.unibo.scafi.incarnations.BasicAbstractIncarnation

object ScafiIncarnation extends BasicAbstractIncarnation {

  def emptyContext(id: ID): ScafiIncarnation.CONTEXT = ScafiIncarnation.factory.context(id, Map())
}
