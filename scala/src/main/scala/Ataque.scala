package DragonBall

import DragonBall.Movimiento._

abstract class Ataque{
  def ejecutar(unAtacante :Guerrero, unAtacado :Guerrero): Contrincantes
}

abstract class AtaqueFisico extends Ataque
abstract class AtaqueDeEnergia extends Ataque


class MuchosGolpesNinja extends AtaqueFisico{
  def ejecutar(unAtacante :Guerrero, unAtacado :Guerrero): Contrincantes ={
    (unAtacante, unAtacado)match {
      case (atacante: Humano, atacado: Androide) => (atacante.recibirAtaque(this, 10), atacado)
      case (atacante, atacado) => if (atacado.getVida < atacante.getVida){(atacante, atacado.recibirAtaque(this, 20))}
                                  else {(atacante.recibirAtaque(this, 20), atacado)}
    }
  }
}

class Explotar extends AtaqueFisico{
  def ejecutar(unAtacante :Guerrero, unAtacado :Guerrero): Contrincantes ={
    (unAtacante, unAtacado) match {
    case (atacante: Androide, atacado) => (atacante.cambiarVida(0), atacado.recibirAtaque(this, atacante.getVida * 3))
    case (atacante, atacado) => (atacante.cambiarVida(0), atacado.recibirAtaque(this, atacante.getVida * 2))

    }
  }
}

case class Onda(energia: Int) extends AtaqueDeEnergia{
  def ejecutar(unAtacante :Guerrero, unAtacado :Guerrero): Contrincantes ={
    (unAtacante, unAtacado) match {
      case (atacante, atacado) if atacante.getVida() < energia =>  (atacante, atacado)
      case (atacante, atacado) => (atacante.cambiarVida(atacante.getVida - energia), atacado.recibirAtaque(this, energia * 2))
    }
  }
}

class Genkidama extends AtaqueDeEnergia{
  def ejecutar(unAtacante :Guerrero, unAtacado :Guerrero): Contrincantes ={
    (unAtacante, unAtacado.recibirAtaque(this, scala.math.pow(10, unAtacante.roundsFajado).toInt))
  }
}