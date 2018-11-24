package DragonBall

import DragonBall.Movimiento.{Contrincantes, Movimiento}


class ResultadoPelea(contrincantes :Contrincantes){
  def hayGanador(): Boolean = {
    return contrincantes._1.estaMuerto() ^ contrincantes._2.estaMuerto() //ese es un o exclusivo re loco
  }

  def getGanador(): Option[Guerrero] = {
    (contrincantes._1.estado, contrincantes._2.estado) match{
      case (Muerto, Muerto) =>  None
      case (Muerto,_) =>  Some(contrincantes._2)
      case (_,Muerto) =>  Some(contrincantes._1)
      case (_,_) =>  None
    }
  }

  def getAtacante():Guerrero =  contrincantes._1
  def getAtacado():Guerrero = contrincantes._2
}

abstract class Resultado{
  def getGanador() :Option[Guerrero]
  def pelear(movimiento: Movimiento):Resultado
}

class PeleaFinalizada(triunfador: Guerrero) extends Resultado{
  override def getGanador() :Option[Guerrero] = Some(triunfador)

  override def pelear(movimiento: Movimiento): Resultado = this
}

class PeleaEnCurso(contrincantes: Contrincantes) extends Resultado{
  override def getGanador() :Option[Guerrero] = None

  override def pelear(movimiento: Movimiento): Resultado = contrincantes._1.pelearRound2(movimiento, contrincantes._2)
}

class PeleaFinalizadaSinGanador extends Resultado{
  override def getGanador():Option[Guerrero] = None

  override def pelear(movimiento: Movimiento): Resultado = this
}
