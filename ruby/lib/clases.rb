class Object

  def matches?(objeto_a_evaluarse, &bloque)
    evaluador = Evaluator.new
    evaluador.instance_eval(&bloque) #creo Patrones   --envio bloque con with's y otherwise al evaluador
    evaluador.evaluar(objeto_a_evaluarse)
  end

end
