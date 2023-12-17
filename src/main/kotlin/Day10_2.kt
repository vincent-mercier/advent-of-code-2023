import java.io.File
import java.io.InputStream
import java.lang.Integer.max
import java.lang.Integer.min


enum class Direction2(val deltaY: Int, val deltaX: Int, val nextDirection: Map<Char, () -> Direction2?>) {
    S(
        0,
        0,
        mapOf(
            'S' to { S }
        )
    ),
    UP(
        -1,
        0,
        mapOf(
            '|' to { UP },
            'F' to { RIGHT },
            '7' to  { LEFT },
            '.' to { null },
            '-' to { null },
            'L' to { null },
            'J' to { null },
            'S' to { S }
        )
    ),
    DOWN(
        1,
        0,
        mapOf(
            '|' to { DOWN },
            'J' to { LEFT },
            'L' to  { RIGHT },
            '.' to { null },
            '-' to { null },
            '7' to { null },
            'F' to { null },
            'S' to { S }
        )
    ),
    LEFT(
        0,
        -1,
        mapOf(
            '-' to { LEFT },
            'L' to { UP },
            'F' to  { DOWN },
            '.' to { null },
            '|' to { null },
            '7' to { null },
            'J' to { null },
            'S' to { S }
        )
    ),
    RIGHT(
        0,
        1,
        mapOf(
            '-' to { RIGHT },
            'J' to { UP },
            '7' to  { DOWN },
            '.' to { null },
            '|' to { null },
            'L' to { null },
            'F' to { null },
            'S' to { S }
        )
    )
}

data class Point2(val y: Int, val x: Int) {
    fun moveIn(direction: Direction, matrix: List<String>): Pair<Point2, Direction?>? {
        val newPoint = Point2(
            y + direction.deltaY,
            x + direction.deltaX
        )
        return newPoint to direction.nextDirection[matrix[newPoint.y][newPoint.x]]?.invoke()
    }

    companion object {
        fun fromCoords(coords: Pair<Int, Int>): Point2 {
            return Point2(coords.first, coords.second)
        }
    }
}

fun main() {
    val inputStream: InputStream = File("./src/main/resources/day10.txt").inputStream()

    val matrix = inputStream.bufferedReader().readLines().toMutableList()

    val s = Point2.fromCoords(
        matrix.mapIndexed {lineIndex, line ->
            val sIndex = line.mapIndexed { charIndex, char ->
                if (char == 'S') {
                    charIndex
                } else {
                    null
                }
            }.firstOrNull { it != null }

            if (sIndex != null) {
                lineIndex to sIndex
            } else {
                null
            }
        }.first { it != null }!!
    )

    var steps = 0
    var currentPosition = s

    val directions = listOf(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT)
        .filter { s.moveIn(it, matrix)?.second != null }

    val replacement = if (directions.contains(Direction.UP) && directions.contains(Direction.RIGHT)) {
        'L'
    } else if (directions.contains(Direction.UP) && directions.contains(Direction.LEFT)) {
        'J'
    } else if (directions.contains(Direction.DOWN) && directions.contains(Direction.LEFT)) {
        '7'
    } else if (directions.contains(Direction.DOWN) && directions.contains(Direction.RIGHT)) {
        'F'
    } else if (directions.contains(Direction.UP) && directions.contains(Direction.DOWN)) {
        '|'
    } else {
        '-'
    }

    matrix[s.y] = matrix[s.y].replaceRange(s.x until s.x+1, "$replacement")

    var nextDirection = directions.first()

    val points = mutableSetOf(s)
    var minY = matrix.size
    var minX = matrix[0].length
    var maxY = 0
    var maxX = 0

    do {
        steps++
        val next = currentPosition.moveIn(nextDirection, matrix)
        currentPosition = next!!.first
        nextDirection = next.second!!
        points.add(currentPosition)
        maxX = max(maxX, currentPosition.x)
        maxY = max(maxY, currentPosition.y)
        minX = min(minX, currentPosition.x)
        minY = min(minY, currentPosition.y)
    } while (currentPosition != s)

    var countInLoop = 0

    for (y in minY..maxY) {
        var inLoop = false
        var lastBarrier: Char? = null
        for (x in minX .. maxX) {
            val char = matrix[y][x]

            if (points.contains(Point2(y, x))) {
                when (char) {
                    '|' -> {
                        inLoop = !inLoop
                    }

                    'L', 'F' -> {
                        lastBarrier = char
                    }

                    'J' -> {
                        if (lastBarrier == 'F') {
                            inLoop = !inLoop
                        }
                    }

                    '7' -> {
                        if (lastBarrier == 'L') {
                            inLoop = !inLoop
                        }
                    }
                }
            } else {
                countInLoop += if (inLoop) 1 else 0
            }
        }
    }

    print(countInLoop)

}
