
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

class Proc
  def and (*matchers)
    return Proc.new do
    |callArgument| matchers.all? {|matcher| matcher.call(callArgument)} && self.call(callArgument)
    end
  end
end

