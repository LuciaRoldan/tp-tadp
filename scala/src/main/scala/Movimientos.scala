package DragonBall

import Guerrero._


object Movimiento {
  type Movimiento = Contrincantes => Contrincantes
  //type ContrincantesBiologicos = (Biologico, Biologico)

object cargarKi {
  def apply(contrincantes: Contrincantes): Contrincantes = {
    val (atacante, atacado) = contrincantes
    atacante match {
      case atacante: Sayajin => (atacante.cambiarKi(atacante.ki + atacante.nivelSS * 150),atacado)
      case _: Androide => contrincantes
      case atacante: Biologico => (atacante.cambiarKi(atacante.ki + 100), atacado)
    }
  }
}

  class usarItem(item: Item) extends Movimiento {
    def apply(contrincantes: Contrincantes): Contrincantes = {
      val (atacante, atacado) = contrincantes
      if (atacante.tieneItem(item)) {
        (item, atacado) match {
          case (armaRoma, Androide) => (atacante, atacado)
          case (armaRoma, _) => if(atacado.ki < 300) {(atacante, acatado.cambiarEstado(Inconsciente))}
          case (armaFilosa, Sayajin) => if(atacado.tieneCola) {(atacante, atacado.perderCola().dejarKiEnUno())}
          case (armaFilosa, MonoGigante) => (atacante, atacado.getSayajin().perderCola().dejarKiEnUno())
          case (armaFilosa, Androide) => (atacante, atacado)
          case (armaFilosa, Biologico) => (atacante, atacado.disminuirKi(atacante.ki/100))
          case (armaDeFuego, Humo) => if(atacante.tieneMunicion) {(atacante.perderMunicion(), atacado.perderKi(20))}
                                      else (atacante, acatado)
          case (armaDeFuego, Namekusein) => if(atacante.tieneMunicion) {(atacante.perderMunicion(), atacado.perderKi(10))}
                                            else (atacante, acatado)
        }
      }
    }
  }

}




