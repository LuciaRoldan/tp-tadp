class Evaluator

  attr_accessor :patrones
  def initialize
    @patrones = []
  end

  def evaluar(objeto_a_evaluarse)
    patron = @patrones.find do |patron| patron.matchea(objeto_a_evaluarse) end

    if patron != nil

      return patron.ejecutar_bloque_en_contexto(objeto_a_evaluarse)
    end

    raise 'Ningun patron matchea. Agregar un otherwise'
  end

  def with(*matchers, &bloque)
    @patrones.push(Patron.new(matchers, &bloque) )
  end

  def otherwise(&bloque)
    patrones.push(Patron.new([],&bloque))
  end


  def val(valor)
    ValMatcher.new valor
  end

  def type(clase)
    TypeMatcher.new clase
  end


  def list(lista, match_size = true)
    ListMatcher.new lista, match_size
  end

  def duck ( *mensajes )
    DuckMatcher.new mensajes
  end

end