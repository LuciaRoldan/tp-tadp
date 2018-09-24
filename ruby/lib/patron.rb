class Patron

  attr_accessor :matchers, :bloque
  def initialize(bloque, *matchers)
    #self.matchers = []
    self.matchers = matchers
    self.bloque = bloque
  end
  def ejecutar_bloque(objeto_a_evaluarse)
    puts('Lo que devuelve el bloque es: ', bloque.call(objeto_a_evaluarse))
    bloque.call(objeto_a_evaluarse)
  end
  def evaluar_matchers(cosa)
    evalu = Evaluator.new
    self.matchers.all? {|matcher| Evaluator.instance_exec(matcher) {cosa}}
  end

end