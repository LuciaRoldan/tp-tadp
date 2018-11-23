package DragonBall

import DragonBall.Movimiento.Contrincantes


class ResultadoPelea(contrincantes :Contrincantes){
  def hayGanador(): Boolean = {
    return contrincantes._1.estaMuerto() ^ contrincantes._2.estaMuerto() //ese es un o exclusivo re loco
  }

  def getGanador(): Option[Guerrero] = {
    (contrincantes._1.estado, contrincantes._2.estado) match{
      case (Muerto, Muerto) => return None
      case (Muerto,_) => return Some(contrincantes._2)
      case (_,Muerto) => return Some(contrincantes._1)
      case (_,_) => return None
    }
  }

  def getAtacante():Guerrero =  contrincantes._1
  def getAtacado():Guerrero = contrincantes._2
}
