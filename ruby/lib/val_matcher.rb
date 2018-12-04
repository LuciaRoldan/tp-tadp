class ValMatcher < ProcMatcher

  attr_accessor :bloque

  def initialize(&bloque)
    @bloque = bloque
  end

  def bindear(objeto_a_evaluar)
    Hash.new
  end

end