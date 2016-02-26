package checkers;

public class Move {
	Coordinate _start, _end;

	Move(Coordinate start, Coordinate end) {
		_start = start;
		_end = end;
	}

	Move(Coordinate start, int end_x, int end_y) {
		_start = start;
		_end = new Coordinate(end_x, end_y);
	}

	Move(int start_x, int start_y, Coordinate end) {
		_start = new Coordinate(start_x, start_y);
		_end = end;
	}

	Move(int start_x, int start_y, int end_x, int end_y) {
		_start = new Coordinate(start_x, start_y);
		_end = new Coordinate(end_x, end_y);
	}

	Coordinate getStart() {
		return _start;
	}

	Coordinate getEnd() {
		return _end;
	}

	boolean outOfBounds() {
		if (_end.getRow() < 0 || _end.getRow() > 7 || _end.getCol() < 0 || _end.getCol() > 7 ||
			_start.getRow() < 0 || _start.getRow() > 7 || _start.getCol() < 0 || _start.getCol() > 7) {
			return true;
		}
		return false;
	}

	boolean isJump() {
		return (Math.abs(_start.getRow() - _end.getRow()) == 2 &&
				Math.abs(_start.getCol() - _end.getCol()) == 2);
	}

	@Override
	public String toString() {
		String result = "(" + _start.getRow() + ", " + _start.getCol() + ") -> (" + _end.getRow() + ", " + _end.getCol() + ")";
		return result;
	}
}