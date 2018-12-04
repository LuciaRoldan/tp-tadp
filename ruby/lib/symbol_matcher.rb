class SymbolMatcher < ProcMatcher

  attr_accessor :bloque, :bindings

  def initialize(simbolo, &bloque)
    @simbolo = simbolo
    @bloque = bloque
    @bindings = Hash.new
  end

  def bindear(objeto_a_evaluar)
    bindings[simbolo] = objeto_a_evaluar
  end


end