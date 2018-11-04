package DragonBall


import DragonBall._
import DragonBall.Guerrero.Sayajin
import DragonBall.Estado
import DragonBall.Movimiento._

import org.scalatest.{FreeSpec, Matchers}



class DragonBallSpec extends FreeSpec with Matchers {
  ("Goku" should "Cargar su ki y quedar en 250").in({
      val goku = Sayajin(estado = Normal, ki = 100, nombre = "GOKU", inventario = List(), nivelSS = 1)

      val vegeta = Sayajin(estado = Normal, ki = 100, nombre = "VEGETA", inventario = List(), nivelSS = 1)


      val gokuKiCargado: (Sayajin, Sayajin) = cargarKi(goku, vegeta)

      gokuKiCargado._1.ki should be(250)
    })

}