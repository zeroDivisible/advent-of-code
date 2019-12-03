use std::{
    collections::HashMap,
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

#[derive(Debug, PartialEq, Eq, Hash, Clone)]
struct Point {
    pub x: i32,
    pub y: i32
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
    let line = input.first().unwrap();
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

fn lay_wire(plane: &mut HashMap<Point, Vec<i32>>, origin: Point, path: &Vec<WirePath>, wire_id: i32) {
    let mut current_point = origin.clone();

    for path in path {
        for _x in 1..(path.length + 1) {
            current_point = extend_point(&current_point.clone(), &path.dir);

            if plane.contains_key(&current_point) {
                if !plane.get(&current_point).unwrap().contains(&wire_id) {
                    plane.get_mut(&current_point).unwrap().push(wire_id);
                }
            } else {
                plane.insert(current_point.clone(), vec![wire_id]);
            }
        }
    }
}

fn part_01(input: &Vec<Vec<WirePath>>) -> () {
    const ORIGIN: Point = Point { x: 0, y: 0 };
    let mut wire_id = 1;
    let mut plane = HashMap::new();

    for path in input {
        lay_wire(&mut plane, ORIGIN, path, wire_id);
        wire_id += 1;
    }

    let mut points: Vec<Point> = Vec::new();

    for (k,v) in plane {
        if v.len() > 1 {
            points.push(k);
        }
    }

    dbg!(points.iter()
        .map(|p| p.x.abs() + p.y.abs())
        .min_by(|a,b| a.partial_cmp(b).expect("Tried to compare a NaN")).unwrap());
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
        let wire_id = 1;
        let mut plane = HashMap::new();

        lay_wire(&mut plane, origin, parsed_input, wire_id);

        assert!(plane.contains_key(&Point { x: 1, y: 0}));
        assert!(plane.contains_key(&Point { x: 2, y: 0}));
        assert!(plane.contains_key(&Point { x: 2, y: 1}));
    }
}
