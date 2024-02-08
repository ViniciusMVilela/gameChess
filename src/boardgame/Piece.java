package boardgame;

// se uma classe possui algum método abstrato, esta também é abstrata
public abstract class Piece {
    protected Position position;
    private Board board;

    public Piece(Board board) {
        this.board = board;
        this.position = null; // posição de uma peça recém-criada será nula
    }

    protected Board getBoard() {
        return board;
    }

    // iremos para cada piece aplicar a sua matriz de movimentos possíveis
    public abstract boolean[][] possibleMoves();

    //testa se a peça pode se mover para uma dada posição
    // usamos a matriz de peça da classe específica e passamos a posição
    public boolean possibleMove(Position position) {
        // Hook methods → método que faz um gancho que a subclasse
        return possibleMoves()[position.getRow()][position.getColumn()];
    }

    // vamos ver se na matriz existe alguma posição verdadeira
    public boolean isThereAnyPossibleMove() {
        boolean[][] mat = possibleMoves();
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat.length; j++) {
                if (mat[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }
}
