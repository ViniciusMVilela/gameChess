package chess;

//irá fazer a convesão da Posição em formato matriz (8-8) para formato do
// tabuleiro (8-h)

import boardgame.Position;

public class ChessPosition {

    // agora em char porque as colunas vão de → a...h
    private char column;
    private int row;

    public ChessPosition( char column, int row) {
        if(column < 'a' || column > 'h' || row < 1 || row >8) {
            throw new ChessException("Error instantiating ChessPosition. Valid values are from a1 to h8.");
        }

        this.row = row;
        this.column = column;
    }

    // somente getters assim como no Board, não podemos permitir alteração após
    // a criação, para mover a peça já temos métodos com essa responsabilidade
    public char getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    // realizando a conversão da posição do xadrez para a posição normal
    // matrix_row = 8 - chess_row
    // matrix_column = chess_column - 'a'

    protected Position toPosition() {
        return new Position(8 - row, column - 'a');
    }

    //convertando posição da matriz para posição do xadrez
    protected static ChessPosition fromPosition(Position position) {
            // temos que realizar um casting para char na coluna
        return new ChessPosition((char) ('a' + position.getColumn()), 8 - position.getRow());
    }

    @Override
    public String toString() {
        return "" + column + row;
    }
}
