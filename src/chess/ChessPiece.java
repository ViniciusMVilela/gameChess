package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

public abstract class ChessPiece extends Piece {
    private Color color;
    private int moveCount;

    public ChessPiece(Board board, Color color) {
        super(board);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public void increaseMoveCount() {
        moveCount++;
    }

    public void decreaseMoveCount() {
        moveCount--;
    }

    public ChessPosition getChessPosition() {
        // vamos converter uma posição para posição de xadrez
       return ChessPosition.fromPosition(position);
    }

    // método para verificar se existe uma peça oponente na casa target, pois
    // se tiver, a peça ainda pode se mover até essa posição e realizar a captura
    protected boolean isThereOpponentPiece(Position position) {
        // precisamos fazer um downcasting, chesspiece usa ChessPosition
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        // verificamos se a posição não é nula, e se a cor desta peça ocupante
        // é diferente da cor da minha peça
        return p != null && p.getColor() != color;
    }
}
