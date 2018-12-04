class Patron

  attr_accessor :matchers, :bloque_del_with
  def initialize(matchers, &bloque_del_with)
    @matchers= matchers.map { |matcher|
      (matcher.is_a?(Symbol))? SymbolMatcher.new(matcher) :matcher
    }
    @bloque_del_with = bloque_del_with
  end

  def ejecutar_bloque_en_contexto(objeto_a_evaluarse)
    obtener_contexto_bindeado(objeto_a_evaluarse).instance_eval(&bloque_del_with)
  end

  def obtener_contexto_bindeado(objeto_a_evaluarse)
    bindings = matchers.reduce(Hash.new){ |hash, matcher| hash.merge(matcher.get_bindings(objeto_a_evaluarse))}
    ContextoDeEjecucion.new(bindings)
  end

  def matchea(objeto_a_evaluarse)
    matchers.all? do |matcher|
      matcher.call(objeto_a_evaluarse)
    end
  end

end