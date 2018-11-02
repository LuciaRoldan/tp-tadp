package DragonBall


import DragonBall._
import DragonBall.Guerrero.Sayajin
import DragonBall.Estado
import DragonBall.Movimiento._

import org.scalatest.{FreeSpec, Matchers}



class DragonBallSpec extends FreeSpec with Matchers {
  "Un pokemon" should "realizar actividad descansar y recuperar su energia" in {
    val goku = Sayajin(estado = Normal, ki = 100, nombre = "GOKU", inventario = List(), nivelSS = 1)

    val vegeta = Sayajin(estado = Normal, ki = 100, nombre = "VEGETA", inventario = List(), nivelSS = 1)


    val gokuKiCargado = cargarKi(goku, vegeta)

    gokuKiCargado._1.ki should be(250)
  }

}