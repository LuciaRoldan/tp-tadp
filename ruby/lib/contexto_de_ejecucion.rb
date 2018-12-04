class ContextoDeEjecucion
  attr_accessor :matchers
  def initialize(bindings)
    puts(bindings)
    bindings.each do |binding|
      define_singleton_method(binding[0]) {binding[1]}
    end
  end
end