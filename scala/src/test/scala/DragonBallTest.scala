import DragonBall.Movimiento._
import DragonBall._
import org.scalatest.FunSuite

class DragonBallTest extends FunSuite {

  val goku = Sayajin(estado = Normal, ki = 100, kiMaximo = 100, nombre = "GOKU", inventario = List(ArmaFilosa, ArmaDeFuego), nivelSS = 1, tieneCola = true, listaDeMovimientos = List(cargarKi, new usarItem(ArmaFilosa), new usarItem(ArmaDeFuego)))
  val vegeta = Sayajin(estado = Normal, ki = 100, kiMaximo = 100, nombre = "VEGETA", inventario = List(ArmaFilosa, FotoDeLaLuna), nivelSS = 1, tieneCola = true, listaDeMovimientos = List(cargarKi, new usarItem(ArmaFilosa)))
  val krillin = Humano(estado = Normal, ki = 100, kiMaximo = 100, nombre = "KRILLIN", inventario = List(), listaDeMovimientos = List(dejarseFajar))
  val androide18 = Androide(estado = Normal, nombre = "ANDROIDE 18", inventario = List(), bateria = 100, listaDeMovimientos = List())

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

  test("Mi objeto bizarro anda"){
    val vidaGoku = goku.getVida()

    assert(vidaGoku.equals(100))
  }

  test("Mi objeto bizarro anda pt2"){
    val androide18ConVidaNueva = androide18.cambiarVida(85)
    val vidaNuevaAndroide18 = androide18ConVidaNueva.getVida()

    printf(vidaNuevaAndroide18.toString)

    assert(vidaNuevaAndroide18.equals(85))
  }
}
