class Evaluator

  attr_accessor :patrones, :el_match

  def initialize
    @patrones = []
  end

  def with(*matchers, &bloque)
    @patrones.push(Patron.new(matchers, bloque))
  end

  def evaluar(objeto_a_evaluarse)
    resultado = false
    if(@patrones.none? do |patron| patron.evaluar_matchers(objeto_a_evaluarse) end)
      raise 'Ningun patron matchea. Agregar un otherwise'
    end
    @patrones.each do |patron|
      if(patron.evaluar_matchers(objeto_a_evaluarse))
        resultado = patron.ejecutar_bloque(objeto_a_evaluarse)
        break
      end
    end
    self.patrones = []
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

  def list(lista, match_size = true)
    ProcMatcher.new do |otraLista|

      tuplas = lista.zip(otraLista)
      hashes = Hash[tuplas.select{ |tupla| tupla[0].is_a?(Symbol) }]
      agregarBindings(Hash[hashes]) if self.is_a? (ProcMatcher)

      tuplas.all? do |a, b|
        puts('a:', a)
        puts('b: ', b)
        (   a == b ||
            a.is_a?(Symbol) ||
            if (a.is_a?(ProcMatcher))
              a.instance_exec(b, &a.bloque)
            end
        ) && otraLista.is_a?(Array) && (match_size)? otraLista.length < lista.length: true
      end
    end
  end

  def duck ( *mensajes )
    ProcMatcher.new {|objeto| mensajes.all? { |mensaje| objeto.respond_to?(mensaje)  } }
  end

end