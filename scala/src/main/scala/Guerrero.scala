import Movimiento._


object Guerrero{
  type Contrincantes = (Guerrero, Guerrero)
  type PlanDeAtaque = List[Movimiento]

  abstract class Guerrero {
    def contraatacar(enemigo: Guerrero): Contrincantes = ???


    var estado: Estado
    var nombre: String
    var inventario: List[Item]

    def pelearContra(oponente: Guerrero)(plan: PlanDeAtaque): Unit ={
      plan.foldLeft((this, oponente)) {
        case((atacante, atacado), movimiento) =>
          atacante.pelearRound(movimiento, atacado)
      }
    }

    def pelearRound(movimiento: Movimiento, oponente: Guerrero):Contrincantes ={
      var(yo, oponenteModificado) = this.hacerMovimiento(movimiento, (this, oponente))
      oponenteModificado.contraatacar(yo)
    }

    def hacerMovimiento(movimiento: Movimiento, contrincantes: Contrincantes): Contrincantes ={
      (movimiento, contrincantes._1.estado) match{
        case(comerSemilla,_) => movimiento(contrincantes)
        case(_,Muerto) => contrincantes
        case(_,Inconsciente) => contrincantes
        case(_,_) => movimiento(contrincantes)
      }
    }

  }

  case class Biologico(estado: Estado, ki: Int, nombre: String, inventario: List[Item]) extends Guerrero{
    def disminuirKi(cantidad: Int):Biologico = { this.copy(ki= this.ki - cantidad)}

    def cambiarKi(cantidad: Int):Biologico = { this.copy(ki= cantidad)}
  }

  case class Androide(estado: Estado, nombre: String, inventario: List[Item], bateria: Int) extends Guerrero

  case class Sayajin(override val estado: Estado, override val ki: Int, override val nombre: String, override val inventario: List[Item], nivelSS: Int) extends Biologico(estado: Estado, ki: Int, nombre: String, inventario: List[Item])

  case class Humano(override val estado: Estado, override val ki: Int, override val nombre: String, override val inventario: List[Item]) extends Biologico(estado: Estado, ki: Int, nombre: String, inventario: List[Item])

  case class Fusionado (override val estado: Estado, override val ki: Int, override val nombre: String, override val inventario: List[Item], guerreroOriginal: Guerrero) extends Biologico(estado: Estado, ki: Int, nombre: String, inventario: List[Item])

  case class Namekusein (override val estado: Estado, override val ki: Int, override val nombre: String, override val inventario: List[Item]) extends Biologico(estado: Estado, ki: Int, nombre: String, inventario: List[Item])

  case class Monstruo (override val estado: Estado, override val ki: Int, override val nombre: String, override val inventario: List[Item]) extends Biologico(estado: Estado, ki: Int, nombre: String, inventario: List[Item])

  case class Mono (override val estado: Estado, override val ki: Int, override val nombre: String, override val inventario: List[Item], sayayin: Sayajin) extends Biologico(estado: Estado, ki: Int, nombre: String, inventario: List[Item])


}

