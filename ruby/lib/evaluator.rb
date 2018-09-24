class Evaluator

  attr_accessor :patrones

  def initialize
    @patrones = []
  end

  def with(*matchers, &bloque)
    puts('antes del push')
    @patrones.push(Patron.new(bloque, matchers))
    puts('hice el push')
  end

  def evaluar(objeto_a_evaluarse)
    @patrones.each do |patron|
      if(patron.evaluar_matchers(objeto_a_evaluarse))
        puts('pase el if de evaluar')
        patron.ejecutar_bloque(objeto_a_evaluarse)
        break
      end
    end
    self.patrones = []
  end

  # if(matchers.all? do |matcher| matcher.call(self) end)
  #  simbolos = matchers.select{|matcher| matcher.is_a?(Symbol)}
  #   simbolos.each { |simbolo| bloque.instance_variable_set("@#{simbolo}", self)
  #   }
  #   puts bloque.instance_variable_get("@a_string")
  #   puts bloque.call(self)
  #   raise MyError.new("Cumplo con todas las condciones!", bloque.call(self))
  # end

  def otherwise(&bloque)
    patrones.push(Patron.new(bloque))
  end


  def val (objeto)
    ProcMatcher.new { |otroObjeto| objeto == otroObjeto }
  end

  def type (clase)
    ProcMatcher.new { |objeto| objeto.is_a?(clase) }
  end

  def list (lista, match_size = true)
    ProcMatcher.new do |otraLista| otraLista.is_a?(Array) &&
        (match_size)? (lista == otraLista) : (otraLista.first(lista.length) == lista)
    end
  end

  def duck ( *mensajes )
    ProcMatcher.new {|objeto| mensajes.all? { |mensaje| objeto.respond_to?(mensaje)  } }
  end

end