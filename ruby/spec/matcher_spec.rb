

describe 'val' do

  it '5 es igual a 5' do
    expect(val(5).call(5)).to eq(true)
  end

  it '5 es distinto al caracter 5' do
    expect(val(5).call('5')).to eq(false)
  end

  it '5 es distinto a 4' do
    expect(val(5).call(4)).to eq(false)
  end

end

describe 'type' do

  it '5 es de tipo Integer' do
    expect(type(Integer).call(5)).to eq(true)
  end

  it 'Un string no es un simbolo' do
    expect(type(Symbol).call("Trust me, I'm a Symbol...")).to eq(false)
  end

  it 'Un simbolo es de tipo Symbol' do
    expect(type(Symbol).call(:a_real_symbol)).to eq(true)
  end

end

describe 'list' do

  let (:an_array)  {[1, 2, 3, 4]}

  it 'Una lista es igual a si misma' do
    expect(list([1, 2, 3, 4], true).call(an_array)).to eq(true)
  end

  it 'Una lista es igual a si misma' do
    expect(list([1, 2, 3, 4], false).call(an_array)).to eq(true)
  end

  it 'Los primeros elementos de una lista son iguales a los primeros elementos de si misma' do
    expect(list([1, 2, 3], true).call(an_array)).to eq(false)
  end

  it 'Los primeros elementos de una lista es distinta a si misma' do
    expect(list([1, 2, 3], false).call(an_array)).to eq(true)
  end

  it 'La lista desordenada es distinta a la lista' do
    expect(list([2, 1, 3, 4], true).call(an_array)).to eq(false)
  end

  it 'La lista desordenada es distinta a la lista' do
    expect(list([2, 1, 3, 4], false).call(an_array)).to eq(false)
  end

  it 'Si no se especifica march_size se considera true' do
    expect(list([1, 2, 3]).call(an_array)).to eq(false)
  end

end

describe 'duck' do

  class Psyduck

    def cuack
      'psy..duck'
    end

    def fly
      '(headache)'
    end

  end

  class Dragon

    def fly
      'do some flying'
    end

  end

  let (:psyduck) { Psyduck.new() }
  let (:dragon) { Dragon.new() }


  it 'Psyduck responde a cuack y fly' do
    expect(duck(:cuack, :fly).call(psyduck)).to eq(true)
  end

  it 'Psyduck responde a cuack y fly' do
    expect(duck(:cuack, :fly).call(psyduck)).to eq(true)
  end



  it 'Dragon no responde a cuack y fly' do
    expect(duck(:cuack, :fly).call(dragon)).to eq(false)
  end

  it 'Dragon responde fly' do
    expect(duck(:fly).call(dragon)).to eq(true)
  end

  it 'Object responde a to_s' do
    expect(duck(:to_s).call(Object.new)).to eq(true)
  end

end

describe 'and' do

  it '5 es igual que 5 y es de tipo Fixnum y entiende el +' do
    expect(duck(:+).and(type(Fixnum),val(5)).call(5)).to eq(true)
  end

  it '5 es igual que string 5 y es de tipo Fixnum y entiende el +' do
    expect(duck(:+).and(type(Fixnum),val('5')).call(5)).to eq(false)
  end

  it '5 es igual que 5 y es de tipo Fixnum y entiende el +' do
    expect(duck(:hola).and(type(Fixnum),val(5)).call(5)).to eq(false)
  end

  it '5 es igual que 5 y es de tipo Fixnum y entiende el +' do
    expect(type(Fixnum).and(duck(:+),val(5)).call(5)).to eq(true)
  end
end