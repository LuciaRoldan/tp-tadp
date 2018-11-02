package DragonBall

import DragonBall.Guerrero.Guerrero
import Movimiento._

trait Ki{
  val ki: Int

}

object Guerrero{
  type Contrincantes = (Guerrero, Guerrero)
  type PlanDeAtaque = List[Movimiento]

   class Guerrero (val ki: Int, val estado: Estado, val nombre: String, val inventario: List[Item]){
    def tieneItem(item: Item): Boolean = ???

    def contraatacar(enemigo: Guerrero): Contrincantes = ???

    def copy(nuevoKi :Int = ki, nuevoEstado :Estado = estado, nuevoNombre :String = nombre, nuevoInventario :List[Item] = inventario) :Guerrero ={
      new Guerrero(ki= nuevoKi, estado= nuevoEstado, nombre= nuevoNombre, inventario= nuevoInventario)
    }

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

  //case class Biologico(estado: Estado, ki: Int, nombre: String, inventario: List[Item]) extends Guerrero{
  //  def disminuirKi(cantidad: Int):Biologico = { this.copy(ki= this.ki - cantidad)}

  //  def cambiarKi(cantidad: Int):Biologico = { this.copy(ki= cantidad)}
  //}

  case class Androide(override val estado: Estado, override val ki: Int, override val nombre: String, override val inventario: List[Item], bateria: Int) extends Guerrero(ki :Int,estado :Estado, nombre: String, inventario: List[Item])

  case class Sayajin(estado: Estado, ki: Int, nombre: String, inventario: List[Item], nivelSS: Int) extends Guerrero with Ki

  case class Humano(estado: Estado, ki: Int, nombre: String, inventario: List[Item]) extends Guerrero with Ki

  case class Fusionado (estado: Estado, ki: Int, nombre: String, inventario: List[Item], guerreroOriginal: Guerrero) extends Guerrero with Ki

  case class Namekusein (estado: Estado, ki: Int, nombre: String, inventario: List[Item]) extends Guerrero with Ki

  case class Monstruo (estado: Estado, ki: Int, nombre: String, inventario: List[Item]) extends Guerrero with Ki

  case class Mono (estado: Estado, ki: Int, nombre: String, inventario: List[Item], sayayin: Sayajin) extends Guerrero with Ki


}

