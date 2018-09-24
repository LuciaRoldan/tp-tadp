class Object

  def matches?(objeto_a_evaluarse, &bloque)
    evaluador = Evaluator.new
    evaluador.instance_eval(&bloque)
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


class Symbol

  def call(algo)

    #algo.to_sym = self
    #self = algo
    #algo = self

    return true
  end

end