package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

// uma extenção de ChessPiece
public class Rook extends ChessPiece {

    public Rook(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "R";
        // para imprimir a inicial da peça no tabuleiro
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColums()];

        // variável auxiliar, vai receber os valores das casas do tabuleiro
        // nas respectivas direções e verificar se o movimento é possível ou não
        Position p = new Position(0, 0);

        // above
        // posição na mesma coluna, decrementando a linha
        p.setVales(position.getRow() - 1, position.getColumn());
        // enquanto a posição p existir e não houver peça lá, é movimento R possível
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;

            // decremantamos mais uma linha para continuar a verificação
            p.setRow(p.getRow() - 1);
        }
        // agora testamos se essa casa ocupada é uma peça adversária
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        //left
        // posição na mesma linha, decrementando a coluna
        p.setVales(position.getRow(), position.getColumn() - 1);
        // enquanto a posição p existir e não houver peça lá, é movimento R possível
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;

            // decremantamos mais uma linha para continuar a verificação
            p.setColumn(p.getColumn() - 1);
        }
        // agora testamos se essa casa ocupada é uma peça adversária
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        //right
        // posição na mesma linha, incrementando a coluna
        p.setVales(position.getRow(), position.getColumn() + 1);
        // enquanto a posição p existir e não houver peça lá, é movimento R possível
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;

            // decremantamos mais uma linha para continuar a verificação
            p.setColumn(p.getColumn() + 1);
        }
        // agora testamos se essa casa ocupada é uma peça adversária
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // below
        // posição na mesma coluna, incrementando a linha
        p.setVales(position.getRow() + 1, position.getColumn());
        // enquanto a posição p existir e não houver peça lá, é movimento R possível
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;

            // decremantamos mais uma linha para continuar a verificação
            p.setRow(p.getRow() + 1);
        }
        // agora testamos se essa casa ocupada é uma peça adversária
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        return mat;
    }
}
