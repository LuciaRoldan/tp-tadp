class ValMatcher < ProcMatcher

  attr_accessor :objeto

  def initialize(objeto)
    @objeto = objeto
  end

  def call(otroObjeto)
    objeto == otroObjeto
  end

  def bindear(objeto_a_evaluar)
    Hash.new
  end

end