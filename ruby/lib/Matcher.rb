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

    if matchers.all? do |matcher| matcher.call(self) end

      bloque.call(self)

      #self.match(bloque)

      raise 'Cumplo con todas las condiciones del with!'

      #binding
    end
  end

  def matches?(objeto_a_evaluarse, &bloque)
    begin
    objeto_a_evaluarse.instance_eval(&bloque)
    rescue => respuesta
      respuesta
    end
  end

  def match(bloque)
    self.call
  end
  def otherwise (&bloque)
    self.call(bloque)
  end
end

class Object

  def val (objeto)
     Proc.new { |otroObjeto| objeto == otroObjeto }
  end

  def type (clase)
     Proc.new { |objeto| objeto.is_a?(clase) }
  end

  def list (lista, match_size = true)
     Proc.new { |otraLista| otraLista.is_a?(Array) && (match_size)? (lista == otraLista) : (otraLista.first(lista.length) == lista)}
  end

  def duck ( *mensajes )
     Proc.new {|objeto| mensajes.all? { |mensaje| objeto.respond_to?(mensaje)  } }
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

