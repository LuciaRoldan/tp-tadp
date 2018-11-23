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
      case (atacante: Humano, atacado: Androide) => (atacante.cambiarVida(atacante.getVida - 10), atacado)
      case (atacante, atacado) => if (atacado.getVida < atacante.getVida){(atacante, atacado.cambiarVida(atacado.getVida - 20))}
                                  else {(atacante.cambiarVida(atacante.getVida - 10 ), atacado)}
    }
  }
}

class Explotar extends AtaqueFisico{
  def ejecutar(unAtacante :Guerrero, unAtacado :Guerrero): Contrincantes ={
    (unAtacante, unAtacado)match {
      case (atacante: Monstruo, atacado: Namekusein) => (atacante.cambiarVida(0), atacado.cambiarVida(math.max(atacado.getVida - atacante.getVida * 2, 1)))
    case (atacante: Monstruo, atacado) => (atacante.cambiarVida(0), atacado.cambiarVida(atacado.getVida - atacante.getVida * 2))
    case (atacante: Androide, atacado: Namekusein) => (atacante.cambiarVida(0), atacado.cambiarVida(math.max(atacado.getVida - atacante.getVida * 3, 1)))
    case (atacante: Androide, atacado) => (atacante.cambiarVida(0), atacado.cambiarVida(atacado.getVida - atacante.getVida * 3))
      case (_ ,_) => throw new NoPuedeHacerEseAtaqueException
    }
  }
}

case class Onda(energia: Int) extends AtaqueDeEnergia{
  def ejecutar(unAtacante :Guerrero, unAtacado :Guerrero): Contrincantes ={
    (unAtacante, unAtacado)match {
      case (atacante, atacado) if atacante.getVida() < energia =>  (atacante, atacado)
      case (atacante, atacado: Monstruo) => (atacante.cambiarVida(atacante.getVida - energia), atacado.cambiarVida(atacado.getVida - energia / 2))
      case (atacante, atacado: Androide) => (atacante.cambiarVida(atacante.getVida - energia), atacado.cambiarVida(atacado.getVida + energia * 2))
      case (atacante, atacado) => (atacante.cambiarVida(atacante.getVida - energia), atacado.cambiarVida(atacado.getVida - energia * 2))
    }
  }
}

class Genkidama extends AtaqueDeEnergia{
  def ejecutar(unAtacante :Guerrero, unAtacado :Guerrero): Contrincantes ={
    (unAtacante, unAtacado)match {
      case (atacante, atacado: Androide) => (atacante, atacado.cambiarVida(atacado.getVida + scala.math.pow(10, atacante.roundsFajado).toInt))
      case (atacante, atacado) => (atacante, atacado.cambiarVida(atacado.getVida - scala.math.pow(10, atacante.roundsFajado).toInt))
    }
  }
}