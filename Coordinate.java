package checkers;

public class Coordinate {
	private int _x, _y;

	Coordinate(int r, int c) {
		_x = r;
		_y = c;
	}

	int getRow() {
		return _x;
	}

	int getCol() {
		return _y;
	}

	@Override
	public String toString() {
		return "(" + _x + ", " + _y + ")";
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Coordinate)) {
			return false;
		}
		Coordinate o = (Coordinate) obj;
		if ((o.getRow() == this._x) && (o.getCol() == this._y)) {
			return true;
		}
		return false;
	}
}