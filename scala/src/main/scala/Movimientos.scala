package DragonBall


object Movimiento {

  type Criterio = Contrincantes => Int

  class NoTieneItemException extends Exception

  type Contrincantes = (Guerrero, Guerrero)
  type PlanDeAtaque = List[Movimiento]
  type Movimiento = Contrincantes => Contrincantes
  //type ContrincantesBiologicos = (Biologico, Biologico)

  val diferenciaKiAtacante = (contAntes: Contrincantes, contDespues: Contrincantes) => {
    val (atacanteAntes, atacanteDespues) = (contAntes._1, contDespues._1)
    val kiDespues = (atacanteAntes, atacanteDespues) match {
      case _: (Androide, Androide) => 0
      case guerreros: (Biologico, Biologico) => guerreros._2.ki - guerreros._1.ki
    }
  }

  object cargarKi {
    def apply(contrincantes: Contrincantes): Contrincantes = {
      val (atacante, atacado) = contrincantes
      atacante match {
        case atacante: Sayajin => (atacante.cambiarKi(atacante.ki + atacante.nivelSS * 150), atacado)
        case _: Androide => contrincantes
        case atacante: Biologico => (atacante.cambiarKi(atacante.ki + 100), atacado)
      }
    }
  }

  class usarItem(item: Item) extends Movimiento {
    def apply(contrincantes: Contrincantes): Contrincantes = {
      val (atacante, atacado) = contrincantes
      if (atacante.tieneItem(item)) {
        (item, atacante, atacado) match {
          case (ArmaRoma, _ :Androide, _) => (atacante, atacado)
          case (ArmaRoma, _, atacado :Biologico) if atacado.ki < 300 =>(atacante, atacado.cambiarEstado(Inconsciente))
          case (ArmaRoma, _, _) => (atacante, atacado)
          case (ArmaFilosa, _, atacado :Sayajin) if atacado.tieneCola =>  (atacante, atacado.perderCola.cambiarKi(1))
          case (ArmaFilosa, _, atacado :Mono) => (atacante, atacado.getSayajin.perderCola.cambiarKi(1))
          case (ArmaFilosa, _, _ :Androide) => (atacante, atacado)
          case (ArmaFilosa, atacante :Biologico, atacado :Biologico) => (atacante, atacado.cambiarKi(atacado.ki - atacante.ki/100))
          case (ArmaDeFuego, atacante :Biologico, atacado :Humano) => if(atacante.tieneMunicion) {(atacante.perderMunicion(), atacado.cambiarKi(atacado.ki - 20))}
                                      else (atacante, atacado)
          case (ArmaDeFuego, atacante, atacado :Namekusein) => if(atacante.tieneMunicion) {(atacante.perderMunicion(), atacado.cambiarKi(atacado.ki - 10))}
                                            else (atacante, atacado)
        }
      }
      else throw new NoTieneItemException
    }
  }

}




