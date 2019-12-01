input = File.readlines('input.txt').map(&:to_i)

# step 1
puts input.reduce(&:+)

# step 2
require 'set'

s = Set.new

current_value = 0
index = 0
limit = input.length

loop do
  if s.include?(current_value)
    puts current_value
    break
  else
    s << current_value
  end

  current_value += input[index]
  index = (index + 1) % limit
end
