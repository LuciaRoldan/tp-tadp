class Patron

  attr_accessor :matchers, :bloque_del_with
  def initialize(matchers, &bloque_del_with)
    @matchers=matchers.map do
    |matcher| if matcher.is_a?(Symbol)
                SymbolMatcher.new(matcher) do |objeto_a_evaluarse|
                  true
                end
              else matcher
              end
    end
    @bloque_del_with = bloque_del_with
  end

  def ejecutar_bloque_en_contexto
    obtener_contexto_bindeado.instance_eval(&bloque_del_with )
  end

  def obtener_contexto_bindeado
    ContextoDeEjecucion.new(
        matchers.reduce(Hash.new){ |hash, matcher| hash.merge(matcher.bindings)}
    )
  end

  def matchea(objeto_a_evaluarse)
    matchers.all? do |matcher|
      matcher.call(objeto_a_evaluarse)
    end
  end

end