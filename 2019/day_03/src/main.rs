use std::{
    collections::HashSet,
    fmt,
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

#[derive(Debug, PartialEq)]
enum Direction { Left, Right, Up, Down }

#[derive(Debug, PartialEq)]
struct WirePath {
    pub dir: Direction,
    pub length: i32
}

#[derive(Debug, PartialEq, Eq, Hash, Clone, Copy)]
struct Point {
    pub x: i32,
    pub y: i32
}

impl fmt::Display for Point {
    fn fmt(&self, fmt: &mut fmt::Formatter) -> fmt::Result {
        fmt.write_str(
            format!("[{}, {}]", &self.x, &self.y).as_str()
        );
        Ok(())
    }
}

impl WirePath {
    pub fn new(path: String) -> WirePath {
        let direction: Direction;
        let mut chars = path.chars();

        match chars.next().unwrap() {
            'R' => { direction = Direction::Right }
            'L' => { direction = Direction::Left }
            'D' => { direction = Direction::Down }
            'U' => { direction = Direction::Up }
            _ => { panic!("Unrecognized direction!") }
        }

        let length = chars.as_str().parse::<i32>().unwrap();
        WirePath { dir: direction, length: length}
    }
}

fn prepare_input(input: &Vec<String>) -> Vec<Vec<WirePath>> {
    let mut output = Vec::<Vec<WirePath>>::new();

    for line in input {
        output.push(line
                    .split(",")
                    .map(|s| WirePath::new(s.to_string()))
                    .collect());
    }

    output
}

fn extend_point(point: &Point, dir: &Direction) -> Point {
    match dir {
        Direction::Up => {
            return Point { x: point.x, y: point.y + 1 }
        }
        Direction::Down => {
            return Point { x: point.x, y: point.y - 1 }
        }
        Direction::Left => {
            return Point { x: point.x - 1, y: point.y }
        }
        Direction::Right => {
            return Point { x: point.x + 1, y: point.y }
        }
    }
}

fn lay_wire(origin: Point, path: &Vec<WirePath>) -> Vec<Point> {
    let mut current_point: Point = origin;
    let mut laid_wire = Vec::new();

    for path in path {
        for _x in 1..(path.length + 1) {
            current_point = extend_point(&current_point, &path.dir);
            laid_wire.push(current_point);
        }
    }

    laid_wire
}

fn part_01(input: &Vec<Vec<WirePath>>) {
    const ORIGIN: Point = Point { x: 0, y: 0 };

    let first_wire  = lay_wire(ORIGIN, input.get(0).unwrap());
    let second_wire = lay_wire(ORIGIN, input.get(1).unwrap());

    let set_first: HashSet<_> = first_wire.iter().collect();
    let set_second: HashSet<_> = second_wire.iter().collect();

    let manhattan  = set_first.intersection(&set_second)
        .map(|p| p.x.abs() + p.y.abs())
        .min_by(|a,b| a.partial_cmp(b).expect("Tried to compare a NaN")).unwrap();

    println!("smallest distance = {}", manhattan);
    //dbg!(matching.count());
}

//fn part_02(input: &Vec<String>) -> () {
//}

fn main() {
    let input = lines_from_file("input/input.txt");
    let parsed = prepare_input(&input);

    part_01(&parsed);
    //part_02(&input);
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn it_creates_new_wire_path() {
        let wire_path = WirePath::new("R10".to_string());

        assert_eq!(wire_path.dir, Direction::Right);
        assert_eq!(wire_path.length, 10);

        let wire_path = WirePath::new("D1".to_string());

        assert_eq!(wire_path.dir, Direction::Down);
        assert_eq!(wire_path.length, 1);

        let wire_path = WirePath::new("U1000".to_string());

        assert_eq!(wire_path.dir, Direction::Up);
        assert_eq!(wire_path.length, 1000);
    }

    #[test]
    fn it_prepares_input() {
        let input = vec!["R10,U5,L3".to_string(), "L2,D2".to_string()];
        let parsed = prepare_input(&input);

        assert_eq!(
            parsed,
            vec![
                vec![
                    WirePath { dir: Direction::Right, length: 10 },
                    WirePath { dir: Direction::Up, length: 5 },
                    WirePath { dir: Direction::Left, length: 3 }
                ],
                vec![
                    WirePath { dir: Direction::Left, length: 2 },
                    WirePath { dir: Direction::Down, length: 2 }
                ]
            ]
        );
    }

    #[test]
    fn it_extends_point() {
        let p1 = Point { x: 0, y: 0 };

        assert_eq!(extend_point(&p1, &Direction::Up), Point { x: 0, y: 1 });
        assert_eq!(extend_point(&p1, &Direction::Down), Point { x: 0, y: -1 });
        assert_eq!(extend_point(&p1, &Direction::Left), Point { x: -1, y: 0 });
        assert_eq!(extend_point(&p1, &Direction::Right), Point { x: 1, y: 0 });
    }

    #[test]
    fn it_lays_wire() {
        let input = prepare_input(&vec!["R2,U1".to_string()]);
        let parsed_input = input.first().unwrap();
        let origin = Point { x: 0, y: 0 };

        let wire = lay_wire(origin, parsed_input);

        assert_eq!(
            wire,
            vec![
                Point { x: 1, y: 0 },
                Point { x: 2, y: 0 },
                Point { x: 2, y: 1 }
            ]
        )
    }
}
