import DragonBall.FormaDeComer.FormaDeComerDeMajinBuu
import DragonBall.Magia._
import DragonBall.Movimiento._
import DragonBall._
import org.scalatest.FunSuite

class DragonBallTest extends FunSuite {
////////////PERSONAJES
  val goku = Sayajin(estado = Normal, ki = 100, kiMaximo = 100, nombre = "GOKU", inventario = List(ArmaFilosa,new ArmaDeFuego(10), new EsferasDelDragon(7)), nivelSS = 1, tieneCola = true, listaDeMovimientos = List(cargarKi, new usarItem(ArmaFilosa), new usarItem(new ArmaDeFuego(10)), new hacerMagia(vaciarInventarioEnemigo)), roundsFajado = 0)
  val gokuConSoloFilosa = Sayajin(estado = Normal, ki = 100, kiMaximo = 100, nombre = "GOKU", inventario = List(ArmaFilosa), nivelSS = 1, tieneCola = true, listaDeMovimientos = List(DejarseFajar), roundsFajado = 0)
  val vegeta = Sayajin(estado = Normal, ki = 100, kiMaximo = 100, nombre = "VEGETA", inventario = List(ArmaFilosa, FotoDeLaLuna), nivelSS = 1, tieneCola = true, listaDeMovimientos = List(cargarKi, new usarItem(ArmaFilosa)), roundsFajado = 0)
  val   krillin = Humano(estado = Normal, ki = 100, kiMaximo = 100, nombre = "KRILLIN", inventario = List(), listaDeMovimientos = List(DejarseFajar), roundsFajado = 0)
  val androide18 = Androide(estado = Normal, nombre = "ANDROIDE 18", inventario = List(ArmaFilosa), bateria = 100, bateriaMaxima = 100, listaDeMovimientos = List(DejarseFajar), roundsFajado = 0)
  val majinBuu = Monstruo(estado = Normal, ki = 100, kiMaximo = 100, nombre = "MAJIN BUU", inventario = List(), listaDeMovimientos = List(new comerseAlOponente()), movimientosAdquiridos = List(), FormaDeComerDeMajinBuu, roundsFajado = 0)
  val piccolo = Namekusein(estado = Normal, ki = 100, kiMaximo = 100, nombre = "PICCOLO", inventario = List(), listaDeMovimientos = List(new hacerMagia(new aumentarVidaPropiaYDisminuirLaDelEnemigo(30))), roundsFajado = 0)


  ////////////ARMAS
  val armaDeFuegoAndroide= new ArmaDeFuego(10)
  ////////////MAGIAS

  object dejarInconcienteAlOponente extends Magia {
    def apply(contrincantes: (Guerrero, Guerrero)): (Guerrero, Guerrero) = {
      val (atacante, atacado) = contrincantes
      return (atacante,atacado.cambiarEstado(Inconsciente))
    }
  }

  class aumentarVidaPropiaYDisminuirLaDelEnemigo(vida: Int) extends Magia {
    def apply(contrincantes: (Guerrero, Guerrero)): (Guerrero, Guerrero) = {
      val (atacante, atacado) = contrincantes
      return (atacante.cambiarVida(atacante.getVida() + vida), atacado.cambiarVida(atacado.getVida() - vida))
    }
  }

  object vaciarInventarioEnemigo extends Magia {
    def apply(contrincantes: (Guerrero, Guerrero)): (Guerrero, Guerrero) = {
      val (atacante, atacado) = contrincantes
      return (atacante, atacado.copear(nuevoInventario = List()))
    }
  }

  ////////////TESTS

  test("Goku carga su ki") {
    val gokuKiCargado = cargarKi(goku, vegeta)._1

    assert(gokuKiCargado == goku.copy(ki = 250))
  }
  test("Goku y Vegeta pelean un round") {
    val nuevoVegeta = goku.pelearRound(new usarItem(ArmaFilosa), vegeta)._2

    assert(!nuevoVegeta.asInstanceOf[Sayajin].tieneCola)
  }
  test("Goku pelea contra Krillin"){
    val krillinNuevo = goku.pelearContra(krillin)(List(new usarItem(ArmaFilosa), cargarKi))._2

    assert(krillin.ki > krillinNuevo.asInstanceOf[Humano].ki)
  }

  test("Krillin se deja fajar 2 rounds y luego tira un genkidama a goku"){
    val krillinConMasKiParaQueNoMuera = krillin.cambiarKi(1000)
    val gokuConMasVidaParaVerQueSeLeResteBienElKi = gokuConSoloFilosa.cambiarKi(500)
    val gokuNuevo = krillinConMasKiParaQueNoMuera.pelearContra(gokuConMasVidaParaVerQueSeLeResteBienElKi)(List(DejarseFajar, DejarseFajar, new hacerAtaqueTurbina(Genkidama)))._2

    //printf(gokuNuevo.getVida.toString)
    assert(gokuNuevo.getVida == 400)
  }

  test("Krillin se deja fajar 2 rounds y luego tira un genkidama a un androide"){
    val krillinConMasKiParaQueNoMuera = krillin.cambiarKi(1000)
    val androide18SuperEnergico = androide18.copy(bateriaMaxima = 1000)
    val resultado = krillinConMasKiParaQueNoMuera.pelearContra(androide18SuperEnergico)(List(DejarseFajar, DejarseFajar, new hacerAtaqueTurbina(Genkidama)))
    val androideNuevo = resultado._2

    assert(androideNuevo.getVida == 200)
  }


  test("Vegeta se convierte en un mono"){
    val vegetaTransformado = vegeta.pelearRound(convertirseEnMono, krillin)._1

    assert(vegetaTransformado.isInstanceOf[Mono])
  }
  test("Vegeta se convierte en super sayajin"){
    val vegetaTransformado = vegeta.aumentarNivelSS()

    assert(vegetaTransformado.nivelSS == 2)
  }
  test("Goku y Vegeta se fusionan"){
    val gogeta = goku.hacerMovimiento(new fusionarse(vegeta), (goku,krillin))._1

    assert(gogeta.isInstanceOf[Fusionado])
    assert(gogeta.listaDeMovimientos == goku.listaDeMovimientos ::: vegeta.listaDeMovimientos)
    assert(gogeta.getVida() == goku.getVida() + vegeta.getVida())
    assert(gogeta.getVidaMaxima() == goku.getVidaMaxima() + vegeta.getVidaMaxima())

  }

  test("Test de get vida"){
    val vidaGoku = goku.getVida()

    assert(vidaGoku.equals(100))
  }

  test("Test de cambiar vida"){
    val androide18ConVidaNueva = androide18.cambiarVida(85)
    val vidaNuevaAndroide18 = androide18ConVidaNueva.getVida()

    printf(vidaNuevaAndroide18.toString)

    assert(vidaNuevaAndroide18.equals(85))
  }


  test("Majin Boo se come a Krillin"){
    val (buuModificado: Monstruo, krillinModificado) = majinBuu.hacerMovimiento(new comerseAlOponente(), (majinBuu,krillin))

    assert(buuModificado.movimientosAdquiridos == List(DejarseFajar) &&  krillinModificado.estado == Muerto)
  }

  test("Goku puede hacer magia porque tiene 7 esferas del dragon"){
    val(gokuModificado, androideModificado) = goku.hacerMovimiento(new hacerMagia(vaciarInventarioEnemigo), (goku, androide18))

    assert(!gokuModificado.inventario.contains(new EsferasDelDragon(7)) && androideModificado.inventario.isEmpty)
  }

  test("Piccolo puede hacer magia") {
    val(piccoloModificado, majinBuuModificado) = piccolo.hacerMovimiento(new hacerMagia(new aumentarVidaPropiaYDisminuirLaDelEnemigo(30)), (piccolo, majinBuu))

    assert(majinBuuModificado.getVida() == majinBuu.getVida() - 30)
  }

  test("Un guerrero puede comer una semilla aunque este muerto") {
    val(piccoloModificado, majinBuuModificado) = piccolo.copy(estado=Muerto).cambiarVida(5).hacerMovimiento(ComerSemilla, (piccolo, majinBuu))

    assert(piccoloModificado.getVida() == piccoloModificado.getVidaMaxima()
    )
  }

}
