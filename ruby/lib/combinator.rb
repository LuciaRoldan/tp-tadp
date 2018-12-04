class Combinator < ProcMatcher
  #Porque un combinator entiende los mensajes and, or, not y call
  attr_accessor :matchers

  def initialize(matchers)
    @matchers = matchers
  end

end

class AndCombinator < Combinator
  def call(objeto)
    @matchers.all? {|matcher| matcher.call(objeto)}
  end
end

class OrCombinator < Combinator
  def call(objeto)
    @matchers.any? {|matcher| matcher.call(objeto)}
  end
end

class NotCombinator < Combinator

  attr_accessor :matcher

  def initialize(matcher)
    @matcher = matcher
  end

  def call(objeto)
    !@matcher.call(objeto)
  end
end