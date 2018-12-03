class Evaluator

  attr_accessor :patrones
  def initialize
    @patrones = []
  end

  def evaluar(objeto_a_evaluarse)

    patron = @patrones.find do |patron| patron.matchea(objeto_a_evaluarse) end

    if patron != nil
      return patron.ejecutar_bloque_en_contexto
    end

    raise 'Ningun patron matchea. Agregar un otherwise'
  end

  def with(*matchers, &bloque)
    @patrones.push(Patron.new(matchers, &bloque) )
  end

  def otherwise(&bloque)
    patrones.push(Patron.new([],&bloque))
  end


  def val(objeto)
    ProcMatcher.new { |otroObjeto| objeto == otroObjeto }
  end

  def type(clase)
    ProcMatcher.new { |objeto| objeto.is_a?(clase) }
  end


  def list(lista, match_size = true)
    pm = ProcMatcher.new do |otraLista|

      pm.agregar_bindings_de_listas(lista, otraLista)

      tuplas = lista.zip(otraLista)
      tuplas.all? do |a, b|
        (a == b ||
            a.is_a?(Symbol) ||
            (a.is_a?(ProcMatcher)? a.call(b): false ))
      end &&
          otraLista.is_a?(Array) &&
          ((match_size)? (otraLista.length == lista.length) : true )
    end
  end

  def duck ( *mensajes )
    ProcMatcher.new {|objeto| mensajes.all? { |mensaje| objeto.respond_to?(mensaje)  } }
  end

end