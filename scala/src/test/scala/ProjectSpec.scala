package DragonBall


import DragonBall._
import DragonBall.Guerrero.{Androide, Guerrero, Sayajin}
import DragonBall.Estado
import DragonBall.Movimiento._
import org.scalatest.{FreeSpec, Matchers}



class DragonBallSpec extends FreeSpec with Matchers {
  ("Goku" should "Cargar su ki y quedar en 250").in({
      val goku = new Guerrero(raza= Sayajin(ki= 100, nivelSS = 1),estado= Normal, nombre = "GOKU", inventario = List())

      val vegeta = new Guerrero(raza= Sayajin(ki= 100, nivelSS = 1),estado= Normal, nombre = "VEGETA", inventario = List())


      val gokuKiCargado = cargarKi(goku, vegeta)

      gokuKiCargado._1.raza.ki should be(250)
    })

}