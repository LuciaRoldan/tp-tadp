package DragonBall

import DragonBall.FormaDeComer.FormaDeComer
import Movimiento._

trait Fusionable extends Biologico{
  def fusionar(atacante: Fusionable, amigo: Fusionable) =
    new Fusionado(estado = Normal, ki = atacante.ki + amigo.ki, kiMaximo = atacante.kiMaximo + amigo.kiMaximo, "FUSION",
      inventario = atacante.inventario ++ amigo.inventario, guerreroOriginal = atacante, listaDeMovimientos = atacante.listaDeMovimientos ++ amigo.listaDeMovimientos, roundsFajado = atacante.roundsFajado + amigo.roundsFajado)
}

abstract class Guerrero(val estado: Estado, val nombre: String, val inventario: List[Item], val listaDeMovimientos: PlanDeAtaque, val roundsFajado :Int) {

  def copear(nuevoEstado :Estado = estado, nuevoNombre :String = nombre, nuevoInventario :List[Item] = inventario, nuevosRoundsFajado :Int = roundsFajado) :Guerrero
  def recibirAtaque(ataque: Ataque, danio: Int): Guerrero

  def dejarseFajar():Guerrero = this.copear(nuevosRoundsFajado = this.roundsFajado +1)

  def resetearFajamiento():Guerrero = this.copear(nuevosRoundsFajado = 0)

  type Contrincantes = (Guerrero, Guerrero)
    type PlanDeAtaque = List[Movimiento]

    def perderMunicion(arma: ArmaDeFuego):Guerrero =
      this.copear( nuevoInventario = this.inventario.filter{itemLista => filtrarArma(itemLista, arma)} :+ ArmaDeFuego(arma.municion-1, arma.tipo))

    def filtrarArma (itemLista :Item, arma: ArmaDeFuego) :Boolean ={
      itemLista match {
        case itemLista : ArmaDeFuego => itemLista.tipo != arma.tipo
        case _ => true
      }
    }

    def cambiarEstado(nuevoEstado: Estado):Guerrero = this.copear(nuevoEstado)
    def tieneItem(item: Item): Boolean = inventario.contains(item)
    def getVida(): Int
    def getVidaMaxima() :Int
    def cambiarVida(nuevaVida: Int): Guerrero = {
      val nuevoGuerrero = if(nuevaVida==0) {
        this.cambiarEstado(Muerto)
      }
      else {
        this
      }
      nuevoGuerrero.setVida(nuevaVida)
    }

    def setVida(nuevaVida: Int): Guerrero
    

    def contraatacar(enemigo: Guerrero): Contrincantes = {
      val movimiento: Movimiento = this.movimientoMasEfectivoContra(enemigo)(diferenciaKiAtacante).getOrElse(Movimiento.pedirAyudaADios)
      val (yoModificado, enemigoModificado) = this.hacerMovimiento(movimiento, (this, enemigo))
      printf("contraatacando")
      (enemigoModificado, yoModificado)
    }


    def pelearRound(movimiento: Movimiento, oponente: Guerrero):Contrincantes ={
      val(yo, oponenteModificado) = this.hacerMovimiento(movimiento, (this, oponente))
      oponenteModificado.contraatacar(yo)
    }

    def pelearRound2(movimiento: Movimiento, oponente: Guerrero):Resultado ={
      val(yo, oponenteModificado) = this.hacerMovimiento(movimiento, (this, oponente))
      val contrincantes = oponenteModificado.contraatacar(yo)
      (contrincantes._1.estado, contrincantes._2.estado) match{
        case (Muerto, Muerto) =>  new PeleaFinalizadaSinGanador()
        case (Muerto,_) =>  new PeleaFinalizada(contrincantes._2)
        case (_,Muerto) =>  new PeleaFinalizada(contrincantes._1)
        case (_,_) =>  new PeleaEnCurso(contrincantes)
      }
    }

    def hacerMovimiento(movimiento: Movimiento, contrincantes: Contrincantes): Contrincantes ={
      (movimiento, contrincantes._1.estado) match{
        case(ComerSemilla,_) => movimiento(contrincantes)
        case(_,Muerto) => contrincantes
        case(_,Inconsciente) => contrincantes
        case (DejarseFajar,_) => movimiento(contrincantes)
        case(_,_) => (movimiento(contrincantes)._1.resetearFajamiento(), movimiento(contrincantes)._2)
      }
    }

     def movimientoMasEfectivoContra(atacado: Guerrero) (criterio: Criterio) : Option[Movimiento] = {
       var movMasEfectivo = this.listaDeMovimientos.maxBy(movimiento => criterio((this, atacado), hacerMovimiento(movimiento, (this, atacado))))
       if (criterio((this, atacado), hacerMovimiento(movMasEfectivo, (this, atacado))) > 0){
         return Some(movMasEfectivo)
       }else{
         return None
       }
     }

     def planDeAtaqueContra(atacado :Guerrero, cantidadDeRounds :Int) (criterio: Criterio): PlanDeAtaque ={
       List.fill(cantidadDeRounds)(0).foldLeft((List(): List[Movimiento],(this, atacado))){
         case ((lista, contrincantes), _) =>
           (
             lista :+ movimientoMasEfectivoContra(atacado)(criterio).getOrElse(Movimiento.pedirAyudaADios),
             contrincantes._1.pelearRound(this.movimientoMasEfectivoContra(atacado)(criterio).getOrElse(Movimiento.pedirAyudaADios),contrincantes._2)
             //hacerMovimiento(this.movimientoMasEfectivoContra(atacado)(criterio), contrincantes)
           )
         // en cada iteracion se suma el mejor movimiento a la lista vacia inicial de movimientos
         // y se calcula el estado de los guerreros despues de aplicar el mejor movimiento
       }._1 // retorna el primer elemento de la tupla (listaDeMejoresMovimientos, contrincantes)

     }

    def pelearContra(oponente: Guerrero)(plan: PlanDeAtaque) :ResultadoPelea ={
      val contrincantes = plan.foldLeft((this, oponente)) {
        case((atacante, atacado), _) if atacante.estado == Muerto || atacado.estado == Muerto => (atacante, atacado)
        case((atacante, atacado), movimiento) => atacante.pelearRound(movimiento, atacado)
      }
      return new ResultadoPelea(contrincantes)
    }

    def pelearContra2(oponente: Guerrero)(plan: PlanDeAtaque) :Resultado = {

      plan.foldLeft(new PeleaEnCurso(this, oponente) : Resultado) { case (resultado, movimiento) => resultado.pelear(movimiento) }
    }

     def tiene7Esferas() ={
       this.inventario.contains(new EsferasDelDragon(7))
     }

    def estaMuerto(): Boolean =  return this.estado == Muerto
  }


abstract class Biologico(val ki: Int, val kiMaximo: Int, estado: Estado, nombre: String, inventario: List[Item], listaDeMovimientos: PlanDeAtaque, roundsFajado :Int) extends Guerrero(estado :Estado, nombre: String, inventario: List[Item], listaDeMovimientos: PlanDeAtaque, roundsFajado :Int) {
  require(ki <= kiMaximo)
  def copear(nuevoEstado :Estado = estado, nuevoNombre :String = nombre, nuevoInventario :List[Item] = inventario, nuevosRoundsFajado :Int = roundsFajado) :Biologico
  def cambiarKi(cantidad: Int): Biologico
  def setVida(nuevaVida: Int): Guerrero ={
    this.cambiarKi(nuevaVida)
  }
  def getVida(): Int ={
    this.ki
  }
  override def recibirAtaque(ataque: Ataque, danio: Int): Guerrero ={
    (ataque, danio) match{
      case (ataque: AtaqueDeEnergia, danio) => this.cambiarVida(this.getVida() - danio)
      case (ataque: AtaqueFisico, danio) => this.cambiarVida(this.getVida() - danio)
    }
  }
  def getVidaMaxima() :Int = this.kiMaximo


}

case class Androide(override val estado: Estado, override val nombre: String, override val inventario: List[Item], bateria: Int, bateriaMaxima :Int, override val listaDeMovimientos: PlanDeAtaque, override val roundsFajado :Int) extends Guerrero(estado :Estado, nombre: String, inventario: List[Item], listaDeMovimientos: PlanDeAtaque, roundsFajado :Int){
  require(bateria <= bateriaMaxima)
  override def copear(nuevoEstado: Estado, nuevoNombre: String, nuevoInventario: List[Item], nuevosRoundsFajado :Int): Androide =
      new Androide(estado= nuevoEstado, nombre= nuevoNombre, inventario= nuevoInventario, bateria = bateria, bateriaMaxima = bateriaMaxima, listaDeMovimientos = listaDeMovimientos, roundsFajado= nuevosRoundsFajado)
    def cambiarBateria(cantidad: Int): Androide={
      this.copy(bateria = bateriaMaxima.min(cantidad))
    }
    def setVida(nuevaVida: Int): Guerrero ={
      this.cambiarBateria(nuevaVida);
    }
    def getVida(): Int ={
      this.bateria
    }
  override def cambiarEstado(nuevoEstado: Estado):Androide = {
     this.copear(nuevoEstado)
  }
  override def recibirAtaque(ataque: Ataque, danio: Int): Guerrero ={
    (ataque, danio) match{
      case (ataque: AtaqueDeEnergia, danio) => this.cambiarVida(this.getVida() + danio)
      case (ataque: AtaqueFisico, danio) => this.cambiarVida(this.getVida() - danio)
    }
  }
  def getVidaMaxima() :Int = this.bateriaMaxima
  }

case class Sayajin(override val estado: Estado, override val ki: Int, override val kiMaximo: Int, override val nombre: String, override val inventario: List[Item], nivelSS: Int, tieneCola: Boolean, override val listaDeMovimientos: PlanDeAtaque, override val roundsFajado :Int) extends Biologico(ki: Int, kiMaximo: Int, estado :Estado, nombre: String, inventario: List[Item], listaDeMovimientos: PlanDeAtaque, roundsFajado :Int) with Fusionable{

    def perderCola :Sayajin = {this.copy(tieneCola = false)}

    override def cambiarKi(cantidad: Int): Sayajin = this.copy(ki = kiMaximo.min(cantidad))

    override def cambiarEstado(nuevoEstado: Estado):Sayajin = {
       nuevoEstado match{
         case Inconsciente => this.perderCola.copy(estado = nuevoEstado, nivelSS = 1)
         case _ => this.copear(nuevoEstado)
       }
    }
      
    override def copear(nuevoEstado: Estado, nuevoNombre: String, nuevoInventario: List[Item], nuevosRoundsFajado :Int): Sayajin =
      new Sayajin(estado = nuevoEstado, nombre = nuevoNombre, inventario = nuevoInventario, ki = ki, kiMaximo = kiMaximo, nivelSS = nivelSS, tieneCola = tieneCola, listaDeMovimientos = listaDeMovimientos, roundsFajado= nuevosRoundsFajado)

    def convertirseEnMono(): Guerrero ={
        if(this.puedeConvertirseEnMono){
        new Mono(estado = this.estado, ki = this.kiMaximo*3, kiMaximo = this.kiMaximo*3, nombre = this.nombre, inventario = this.inventario, this.copy(nivelSS = 0), listaDeMovimientos = this.listaDeMovimientos, roundsFajado = this.roundsFajado) // si el guerrero se transforma en mono el estado de SS se pierde
      } else {this}
    }
    def puedeConvertirseEnMono() ={
      inventario.contains(FotoDeLaLuna) && this.tieneCola //&& this.nivelSS <= 1 Los SS se pueden convertir en mono, pero al volver a su estado normal pierden su nivel SS
    }
    def aumentarNivelSS(): Sayajin = {
      if(this.puedeAumentarNivelSS()){
        this.copy(nivelSS = nivelSS + 1, kiMaximo = kiMaximo * (nivelSS + 1))
      } else {this}
    }
    def puedeAumentarNivelSS(): Boolean ={
      ki >= kiMaximo/2
    }
  }

case class Humano(override val estado: Estado, override val ki: Int, override val kiMaximo: Int, override val nombre: String, override val inventario: List[Item], override val listaDeMovimientos: PlanDeAtaque, override val roundsFajado :Int) extends Biologico(ki :Int, kiMaximo: Int, estado :Estado, nombre: String, inventario: List[Item], listaDeMovimientos: PlanDeAtaque, roundsFajado :Int) with Fusionable{
  override def cambiarKi(cantidad: Int): Humano = this.copy(ki = kiMaximo.min(cantidad))
  override def cambiarEstado(nuevoEstado: Estado):Humano = {
    this.copy(nuevoEstado)
  }
    override def copear(nuevoEstado: Estado, nuevoNombre: String, nuevoInventario: List[Item], nuevosRoundsFajado :Int): Humano =
      new Humano(estado = nuevoEstado, nombre = nuevoNombre, inventario = nuevoInventario, ki = ki, kiMaximo = kiMaximo, listaDeMovimientos = listaDeMovimientos, roundsFajado = nuevosRoundsFajado)
  }

case class Fusionado (override val estado: Estado, override val ki: Int, override val kiMaximo: Int, override val nombre: String, override val inventario: List[Item], guerreroOriginal: Fusionable, override val listaDeMovimientos: PlanDeAtaque, override val roundsFajado :Int) extends Biologico(ki :Int, kiMaximo: Int, estado :Estado, nombre: String, inventario: List[Item], listaDeMovimientos: PlanDeAtaque, roundsFajado :Int){

   override def cambiarKi(cantidad: Int): Biologico = this.copy(ki = kiMaximo.min(cantidad))

   override def cambiarEstado(nuevoEstado: Estado):Biologico = {
       nuevoEstado match{
         case Normal => this.copy(estado = nuevoEstado) 
         case _ => guerreroOriginal.copear()
       }
    }

    override def copear(nuevoEstado: Estado, nuevoNombre: String, nuevoInventario: List[Item], nuevosRoundsFajado :Int): Fusionado =
      new Fusionado(estado = nuevoEstado, nombre = nuevoNombre, inventario = nuevoInventario, ki = ki, kiMaximo = kiMaximo, guerreroOriginal = guerreroOriginal, listaDeMovimientos = listaDeMovimientos, roundsFajado = nuevosRoundsFajado)
  }

case class Namekusein (override val estado: Estado, override val ki: Int, override val kiMaximo: Int, override val nombre: String, override val inventario: List[Item], override val listaDeMovimientos: PlanDeAtaque, override val roundsFajado :Int) extends Biologico(ki :Int, kiMaximo: Int, estado :Estado, nombre: String, inventario: List[Item], listaDeMovimientos: PlanDeAtaque, roundsFajado :Int) with Fusionable{

  override def cambiarKi(cantidad: Int): Namekusein = this.copy(ki = kiMaximo.min(cantidad))

  override def cambiarEstado(nuevoEstado: Estado):Namekusein = {
    this.copy(estado = nuevoEstado)
  }

  override def recibirAtaque(ataque: Ataque, danio: Int): Guerrero ={
    (ataque, danio) match{
      case (ataque: Explotar, danio) => this.cambiarVida(math.max(this.getVida() - danio, 1))
      case (ataque: AtaqueDeEnergia, danio) => this.cambiarVida(this.getVida() + danio)
      case (ataque: AtaqueFisico, danio) => this.cambiarVida(this.getVida() - danio)
    }
  }

  override def copear(nuevoEstado: Estado, nuevoNombre: String, nuevoInventario: List[Item], nuevosRoundsFajado :Int): Namekusein =
      new Namekusein(estado = nuevoEstado, nombre = nuevoNombre, inventario = nuevoInventario, ki = ki, kiMaximo = kiMaximo, listaDeMovimientos = listaDeMovimientos, roundsFajado = nuevosRoundsFajado)
  }

case class Monstruo (override val estado: Estado, override val ki: Int, override val kiMaximo: Int, override val nombre: String, override val inventario: List[Item], override val listaDeMovimientos: PlanDeAtaque, val movimientosAdquiridos: PlanDeAtaque, val formaDeComer: FormaDeComer, override val roundsFajado :Int) extends Biologico(ki :Int, kiMaximo: Int, estado :Estado, nombre: String, inventario: List[Item], listaDeMovimientos: PlanDeAtaque, roundsFajado :Int){

  override def cambiarKi(cantidad: Int):Monstruo  = this.copy(ki = kiMaximo.min(cantidad))

  override def cambiarEstado(nuevoEstado: Estado):Monstruo = this.copy(estado = nuevoEstado)

  override def copear(nuevoEstado: Estado, nuevoNombre: String, nuevoInventario: List[Item], nuevosRoundsFajado :Int): Monstruo =
      new Monstruo(estado = nuevoEstado, nombre = nuevoNombre, inventario = nuevoInventario, ki = ki, kiMaximo = kiMaximo, listaDeMovimientos = listaDeMovimientos, movimientosAdquiridos = movimientosAdquiridos, formaDeComer = formaDeComer, roundsFajado = nuevosRoundsFajado)

  override def recibirAtaque(ataque: Ataque, danio: Int): Guerrero ={
    (ataque, danio) match{
      case (ataque: Onda, danio) => this.cambiarVida(this.getVida - danio/2)
      case (ataque: AtaqueDeEnergia, danio) => this.cambiarVida(this.getVida() + danio)
      case (ataque: AtaqueFisico, danio) => this.cambiarVida(this.getVida() - danio)
    }
  }
  def cambiarMovimientosAdquiridos(nuevosMovimientos: PlanDeAtaque):Monstruo = {this.copy(movimientosAdquiridos = nuevosMovimientos)}
}

case class Mono (override val estado: Estado, override val ki: Int, override val kiMaximo: Int, override val nombre: String, override val inventario: List[Item], val sayajin: Sayajin, override val listaDeMovimientos: PlanDeAtaque, override val roundsFajado :Int) extends Biologico(ki :Int, kiMaximo: Int, estado :Estado, nombre: String, inventario: List[Item], listaDeMovimientos: PlanDeAtaque, roundsFajado :Int){

  def getSayajin :Sayajin = sayajin

  override def cambiarKi(cantidad: Int):Mono = this.copy(ki = kiMaximo.min(cantidad))

  override def cambiarEstado(nuevoEstado: Estado):Mono = {
    this.copear(nuevoEstado)
  }
    override def copear(nuevoEstado: Estado, nuevoNombre: String, nuevoInventario: List[Item], nuevosRoundsFajado :Int): Mono =
      new Mono(estado = nuevoEstado, nombre = nuevoNombre, inventario = nuevoInventario, ki = ki, kiMaximo = kiMaximo, sayajin = sayajin, listaDeMovimientos = listaDeMovimientos, roundsFajado = nuevosRoundsFajado)
  }