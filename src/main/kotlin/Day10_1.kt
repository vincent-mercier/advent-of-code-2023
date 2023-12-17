import java.io.File
import java.io.InputStream

enum class Direction(val deltaY: Int, val deltaX: Int, val nextDirection: Map<Char, () -> Direction?>) {
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

data class Point(val y: Int, val x: Int) {
    fun moveIn(direction: Direction, matrix: List<String>): Pair<Point, Direction?> {
        val newPoint = Point(
            y + direction.deltaY,
            x + direction.deltaX
        )
        return newPoint to direction.nextDirection[matrix[newPoint.y][newPoint.x]]?.invoke()
    }

    companion object {
        fun fromCoords(coords: Pair<Int, Int>): Point {
            return Point(coords.first, coords.second)
        }
    }
}

fun main() {
    val inputStream: InputStream = File("./src/main/resources/day10.txt").inputStream()

    val matrix = inputStream.bufferedReader().readLines().toMutableList()

    val s = Point.fromCoords(
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

    var nextDirection = listOf(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT)
        .filter { s.moveIn(it, matrix).second != null }
        .first()


    do {
        steps++
        val next = currentPosition.moveIn(nextDirection, matrix)
        currentPosition = next.first
        nextDirection = next.second!!
    } while (currentPosition != s)

    print(steps / 2.0)
}
