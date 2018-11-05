package DragonBall


import Movimiento._


   class Guerrero ( val estado: Estado, val nombre: String, val inventario: List[Item]){
     def perderMunicion():Guerrero = ???

     def tieneMunicion: Boolean = ???

     type Contrincantes = (Guerrero, Guerrero)
     type PlanDeAtaque = List[Movimiento]

     def cambiarEstado(nuevoEstado: Estado):Guerrero = ???

     def tieneItem(item: Item): Boolean = ???

    def contraatacar(enemigo: Guerrero): Contrincantes = ???

    /*def copy(nuevoKi :Int = ki, nuevoEstado :Estado = estado, nuevoNombre :String = nombre, nuevoInventario :List[Item] = inventario) :Guerrero ={
      new Guerrero(ki= nuevoKi, estado= nuevoEstado, nombre= nuevoNombre, inventario= nuevoInventario)
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

  class Biologico(val ki:Int, estado: Estado, nombre: String, inventario: List[Item]) extends Guerrero(estado :Estado, nombre: String, inventario: List[Item]){

    def cambiarKi(cantidad: Int):Biologico = this.copear(cantidad)

    def copear(nuevoKi :Int = ki, nuevoEstado :Estado = estado, nuevoNombre :String = nombre, nuevoInventario :List[Item] = inventario) :Biologico =
      new Biologico(ki = nuevoKi, estado= nuevoEstado, nombre= nuevoNombre, inventario= nuevoInventario)

  }
//case class Biologico (override var ki :Int, override var estado : Estado, override var nombre :String, override var inventario : List[Item]) extends Biologico(ki :Int, estado : Estado, nombre :String, inventario : List[Item])

  case class Androide(override val estado: Estado, override val nombre: String, override val inventario: List[Item], bateria: Int) extends Guerrero(estado :Estado, nombre: String, inventario: List[Item])

  case class Sayajin(override val estado: Estado, override val ki: Int, override val nombre: String, override val inventario: List[Item], nivelSS: Int) extends Biologico(ki: Int, estado :Estado, nombre: String, inventario: List[Item]){

    def perderCola :Sayajin = ???

    def tieneCola: Boolean = ???

    override def cambiarKi(cantidad: Int): Sayajin ={
      this.copy(ki = cantidad)
    }
  }

  case class Humano(override val estado: Estado,  override val ki: Int, override val nombre: String, override val inventario: List[Item], nivelSS: Int) extends Biologico(ki :Int, estado :Estado, nombre: String, inventario: List[Item]){
    override def cambiarKi(cantidad: Int): Humano ={
      this.copy(ki = cantidad)
    }
  }

  case class Fusionado (override val estado: Estado, override val ki: Int, override val nombre: String, override val inventario: List[Item], nivelSS: Int, guerreroOriginal: Guerrero) extends Biologico(ki :Int, estado :Estado, nombre: String, inventario: List[Item]){
   override def cambiarKi(cantidad: Int): Fusionado ={
      this.copy(ki = cantidad)
    }
  }

  case class Namekusein (override val estado: Estado, override val ki: Int, override val nombre: String, override val inventario: List[Item], nivelSS: Int) extends Biologico(ki :Int, estado :Estado, nombre: String, inventario: List[Item]){
    override def cambiarKi(cantidad: Int): Namekusein ={
      this.copy(ki = cantidad)
    }
  }

  case class Monstruo (override val estado: Estado, override val ki: Int, override val nombre: String, override val inventario: List[Item], nivelSS: Int) extends Biologico(ki :Int, estado :Estado, nombre: String, inventario: List[Item]){
    override def cambiarKi(cantidad: Int)  ={
      this.copy(ki = cantidad)
    }
  }

  case class Mono (override val estado: Estado, override val ki: Int, override val nombre: String, override val inventario: List[Item], sayayin: Sayajin) extends Biologico(ki :Int, estado :Estado, nombre: String, inventario: List[Item]){

    def getSayajin :Sayajin = sayayin

    override def cambiarKi(cantidad: Int) ={
      this.copy(ki = cantidad)
    }
  }




