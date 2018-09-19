class Patron
  attr_accessor :matchers, :bloque
  def initialize(bloque, *matchers)
    self.matchers = matchers
    self.bloque = bloque
  end
  def evaluar_matchers(cosa)
    matchers.all? {|matcher| Matcher.send(matcher).call(cosa)}
  end
end

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
    puts('en evaluar')
    @patrones.each do |patron|
      puts('evaluando')
      if(patron.evaluar_matchers(objeto_a_evaluarse))
        puts('pase el if de evaluar')
        patron.bloque.call(objeto_a_evaluarse)
        puts('voy a hacer el break')
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

end

class Object

  def matches?(objeto_a_evaluarse, &bloque)
    evaluador = Evaluator.new
    puts('hice el Evaluator new')
    evaluador.instance_eval(&bloque)
    puts('despues del instance_eval')
    evaluador.evaluar(objeto_a_evaluarse)
  end
end

class MyError < StandardError
  attr_reader :respuesta
  def initialize(msg, respuesta)
    @respuesta = respuesta
    super(msg)
  end
end

class Matcher

  def val (objeto)
     ProcMatcher.new { |otroObjeto| objeto == otroObjeto }
  end

  def type (clase)
     ProcMatcher.new { |objeto| objeto.is_a?(clase) }
  end

  def list (lista, match_size = true)
     ProcMatcher.new { |otraLista| otraLista.is_a?(Array) && (match_size)? (lista == otraLista) : (otraLista.first(lista.length) == lista)}
  end

  def duck ( *mensajes )
     ProcMatcher.new {|objeto| mensajes.all? { |mensaje| objeto.respond_to?(mensaje)  } }
  end
end

class ProcMatcher

  attr_accessor :bloque

  def initialize(&bloque)
    self.bloque = bloque
  end

  def call(objeto_a_evaluarse)
    self.bloque.call(objeto_a_evaluarse)
  end

  def and (*matchers)
    return Proc.new do
    |callArgument| matchers.all? {|matcher| Matcher.send(matcher).call(callArgument)} && self.bloque.call(callArgument)
    end
  end

  def or (*matchers)
    return Proc.new do
    |callArgument| matchers.any? {|matcher| Matcher.send(matcher).call(callArgument)} || self.bloque.call(callArgument)
    end
  end

  def not
    return Proc.new do
    |callArgument| !self.bloque.call(callArgument)
    end
  end

end

class Symbol

  def call(algo)

    #algo.to_sym = self
    #self = algo
    #algo = self

    return true
  end

end


