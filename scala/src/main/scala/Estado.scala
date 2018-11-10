package DragonBall

abstract class Estado(val roundsFajado: Int) {
  def dejarseFajar: Estado
  def resetear: Estado
}

case class Normal(override val roundsFajado: Int) extends Estado(roundsFajado: Int){
  override def dejarseFajar ={
    printf((roundsFajado+1).toString)
    this.copy(roundsFajado = roundsFajado+1)
  }
  override def resetear ={
    this.copy(roundsFajado = 0)
  }
}
case class Muerto(override val roundsFajado: Int) extends Estado(roundsFajado: Int){
  override def dejarseFajar ={this.copy()}
  override def resetear ={this.copy()}
}
case class Inconsciente(override val roundsFajado: Int) extends Estado(roundsFajado: Int){
  override def dejarseFajar ={this.copy()}
  override def resetear ={this.copy()}
}