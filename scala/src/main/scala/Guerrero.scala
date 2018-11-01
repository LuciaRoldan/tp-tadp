import Movimientos._
import Item._

object Guerrero{
  type Contrincantes = (Guerrero, Guerrero)
  type PlanDeAtaque = List[Movimiento]

  abstract class Guerrero {

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
        case(_,Muerto) => (contrincantes)
        case(_,Inconsciente) => (contrincantes)
        case(_,_) => movimiento(contrincantes)
      }
    }

  }

  case abstract class Biologico(estado: Estado, ki: Int, nombre: String, inventario: List[Item]) extends Guerrero{
    def disminuirKi(cantidad: Int):Biologico = { this.copy(ki= this.ki - cantidad)}

    def cambiarKi(cantidad: Int):Biologico = { this.copy(ki= cantidad)}
  }

  case class Androide(estado: Estado, nombre: String, inventario: List[Item], bateria: Int) extends Guerrero

  case class Sayajin(estado: Estado, ki: Int, nombre: String, inventario: List[Item], nivelSS: Int) extends Biologico(estado: Estado, ki: Int, nombre: String, inventario: List[Item])

  case class Humano(estado: Estado, ki: Int, nombre: String, inventario: List[Item]) extends Biologico(estado: Estado, ki: Int, nombre: String, inventario: List[Item])

  case class Fusionado (estado: Estado, ki: Int, nombre: String, inventario: List[Item], guerreroOriginal: Guerrero) extends Biologico(estado: Estado, ki: Int, nombre: String, inventario: List[Item])

  case class Namekusein (estado: Estado, ki: Int, nombre: String, inventario: List[Item]) extends Biologico(estado: Estado, ki: Int, nombre: String, inventario: List[Item])

  case class Monstruo (estado: Estado, ki: Int, nombre: String, inventario: List[Item]) extends Biologico(estado: Estado, ki: Int, nombre: String, inventario: List[Item])

  case class Mono (estado: Estado, ki: Int, nombre: String, inventario: List[Item], sayayin: Sayajin) extends Biologico(estado: Estado, ki: Int, nombre: String, inventario: List[Item])


}

