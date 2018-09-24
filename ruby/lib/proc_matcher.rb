class ProcMatcher

  attr_accessor :bloque

  def initialize(&bloque)
    self.bloque = bloque
  end

  def call(objeto_a_evaluarse)
    self.bloque.call(objeto_a_evaluarse)
  end

  def and (*matchers)
    return ProcMatcher.new do
    |callArgument| matchers.all? {|matcher| matcher.call(callArgument)} && self.bloque.call(callArgument)
    end
  end

  def or (*matchers)
    return ProcMatcher.new do
    |callArgument| matchers.any? {|matcher| matcher.call(callArgument)} || self.bloque.call(callArgument)
    end
  end

  def not
    return ProcMatcher.new do
    |callArgument| !self.bloque.call(callArgument)
    end
  end

end