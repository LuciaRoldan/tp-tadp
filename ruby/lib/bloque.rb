class Bloque

  attr_accessor  :bloque

  def initialize(&bloque)
    @bloque = bloque
  end

  def call()
    @bloque.call()
  end

end