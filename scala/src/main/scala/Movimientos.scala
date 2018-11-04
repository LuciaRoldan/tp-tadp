package DragonBall
import DragonBall.Guerrero


object Movimiento {

  class NoTieneItemException extends Exception
  type Contrincantes = (Guerrero, Guerrero)
  type PlanDeAtaque = List[Movimiento]
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
        (item, atacante, atacado) match {
          case (armaRoma, _ :Androide, _) => (atacante, atacado)
          case (armaRoma, _, atacado :Biologico) if atacado.ki < 300 =>(atacante, atacado.cambiarEstado(Inconsciente))
          case (armaRoma, _, _) => (atacante, atacado)
          case (armaFilosa, _, atacado :Sayajin) if atacado.tieneCola =>  (atacante, atacado.perderCola.cambiarKi(1))
          case (armaFilosa, _, atacado :Mono) => (atacante, atacado.getSayajin.perderCola.cambiarKi(1))
          case (armaFilosa, _, _ :Androide) => (atacante, atacado)
          case (armaFilosa, atacante :Biologico, atacado :Biologico) => (atacante, atacado.cambiarKi(atacado.ki - atacante.ki/100))
          case (armaDeFuego, atacante :Biologico, atacado :Humano) => if(atacante.tieneMunicion) {(atacante.perderMunicion(), atacado.cambiarKi(atacado.ki - 20))}
                                      else (atacante, atacado)
          case (armaDeFuego, atacante, atacado :Namekusein) => if(atacante.tieneMunicion) {(atacante.perderMunicion(), atacado.cambiarKi(atacado.ki - 10))}
                                            else (atacante, atacado)
        }
      }
      else throw new NoTieneItemException
    }
  }

}




