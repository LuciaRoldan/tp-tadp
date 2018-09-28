class ProcMatcher

  attr_accessor :bloque, :bindings

  def initialize(&bloque)
    @bloque = bloque
    @bindings = Hash.new
  end

  def agregarBindings(bindings)
    @bindings = @bindings.merge(bindings)
  end

  def call(objeto_a_evaluarse)
    @bloque.call(objeto_a_evaluarse)
  end

  def and (*matchers)
    return ProcMatcher.new do
    |callArgument| matchers.all? {|matcher| matcher.call(callArgument)} && @bloque.call(callArgument)
    end
  end

  def or (*matchers)
    return ProcMatcher.new do
    |callArgument| matchers.any? {|matcher| matcher.call(callArgument)} || @bloque.call(callArgument)
    end
  end

  def not
    return ProcMatcher.new do
    |callArgument| !@bloque.call(callArgument)
    end
  end

end