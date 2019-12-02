use std::{
    fs::File,
    io::{prelude::*, BufReader},
    path::Path,
};

fn lines_from_file(filename: impl AsRef<Path>) -> Vec<String> {
    let file = File::open(filename).expect("no such file");
    let buf = BufReader::new(file);
    buf.lines()
        .map(|l| l.expect("Could not parse line"))
        .collect()
}

fn execute_one(input: &mut Vec<i32>, register: usize) -> bool {
    let opcode = input[register];
    if opcode == 99 {
        return false
    }

    let arg1 = input[input[register + 1] as usize];
    let arg2 = input[input[register + 2] as usize];
    let outindex = input[register + 3] as usize;

    match opcode {
        1 => {
            input[outindex] = arg1 + arg2;
            return true
        }
        2 => {
            input[outindex] = arg1 * arg2;
            return true
        }
        _ => {
            panic!("invalid opcode")
        }
    }
}

fn execute_all(input: &mut Vec<i32>, register: usize) -> () {
    let mut index = register;
    let done = false;

    while !done {
        let finished = execute_one(input, index);
        if !finished {
            break
        }

        index += 4
    }
}

fn prepare_input(input: &Vec<String>) -> Vec<i32> {
    let line = input.first().unwrap();
    line
        .split(",")
        .map(|s| s.to_string().parse::<i32>().unwrap())
        .collect()
}


fn part_01(input: &Vec<String>) -> () {
    let mut parsed = prepare_input(input);

    parsed[1] = 12;
    parsed[2] = 2;

    execute_all(&mut parsed, 0);

    dbg!(parsed[0]);
}

fn part_02(input: &Vec<String>) -> () {
    let parsed = prepare_input(input);

    for noun in 0..99 {
        for verb in 0..99 {
            let mut program = parsed.clone();
            program[1] = noun;
            program[2] = verb;

            execute_all(&mut program, 0);

            if program[0] == 19690720 {
                println!("noun = {}, verb = {}", noun, verb);
                println!("result = {}", 100 * noun + verb);
                break;
            }
        }
    }

}

fn main() {
    let input = lines_from_file("input/input.txt");

    part_01(&input);
    part_02(&input);
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn it_prepares_input() {
        let input = vec!["1,2,3,4,5".to_string()];
        assert_eq!(prepare_input(&input), vec![1,2,3,4,5]);
    }

    #[test]
    fn it_executes_one_iteration() {
        let mut input = vec![1,0,0,0,99];
        execute_one(&mut input, 0);
        assert_eq!(input, vec![2,0,0,0,99]);

        let mut input = vec![2,3,0,3,99];
        execute_one(&mut input, 0);
        assert_eq!(input, vec![2,3,0,6,99]);

        let mut input = vec![2,4,4,5,99,0];
        execute_one(&mut input, 0);
        assert_eq!(input, vec![2,4,4,5,99,9801]);

        let mut input = vec![1,1,1,4,99,5,6,0,99];
        execute_one(&mut input, 0);
        execute_one(&mut input, 4);
        let return_value = execute_one(&mut input, 8);

        assert_eq!(input, vec![30,1,1,4,2,5,6,0,99]);
        assert_eq!(return_value, false);
    }

    #[test]
    fn it_executes_all() {
        let mut input = vec![1,1,1,4,99,5,6,0,99];
        execute_all(&mut input, 0);

        assert_eq!(input, vec![30,1,1,4,2,5,6,0,99]);
    }
}
