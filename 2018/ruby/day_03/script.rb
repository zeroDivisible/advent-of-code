require 'set'

input = File.readlines('input.txt')

input = input.map do |line|
  line = line.match(/(.*) @ (\d*),(\d*): (\d*)x(\d*)/).captures
  [1,2,3,4].each do |n|
    line[n] = line[n].to_i
  end

  line
end

fabric = Array.new(1000) { Array.new(1000) { [] }}

input.each do |line|
  id, left, top, width, height = line

  (top...(top+height)).each do |row|
    (left...(left+width)).each do |column|
      fabric[row][column] << id
    end
  end
end

# sum
sum = 0
best_claim = []
overlapping = Set.new

fabric.each do |row|
  sum += row.select { |n| n.length > 1 }.length

  row.select { |n| n.length > 1 }.flatten.each do |el|
    overlapping << el
  end
end

# part 1
puts sum

# part 2
puts (input.map(&:first) - overlapping.to_a).first
