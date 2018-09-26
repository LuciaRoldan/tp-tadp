class Bloque

  attr_accessor  :bloque

  def initialize(&bloque)
    puts(bloque)
    @bloque = bloque
  end

  def call()
    puts(self)
    @bloque.call()
  end

end