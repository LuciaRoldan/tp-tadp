package DragonBall

import Guerrero._


object Movimiento {
  type Movimiento = Contrincantes => Contrincantes
  //type ContrincantesBiologicos = (Biologico, Biologico)

object cargarKi {
  def apply(contrincantes: Contrincantes): Contrincantes = {
    val (atacante, atacado) = contrincantes
    atacante.raza match {
      case sayajin: Sayajin => (atacante.copy(raza = Sayajin(ki = sayajin.ki + sayajin.nivelSS * 150, nivelSS = sayajin.nivelSS)), atacado)
      case _: Androide => contrincantes
      case bio :Biologico => (atacante.copy(raza = bio.cargarKi()), atacado)

      /*case humano : Humano=> (atacante.copy(raza = Humano(ki= humano.ki + 100)), atacado)
      case namekusein : Namekusein=> (atacante.copy(raza = Namekusein(ki= namekusein.ki + 100)), atacado)
      case monstruo : Monstruo=> (atacante.copy(raza = Monstruo(ki= monstruo.ki + 100)), atacado)
      case mono : Mono=> (atacante.copy(raza = Mono(ki= mono.ki + 100, sayajin = mono.sayajin)), atacado)*/
    }
  }
}

  class usarItem(item: Item) extends Movimiento {
    def apply(contrincantes: Contrincantes): Contrincantes = {
      val (atacante, atacado) = contrincantes
      if (atacante.tieneItem(item)) {
        (item, atacado.raza) match {
          case (armaRoma, _:Androide) => (atacante, atacado)
          case (armaRoma, _:Biologico) => if(atacado.raza.ki < 300) {(atacante, acatado.cambiarEstado(Inconsciente))}
          case (armaFilosa, _:Sayajin) => if(atacado.tieneCola) {(atacante, atacado.perderCola().dejarKiEnUno())}
          case (armaFilosa, _:Mono) => (atacante, atacado.getSayajin().perderCola().dejarKiEnUno())
          case (armaFilosa, _:Androide) => (atacante, atacado)
          case (armaFilosa, _:Raza) => (atacante, atacado.disminuirKi(atacante.ki/100))
          case (armaDeFuego, _:Humano) => if(atacante.tieneMunicion) {(atacante.perderMunicion(), atacado.perderKi(20))}
                                      else (atacante, acatado)
          case (armaDeFuego, _:Namekusein) => if(atacante.tieneMunicion) {(atacante.perderMunicion(), atacado.perderKi(10))}
                                            else (atacante, acatado)
        }
      }
    }
  }

}