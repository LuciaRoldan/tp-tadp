class Object

  def matches?(objeto_a_evaluarse, &bloque)
    evaluador = Evaluator.new
    evaluador.instance_eval(&bloque) #creo Patrones   --envio bloque con with's y otherwise al evaluador
    evaluador.evaluar(objeto_a_evaluarse)
  end

end

class Symbol

  def bindear(objeto_a_evaluarse)
    hash = Hash.new
    hash[self] = objeto_a_evaluarse
    puts('el hash a bindear en Symbol ', hash)
    hash
  end

  def call(x)
    true
  end

  def is_bindeable
    true
  end

end