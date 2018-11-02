package DragonBall

import DragonBall.Guerrero.Guerrero
import Movimiento._

object Guerrero{
  type Contrincantes = (Guerrero, Guerrero)
  type PlanDeAtaque = List[Movimiento]

   case class Guerrero (val raza: Raza, val estado: Estado, val nombre: String, val inventario: List[Item]){

     def cargarKi(): Guerrero = ???

     def tieneMunicion: Boolean = ???

     //no olvidar agregar ki maximo

     def tieneItem(item: Item): Boolean = ???

    def contraatacar(enemigo: Guerrero): Contrincantes = ???

    /*def copy( nuevoEstado :Estado = estado, nuevoNombre :String = nombre, nuevoInventario :List[Item] = inventario) :Guerrero ={
      new Guerrero( estado= nuevoEstado, nombre= nuevoNombre, inventario= nuevoInventario)
    }

     def cargarKi(): Unit ={
       this.raza.aumentarEnergia()
     }*/

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

  /*class Biologico(estado: Estado, ki: Int, nombre: String, inventario: List[Item]) extends Guerrero(estado :Estado, nombre: String, inventario: List[Item]){
    def copy(nuevoKi :Int = ki, nuevoEstado :Estado = estado, nuevoNombre :String = nombre, nuevoInventario :List[Item] = inventario) :Biologico ={
      new Biologico(ki= nuevoKi, estado= nuevoEstado, nombre= nuevoNombre, inventario= nuevoInventario)
    }

    def disminuirKi(cantidad: Int):Biologico = { this.copy()}

    def cambiarKi(cantidad: Int):Biologico = { this.copy(ki= cantidad)}


  }*/

  class Raza

   class Biologico(val ki :Int) extends Raza {
    def cargarKi(): Biologico = this.copy(ki = this.ki+100)
  }


  case class Androide(val bateria: Int) extends Raza{
    def aumentarEnergia ={
    }
  }

  case class Sayajin(val nivelSS: Int, val ki: Int) extends Biologico(ki :Int)

  case class Humano(val ki: Int) extends Biologico(ki :Int)

  case class Fusionado (val ki: Int) extends Biologico(ki :Int)

  case class Namekusein(val ki: Int) extends Biologico(ki :Int)

  case class Monstruo(val ki: Int) extends Biologico(ki :Int)

  case class Mono(val ki: Int, sayajin: Guerrero) extends Biologico(ki :Int)

  /*case class Humano(override val estado: Estado, override val nombre: String, override val inventario: List[Item]) extends Guerrero (estado :Estado, nombre: String, inventario: List[Item])

  case class Fusionado (override val estado: Estado, override val nombre: String, override val inventario: List[Item]) extends Guerrero(estado :Estado, nombre: String, inventario: List[Item])

  case class Namekusein (override val estado: Estado, override val nombre: String, override val inventario: List[Item]) extends Guerrero(estado :Estado, nombre: String, inventario: List[Item])

  case class Monstruo (override val estado: Estado, override val nombre: String, override val inventario: List[Item]) extends Guerrero(estado :Estado, nombre: String, inventario: List[Item])

  case class Mono (override val estado: Estado, override val nombre: String, override val inventario: List[Item]) extends Guerrero(estado :Estado, nombre: String, inventario: List[Item])
*/

}

