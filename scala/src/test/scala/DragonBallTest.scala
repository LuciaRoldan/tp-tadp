import DragonBall.FormaDeComer.FormaDeComerDeMajinBuu
import DragonBall.Movimiento._
import DragonBall._
import org.scalatest.FunSuite

class DragonBallTest extends FunSuite {

  val goku = Sayajin(estado = new Normal(0), ki = 100, kiMaximo = 100, nombre = "GOKU", inventario = List(ArmaFilosa, ArmaDeFuego), nivelSS = 1, tieneCola = true, listaDeMovimientos = List(cargarKi, new usarItem(ArmaFilosa), new usarItem(ArmaDeFuego)))
  val vegeta = Sayajin(estado = new Normal(0), ki = 100, kiMaximo = 100, nombre = "VEGETA", inventario = List(ArmaFilosa, FotoDeLaLuna), nivelSS = 1, tieneCola = true, listaDeMovimientos = List(cargarKi, new usarItem(ArmaFilosa)))
  val krillin = Humano(estado = new Normal(0), ki = 100, kiMaximo = 100, nombre = "KRILLIN", inventario = List(), listaDeMovimientos = List(dejarseFajar))
  val androide18 = Androide(estado = new Normal(0), nombre = "ANDROIDE 18", inventario = List(), bateria = 100, listaDeMovimientos = List())
  val majinBuu = Monstruo(estado = new Normal(0), ki = 100, kiMaximo = 100, nombre = "MAJIN BUU", inventario = List(), listaDeMovimientos = List(new comerseA(krillin)), movimientosAdquiridos = List(), FormaDeComerDeMajinBuu)

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

  test("setters de los estados"){
    val estado = new Inconsciente(5)

    assert(estado.roundsFajado.equals(5))
  }

  test("Majin Boo se come a Krillin"){
    val (buuModificado: Monstruo, krillinModificado) = majinBuu.hacerMovimiento(new comerseA(krillin), (majinBuu,krillin))

    assert(buuModificado.movimientosAdquiridos.contains(dejarseFajar) &&  krillinModificado.estado == new Muerto(0))
  }

}
