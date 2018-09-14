class Patron
  attr_accessor :lista_de_matchers, :bloque
  def initialize(lista_de_matchers, bloque)
    self.lista_de_matchers = lista_de_matchers
    self.bloque = bloque
  end
  def evaluar_matchers(cosa)
    lista_de_matchers.all? {|matcher| matcher.call(cosa)}
  end
end

class Object
  def with(*matchers, &bloque)
    if(matchers.all? do |matcher| matcher.call(self) end)
      return bloque.call(self)
      #puts "hola2"
      raise 'Cumplo con todas las condiciones del with!'
    end
  end

  def matches?(objeto_a_evaluarse, &bloque)
    #matchers.each do |matcher|
    begin
      resultado = objeto_a_evaluarse.instance_eval(&bloque)
    rescue
      resultado
    end

      #if(bloque.call(foo))
       #   bloque.call
       #   return
      #end
  end
end

class Matcher

  def val (objeto)
    return Proc.new { |otroObjeto| objeto == otroObjeto }
  end

  def type (clase)
    return Proc.new { |objeto| objeto.is_a?(clase) }
  end

  def list (lista, match_size = true)
    return Proc.new { |otraLista| otraLista.is_a?(Array) && (match_size)? (lista == otraLista) : (otraLista.first(lista.length) == lista)}
  end

  def duck ( *mensajes )
    return Proc.new {|objeto| mensajes.all? { |mensaje| objeto.respond_to?(mensaje)  } }
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

class Proc
  def and (*matchers)
    return Proc.new do
    |callArgument| matchers.all? {|matcher| matcher.call(callArgument)} && self.call(callArgument)
    end
  end

  def or (*matchers)
    return Proc.new do
    |callArgument| matchers.any? {|matcher| matcher.call(callArgument)} || self.call(callArgument)
    end
  end

  def not
    return Proc.new do
    |callArgument| !self.call(callArgument)
    end
  end
end

