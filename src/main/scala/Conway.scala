@main def gameOfLife(): Unit =
  val size = 5
  val board = Array.tabulate[Char](size, size)((_, _) => '-')
  board(2)(2) = '#'
  board(2)(3) = '#'
  board(3)(1) = '#'
  board(3)(2) = '#'
  printMatrix(board)
  val newState = Array.tabulate[Char](size, size)((_, _) => '-')
  for {
    r <- 0 until size
    c <- 0 until size
  } do newState(r)(c) = deadOrAlive(board, r, c)
  printMatrix(newState)
  println(System.getProperty("os.arch"))

def printMatrix(matrix: Array[Array[Char]]): Unit =
  println(matrix.map(_.mkString("|", ",", "|")).mkString("\n"))
  println("*".repeat(50))

def deadOrAlive(board: Array[Array[Char]], r: Int, c: Int): Char =
  val neighbors = getNeighbors(r, c, board.length, board(0).length)
  val aliveNeighbors = neighbors.filter(tuple => board(tuple._1)(tuple._2) == '#')
  val aliveCount = aliveNeighbors.length
  if (board(r)(c) == '#' && (aliveCount == 2 || aliveCount == 3))
    '#'
  else if (board(r)(c) == '-' && aliveCount == 3)
    '#'
  else
    '-'

def getNeighbors(r: Int, c: Int, height: Int, width: Int): Array[(Int, Int)] =
  (for {
    row <- -1 to 1 if row + r >= 0 && row + r < height
    col <- -1 to 1 if col + c >= 0 && col + c < width
  } yield (row + r, col + c)).toArray.filterNot(_ == (r, c))
