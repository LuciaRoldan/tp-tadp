class Evaluator

  attr_accessor :patrones, :el_match

  def initialize
    @patrones = []
  end

  def with(*matchers, &bloque)
    @patrones.push(Patron.new(matchers, bloque))
  end

  def evaluar(objeto_a_evaluarse)
    @patrones.each do |patron|
      if(patron.evaluar_matchers(objeto_a_evaluarse))
        patron.ejecutar_bloque(objeto_a_evaluarse)
        break
      end
    end
    self.patrones = []
  end

  def ro_evaluar(objeto_a_evaluarse)
    @el_match = @patrones.find { |patron|
      patron.evaluar_matchers(objeto_a_evaluarse)
    }
  end

  def ro_ejecutar_matcheo(objeto_a_evaluarse)
    @el_match.ejecutar_bloque(objeto_a_evaluarse)
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
    patrones.push(Patron.new([],bloque))
  end


  def val (objeto)
    ProcMatcher.new { |otroObjeto| objeto == otroObjeto }
  end

  def type (clase)
    ProcMatcher.new { |objeto| objeto.is_a?(clase) }
  end

  def list (lista, match_size = true)
        
    ProcMatcher.new do |otraLista|

      lista.each_with_index do
      |elemento, index|
        if elemento.is_a? Symbol
          Evaluator.define_method(elemento) do otraLista[index] end
        end
      end

      otraLista.is_a?(Array) &&
        (match_size)? (lista == otraLista) : (otraLista.first(lista.length) == lista)
    end
  end

  def duck ( *mensajes )
    ProcMatcher.new {|objeto| mensajes.all? { |mensaje| objeto.respond_to?(mensaje)  } }
  end

end