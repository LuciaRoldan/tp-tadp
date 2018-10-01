class Evaluator

  attr_accessor :patrones
  def initialize
    @patrones = []
  end

  def with(*matchers, &bloque)
    @patrones.push(Patron.new(matchers, bloque))
  end

  def evaluar(objeto_a_evaluarse)
    resultado = 'lol'
    unless
    (patron = @patrones.find do |patron| patron.matchea(objeto_a_evaluarse) end
    if (patron != nil)
      patron.agregar_bindings(objeto_a_evaluarse)
      resultado = patron.ejecutar_bloque(objeto_a_evaluarse)
    end)
      raise 'Ningun patron matchea. Agregar un otherwise'
    end
    resultado
  end

  def otherwise(&bloque)
    patrones.push(Patron.new([],bloque))
  end



  def val(objeto)
    ProcMatcher.new { |otroObjeto| objeto == otroObjeto }
  end

  def type(clase)
    ProcMatcher.new { |objeto| objeto.is_a?(clase) }
  end

  def agregar_bindings_lista(objeto_a_evaluarse, bloque)
    tuplas = lista.zip(otraLista)
    hashes = Hash[tuplas.select{ |tupla| tupla[0].is_a?(Symbol) }]
    agregar_bindings(Hash[hashes])
  end

  def list(lista, match_size = true)
    pm = ProcMatcher.new do |otraLista|
      pm.lista = lista
      tuplas = lista.zip(otraLista)
      tuplas.all? do |a, b|
        (a == b ||
            a.is_a?(Symbol) ||
            (a.is_a?(ProcMatcher)? a.instance_exec(b, &a.bloque): false ))
      end &&
          otraLista.is_a?(Array) &&
          ((match_size)? (otraLista.length == lista.length) : true )
    end
  end

  def duck ( *mensajes )
    ProcMatcher.new {|objeto| mensajes.all? { |mensaje| objeto.respond_to?(mensaje)  } }
  end

end